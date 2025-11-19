import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("train.csv");
        HashMap<Integer, String> convoMap = Parse.initializeConversationMap(file);
        HashMap<String, Integer> wordmap = Parse.initializeWordMap(convoMap);
        Map<Integer, String> reverseMap = new HashMap<>();
        int[][] output = Tokenize.tokenize(wordmap, convoMap);
        for (Map.Entry<String, Integer> entry : wordmap.entrySet()) {
    reverseMap.put(entry.getValue(), entry.getKey());
}
System.out.println(reverseMap.get(0));
        
        MLP mlp = new MLP(wordmap.size() * 2);

        mlp.train(1, wordmap, convoMap);

        int[] hello = new int[3];
        hello[0] = (int) wordmap.get("my");
        hello[1] = (int) wordmap.get("name");
        hello[2] = (int) wordmap.get("is");
        
        int targetWord = mlp.feedNetwork(hello);

    
    System.out.println(reverseMap.get(targetWord));
    }

    
}
