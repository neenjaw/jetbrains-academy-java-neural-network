package recognition;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.io.File;

public class Network {
    public static final int DISTINCT_VALUES = 10;
    public static final double LEARNING_RATE = 0.5;
    public static final int GENERATIONS = 2000;
    public static final double LEARNED_THRESHOLD = 0.00001;
    public static final int INPUT_NEURON_COUNT = 15;
    public static final int OUTPUT_NEURON_COUNT = DISTINCT_VALUES;

    double[][] weights;
    double[] biases;

    String fileName;

    Network() {
        weights = new double[OUTPUT_NEURON_COUNT][INPUT_NEURON_COUNT+1];
        biases = new double[OUTPUT_NEURON_COUNT];

        // fill random weights
        final Random generate = new Random();
        for (int i = 0; i < OUTPUT_NEURON_COUNT; i++) {
            for (int j = 0; j < INPUT_NEURON_COUNT + 1; j++) {
                weights[i][j] = generate.nextGaussian();
            }
            biases[i] = 1;
        }


        fileName = "./weights-data";
    }

    public void train() throws IOException {
        System.out.println("Learning...");

        learn(GENERATIONS);

        SerializationUtils.serialize(weights, fileName);
    }

    private void learn(int generations) {
        int[][] idealDigitInputs = getIdealData();

        boolean trained = false;
        for (int g = 0; !trained && g < generations; g++) {
            double[][][] deltaWeights = new double[DISTINCT_VALUES][OUTPUT_NEURON_COUNT][INPUT_NEURON_COUNT];
            double[][] deltaWeightMeans = new double[OUTPUT_NEURON_COUNT][INPUT_NEURON_COUNT];

            for (int k = 0; k < DISTINCT_VALUES; k++) {
                int[] idealDigitInput = idealDigitInputs[k];
                double[] outputForDigit = calcOutputNeurons(idealDigitInput);

                // calculate the delta weight for digit x input x output
                // prepare to calculate the mean value
                for (int i = 0; i < OUTPUT_NEURON_COUNT; i++) {
                    for (int j = 0; j < INPUT_NEURON_COUNT; j++) {
                        deltaWeights[k][i][j] = LEARNING_RATE * idealDigitInput[j] * ((k == i ? 1 : 0) - outputForDigit[i]);
                        deltaWeightMeans[i][j] += deltaWeights[k][i][j];
                    }
                }
            }

            double averageDeltaWeightMean = 0;
            // find the mean delta weights for each input x output, and update the weights
            for (int i = 0; i < OUTPUT_NEURON_COUNT; i++) {
                for (int j = 0; j < INPUT_NEURON_COUNT; j++) {
                    averageDeltaWeightMean += deltaWeightMeans[i][j];
                    deltaWeightMeans[i][j] = deltaWeightMeans[i][j] / DISTINCT_VALUES;
                    weights[i][j] = weights[i][j] + deltaWeightMeans[i][j];
                }
            }
            if (Math.abs(averageDeltaWeightMean / (OUTPUT_NEURON_COUNT * INPUT_NEURON_COUNT)) <= LEARNED_THRESHOLD) {
                trained = true;
            }
        }
    }

    public void guess(Scanner sc) throws IOException, ClassNotFoundException {
        int[] digitInputs = new int[INPUT_NEURON_COUNT];

        weights = (double[][]) SerializationUtils.deserialize(fileName);

        // init input grid
        System.out.println("Input grid:");
        for (int i = 0; i < 5; i++) {
            char[] line = sc.nextLine().toCharArray();
            digitInputs[i * 3] = 'X' == line[0] ? 1 : 0;
            digitInputs[i * 3 + 1] = 'X' == line[1] ? 1 : 0;
            digitInputs[i * 3 + 2] = 'X' == line[2] ? 1 : 0;
        }

        double[] digitOutputs = calcOutputNeurons(digitInputs);

        int num = 0;
        for (int i = 1; i < digitOutputs.length; i++) {
            if (digitOutputs[i] > digitOutputs[num]) {
                num = i;
            }
        }
        System.out.println("This number is " + num);
    }

    private int[][] getIdealData() {
        return new int[][]{
                {1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1}, // 0
                {0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0}, // 1
                {1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1}, // 2
                {1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1}, // 3
                {1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 1}, // 4
                {1, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 1}, // 5
                {1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1}, // 6
                {1, 1, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1}, // 7
                {1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1}, // 8
                {1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1}  // 9
        };
    }

    private double[] calcOutputNeurons(int[] inputNeuron){
        double[] outputNeurons = new double[OUTPUT_NEURON_COUNT];
        for (int i = 0; i < OUTPUT_NEURON_COUNT; i++) {
            for (int j = 0; j < INPUT_NEURON_COUNT; j++) {
                outputNeurons[i] += inputNeuron[j] * weights[i][j];
            }
            outputNeurons[i] = sigmoid(outputNeurons[i] + biases[i]*weights[i][INPUT_NEURON_COUNT]);
        }
        return outputNeurons;
    }

    public static double sigmoid(double sum) {
        return 1 / (1 + Math.exp(-sum));
    }
}