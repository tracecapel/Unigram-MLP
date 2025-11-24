public class Perceptron {
  double x[]; // Inputs
  double w[]; // Weights
  double bias; // Bias

  public Perceptron(int numInputs) {
    this.x = new double[numInputs];
    this.w = new double[numInputs];
    for (int i = 0; i < w.length; i++) {
      w[i] = Math.random() * .1;
    }
    this.bias = 0;
  }

  // RELU activation
  public double activate() {
    double weightedSum = 0;

    for (int i = 0; i < x.length; i++) {
      weightedSum += x[i] * w[i];
    }

    weightedSum += bias;

    return 1 / (1 + Math.exp(-weightedSum));
  }

  // Sigmoid activation
  public double activateOutput() {
    double weightedSum = 0;

    for (int i = 0; i < x.length; i++) {
      weightedSum += x[i] * w[i];
    }

    weightedSum += bias;

    return 1 / (1 + Math.exp(-weightedSum));
  }

  // Weighted sum
  public double getWeightedSum() {
    double weightedSum = 0;

    for (int i = 0; i < x.length; i++) {
      weightedSum += x[i] * w[i];
    }

    weightedSum += bias;

    return weightedSum;
  }
}
