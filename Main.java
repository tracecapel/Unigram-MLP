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

    Scanner scanner = new Scanner(System.in);

    MLP mlp = new MLP(wordmap.size());

    mlp.train(100, wordmap, convoMap);

    while (true) {
      String word1 = scanner.nextLine().toLowerCase().trim();
      int[] input = new int[1];

      int targetWord = mlp.feedNetwork(input);

      System.out.print(reverseMap.get(targetWord));
      mlp.printNetworkMappings(wordmap);
    }
  }
}
