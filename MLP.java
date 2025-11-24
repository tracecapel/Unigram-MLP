import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) throws FileNotFoundException {
    File file = new File("simple.csv");
    HashMap<Integer, String> convoMap = Parse.initializeConversationMap(file);
    HashMap<String, Integer> wordmap = Parse.initializeWordMap(convoMap);
    Map<Integer, String> reverseMap = new HashMap<>();
    int[][] output = Tokenize.tokenize(wordmap, convoMap);

    for (Map.Entry<String, Integer> entry : wordmap.entrySet()) {
      reverseMap.put(entry.getValue(), entry.getKey());
    }

    MLP mlp = new MLP(25, wordmap.size());
    mlp.train(100000, wordmap, convoMap);

    System.out.println("Demo prediction:");
    for (int i = 0; i < 10; i++) {
      if (i == 0) {
        System.out.print(reverseMap.get(0) + " ");
      } else {
        int[] input = new int[1];
        input[0] = i-1;
        int targetWord = mlp.feedNetwork(input);
        System.out.print(reverseMap.get(targetWord) + " ");
      }
    }
    System.out.println("\n");

    Scanner scnr = new Scanner(System.in);
    System.out.println(
        "Enter a sentence (the model will predict based on the last word):");
    String sentence = scnr.nextLine().toLowerCase().trim();

    String[] words = sentence.split("\\s+");

    if (words.length == 0) {
      System.out.println("enter more than 1 word");
      scnr.close();
      return;
    }

    String lastWord = words[words.length - 1];

    if (wordmap.containsKey(lastWord)) {
      int[] input = new int[1];
      input[0] = wordmap.get(lastWord);
      int predictedToken = mlp.feedNetwork(input);
      String predictedWord = reverseMap.get(predictedToken);

      System.out.println("\nYour sentence: " + sentence);
      System.out.println("Last word: " + lastWord);
      System.out.println("Predicted next word: " + predictedWord);
      System.out.println(
          "\nComplete sentence: " + sentence + " " + predictedWord);
    } else {
      System.out.println(
          "Last word '" + lastWord + "' does not exist in wordmap");
    }

    scnr.close();
  }
}
