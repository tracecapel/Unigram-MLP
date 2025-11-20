import java.util.HashMap;
import java.util.Map;

public class MLP {
  Perceptron[] hiddenLayer;
  Perceptron[] outputLayer;
  double learnrate;
  int wordChunkSize;

  public MLP(int hiddenlayersize) {
    this.wordChunkSize = 1;
    this.hiddenLayer = new Perceptron[hiddenlayersize];
    for (int i = 0; i < hiddenLayer.length; i++) {
      hiddenLayer[i] = new Perceptron(wordChunkSize);
    }
    this.outputLayer = new Perceptron[hiddenlayersize];
    for (int i = 0; i < hiddenLayer.length; i++) {
      outputLayer[i] = new Perceptron(hiddenLayer.length);
    }
    this.learnrate = .1;
  }

  public int feedNetwork(int[] inputs) {
    if (inputs.length != wordChunkSize) {
      return 0;
    }

    for (int i = 0; i < hiddenLayer.length; i++) {
      for (int j = 0; j < inputs.length; j++) {
        // System.err.print("Input:  " + inputs[j] + " ");
        hiddenLayer[i].x[j] = inputs[j];
      }
    }

    for (int i = 0; i < outputLayer.length; i++) {
      for (int j = 0; j < hiddenLayer.length; j++) {
        outputLayer[i].x[j] = hiddenLayer[j].activate();
      }
    }

    return getNetworkDecision();
  }

  public int getNetworkDecision() {
    int index = 0;

    double max = 0;
    for (int i = 0; i < outputLayer.length; i++) {
      double predict = outputLayer[i].activateOutput();
      if (predict > max) {
        max = predict;
        index = i;
        // System.out.println(i);
      }
    }

    // System.out.println(max);
    return index;
  }

  public void printNetworkMappings(HashMap<String, Integer> wordMap) {
    Map<Integer, String> reverseMap = new HashMap<>();
    for (Map.Entry<String, Integer> entry : wordMap.entrySet()) {
      reverseMap.put(entry.getValue(), entry.getKey());
    }
    for (int i = 0; i < outputLayer.length; i++) {
      // System.out.println("Output node: " + i + " mapped to word " +
      // reverseMap.get(i) + " -> "+outputLayer[i].activateOutput());
    }
  }

  public void backpropagate(int targetWordIndex) {
    double[] outputDeltas = new double[outputLayer.length];

    for (int i = 0; i < outputLayer.length; i++) {
      double prediction = outputLayer[i].activateOutput();

      double target = 0;
      if (targetWordIndex == i) {
        target = 1;
      }
      double error = target - prediction;
      double errorGrad = (prediction) * (1 - prediction);
      double deltaOutput = error * errorGrad;
      outputDeltas[i] = deltaOutput;

      for (int j = 0; j < outputLayer[i].w.length; j++) {
        outputLayer[i].w[j] =
            outputLayer[i].w[j] + learnrate * deltaOutput * outputLayer[i].x[j];
      }
      outputLayer[i].bias = outputLayer[i].bias + learnrate * deltaOutput;
    }

    // Hidden layer updates
  }

  public void train(int epochs, HashMap<String, Integer> wordMap,
      HashMap<Integer, String> convoMap) {
    int[][] wordToNums = Tokenize.tokenize(wordMap, convoMap);
    Map<Integer, String> reverseMap = new HashMap<>();
    for (Map.Entry<String, Integer> entry : wordMap.entrySet()) {
      reverseMap.put(entry.getValue(), entry.getKey());
    }

    for (int epoch = 0; epoch < epochs; epoch++) {
      // System.out.println(epoch + "/" + epochs);
      for (int i = 0; i < wordToNums.length; i++) {
        int[] context = new int[wordChunkSize];
        int index = 0;
        for (int j = 0; j < wordToNums[i].length; j++) {
          context[index] = wordToNums[i][j];
          index++;

          if (index == wordChunkSize) {
            // System.out.println("----------------START --------------------");
            for (int g = 0; g < context.length; g++) {
              //  System.out.print(reverseMap.get(context[g]) + " ");
            }
            // System.out.println();
            feedNetwork(context);

            if (j + 1 < wordToNums[i].length) {
              // System.out.println("Trained on: " +
              // reverseMap.get(wordToNums[i][j + 1]));
              backpropagate(wordToNums[i][j + 1]);
              // System.out.println("----------------END --------------------");
            }

            context = new int[wordChunkSize];
            index = 0;
          }
        }
      }
    }
  }
}
