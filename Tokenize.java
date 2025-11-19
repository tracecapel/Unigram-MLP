import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class Tokenize {
    public static int[][] tokenize(HashMap<String, Integer> wordMap, HashMap<Integer, String> convoMap){
        int[][] output = new int[convoMap.size()][];
        int index = 0;
        for (Map.Entry<Integer, String> entry : convoMap.entrySet()) {
            String convo = entry.getValue();
            String[] convoWords = convo.split(" ");
            int[] tokenizedConvo = new int[convoWords.length];

            for(int i = 0; i < convoWords.length; i++){
                tokenizedConvo[i] = wordMap.get(convoWords[i]);
            }
            output[index] = tokenizedConvo;
            index++;
            

            

        }
        return output;
        
    }

   
    /* How to iterate over tokenized output
    for (int i = 0; i < tokenizedConvo.length; i++) {
        System.out.print("Conversation " + i + ": ");
        
        // Print each tokenized word (integer)
        for (int j = 0; j < tokenizedConvo[i].length; j++) {
            System.out.print(tokenizedConvo[i][j] + " ");
        }
        
        System.out.println(); 
    }

    */



    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("train.csv");
        HashMap<Integer, String> convoMap = Parse.initializeConversationMap(file);
        HashMap<String, Integer> wordmap = Parse.initializeWordMap(convoMap);
        double[][] output = tokenize(wordmap, convoMap);


        
        
        
    


        
        
        

        
    }
}
