
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Parse {
  public static HashMap<Integer, String> initializeConversationMap(File file)
      throws FileNotFoundException {
    HashMap<Integer, String> map = new HashMap<>();
    int index = 0;
    Scanner scanner = new Scanner(file);
    //scanner.nextLine();
    String conversation = "";

    while (scanner.hasNext()) {
      String line = scanner.nextLine();

      conversation += line;

      if (line.contains("\']\"")) {
        map.put(index, clean(conversation));
        index++;
        conversation = " ";
      }
    }

    scanner.close();

    return map;
  }

  public static HashMap<String, Integer> initializeWordMap(
      HashMap<Integer, String> convoMap) {
    int index = 0;
    HashMap<String, Integer> wordmap = new HashMap<>();
    for (Map.Entry<Integer, String> entry : convoMap.entrySet()) {
      Integer key = entry.getKey();

      String convo = clean(convoMap.get(key));

      String[] words = convo.split(" ");

      for (int i = 0; i < words.length; i++) {
        if (!wordmap.containsKey(words[i])) {
          wordmap.put(words[i], index);
          index++;
        }
      }
    }

    return wordmap;
  }

  public static String clean(String conversation) {
    if (conversation == null) {
      return null;
    }
    char[] arr = conversation.toCharArray();
    String cleanedConvo = "";

    for (int i = 0; i < arr.length; i++) {
      char c = arr[i];
      if (c == 'â') {
        cleanedConvo += 'a';
      } else if (c == '.') {
        cleanedConvo += " . ";
      } else if (Character.isAlphabetic(c) || c == ' ') {
        cleanedConvo += c + "";
      }
    }

    return cleanedConvo.replaceAll("\\s+", " ").trim().toLowerCase();
  }
}
