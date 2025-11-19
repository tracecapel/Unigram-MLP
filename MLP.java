import java.util.HashMap;

public class MLP {
  Perceptron[] hiddenLayer;
  Perceptron[] outputLayer;
  double learnrate;

  public MLP(int hiddenlayersize) {
    this.hiddenLayer = new Perceptron[hiddenlayersize];
    for (int i = 0; i < hiddenLayer.length; i++) {
      hiddenLayer[i] = new Perceptron(1250);
    }
    this.outputLayer = new Perceptron[hiddenlayersize / 2];
    for (int i = 0; i < hiddenLayer.length / 2; i++) {
      outputLayer[i] = new Perceptron(1250);
    }
  }

  public int feedNetwork(int[] inputs) {
    int zeros = 0;
    

    
    int outputIndex = 0;
    for (int i = 0; i < hiddenLayer.length; i++) {
      for (int j = 0; j < inputs.length; j++) {
        hiddenLayer[i].x[j] = inputs[j];
        
      }
      if (i % 2 == 0 && i >= 1) {
        double firsthiddenNodeOutput = hiddenLayer[i - 1].activate();

        double secondhiddenNodeOutput = hiddenLayer[i].activate();

        outputLayer[outputIndex].x[0] = firsthiddenNodeOutput;
        outputLayer[outputIndex].x[1] = secondhiddenNodeOutput;
        outputIndex++;
      }
    }

    return getNetworkDecision();
  }

  public int getNetworkDecision() {
    double outputMax = 0;
    int index = 0;
    for (int i = 0; i < outputLayer.length; i++) {
      if (outputLayer[i].activateOutput() > outputMax) {
        outputMax = outputLayer[i].activateOutput();
        index = i;
      }
    }
    return index;
  }

  public void backpropagate(int target) {
    double outputWordNodePrediction = outputLayer[target].activateOutput();
    double outputeWordNodeError = outputWordNodePrediction - target;
    double outputWordNodeGrad =
        outputWordNodePrediction * (1 - outputWordNodePrediction);
    double outputWordNodeDelta = outputeWordNodeError * outputWordNodeGrad;

    for (int i = 0; i < outputLayer[target].x.length; i++) {
      outputLayer[target].w[i] = outputLayer[target].w[i]
          + learnrate * outputWordNodeDelta * outputLayer[target].x[i];
    }
    outputLayer[target].bias =
        outputLayer[target].bias + learnrate * outputWordNodeDelta;

    // Done updating output node for correct word
    for (int i = 0; i < outputLayer.length; i++) {
      if (i == target) {
        // Do nothing on correct node
      } else {
        double outputWordNodePredictionWrong = outputLayer[i].activateOutput();
        double outputeWordNodeErrorWrong =
            outputWordNodePredictionWrong - 0; // false
        double outputWordNodeGradWrong =
            outputWordNodePredictionWrong * (1 - outputWordNodePredictionWrong);
        double outputWordNodeDeltaWrong =
            outputeWordNodeErrorWrong * outputWordNodeGradWrong;
        for (int j = 0; j < outputLayer[i].x.length; j++) {
          outputLayer[i].w[j] = outputLayer[i].w[j]
              + learnrate * outputWordNodeDeltaWrong * outputLayer[i].x[j];
        }
        outputLayer[i].bias =
            outputLayer[i].bias + learnrate * outputWordNodeDelta;
      }
    }

    // Hidden layer backprop

    for (int i = 0; i < hiddenLayer.length; i++) {
      int outputIndex = i / 2;

      double output1Predict = outputLayer[outputIndex].activateOutput();
      double output1Error = output1Predict - 0;
      double output1Grad = output1Predict * (1 - output1Predict);
      double output1Delta = output1Error * output1Grad;

      double hiddenNodeGrad = 0;
      if (hiddenLayer[i].getWeightedSum() > 0) {
        hiddenNodeGrad = 1;
      }
      double hiddenNodeDelta = output1Delta * hiddenNodeGrad;

      for (int j = 0; j < hiddenLayer[i].x.length; j++) {
        hiddenLayer[i].w[j] = hiddenLayer[i].w[j]
            + learnrate * hiddenNodeDelta * hiddenLayer[i].x[j];
      }
      hiddenLayer[i].bias = hiddenLayer[i].bias + learnrate * hiddenNodeDelta;
    }
  }

  public void train(int epochs, HashMap<String, Integer> wordMap,
    HashMap<Integer, String> convoMap) {
    int[][] wordToNums = Tokenize.tokenize(wordMap, convoMap);
    Map<Integer, String> reverseMap = new HashMap<>();
    for (Map.Entry<String, Integer> entry : wordmap.entrySet()) {
    reverseMap.put(entry.getValue(), entry.getKey());
}

    for (int i = 0; i < wordToNums.length; i++) {
      int[] context = new int[wordToNums[i].length];
      int contextIndex = 0;
      
      for (int j = 0; j < wordToNums[i].length; j++) {
        feedNetwork(context);
        for(int i = 0; i < context.length; i++){
          reverseMap.get(context[i]);
        }
        backpropagate(wordToNums[i][j]);

        context[contextIndex] = wordToNums[i][j];
      }
    }
  }

  


}
