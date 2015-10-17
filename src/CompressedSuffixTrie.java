import net.datastructures.Node;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.io.IOException;
import java.io.BufferedWriter;
import java.util.Scanner;
import net.datastructures.TreeNode;

/**
 * Created by christophernheu on 17/10/2015.
 */
public class CompressedSuffixTrie {
    //TODO: Define data structures for the compressed trie
    int ALPHABET_SIZE = 4;

    /** Constructor */
    public CompressedSuffixTrie (String f) {
        //TODO: create a compressed suffix trie from file f
        String analysisString = fileToString(f);
        System.out.println(analysisString);

        SuffixTrieNode suffixTrieNode = new SuffixTrieNode(analysisString);
        System.out.println(suffixTrieNode);


    }

    /** Method for finding the first occurrence of a pattern s in the DNA sequence */
    public int findString (String s) {
        //TODO: define method
        return 0;
    }

    /** Method for computing the degree of similarity of two DNA sequences stored in the text files f1 and f2 */
    public static float similarityAnalyser(String f1, String f2, String f3) {
        return 0;
    }

    /** Nested protected class SuffixTrieNode which adapts the Node class for our purposes */
    public class SuffixTrieNode extends TreeNode<String> {



        public SuffixTrieNode(String s) {
            super(s,null,null);
        }

        public String toString() { return this.element();}
    }

    /** Convert file into String */
    public String fileToString(String fileName) {
        Path path = Paths.get("");
        fileName = path.toAbsolutePath().toString() + "/" + fileName + ".txt";
        System.out.println(fileName);
        File file = new File(fileName);
        String inputString = ""; // O(1)

        try {
            Scanner input = new Scanner(file);
            while (input.hasNextLine()) { // O(n) - at worst, if each task attribute is on a separate line
                inputString += input.nextLine(); // O(1)
            }
            if (inputString == "") throw new Exception("(Input task file empty)");

        }
        catch(FileNotFoundException e) {
            System.out.println("[ERROR] " + e.getMessage());
            return null;
        }
        catch (Exception e) {
            System.out.println("[ERROR] " + e.getMessage());
            return null;
        }
        return inputString;
    }

    public static void main(String args[]) throws Exception{

    /** Construct a trie named trie1 */
        CompressedSuffixTrie trie1 = new CompressedSuffixTrie("file1");

//        System.out.println("ACTTCGTAAG is at: " + trie1.findString("ACTTCGTAAG"));
//
//        System.out.println("AAAACAACTTCG is at: " + trie1.findString("AAAACAACTTCG"));
//
//        System.out.println("ACTTCGTAAGGTT : " + trie1.findString("ACTTCGTAAGGTT"));
//
//        System.out.println(CompressedSuffixTrie.similarityAnalyser("file2", "file3", "file4"));
    }

}
