import net.datastructures.Node;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.io.IOException;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

import net.datastructures.Position;
import net.datastructures.PositionList;
import net.datastructures.TreeNode;

import java.util.HashMap;

/**
 * Created by christophernheu on 17/10/2015.
 */
public class CompressedSuffixTrie {
    //TODO: Define data structures for the compressed trie
    int ALPHABET_SIZE = 4;

    /** Constructor */
    public CompressedSuffixTrie (String f) {
        //TODO: create a compressed suffix trie from file f
//        String analysisString = fileToString(f);
//        System.out.println(analysisString);
//
//        SuffixTrieNode suffixTrieNode = new SuffixTrieNode(analysisString);
//        System.out.println(suffixTrieNode);


        String[] stringArray = new String[4];
        stringArray[0] = "Happy";
        stringArray[1] = "Birthday";
        stringArray[2] = "to";
        stringArray[3] = "me!";

        SuffixTrieNode suffixTrieNodeRoot = new SuffixTrieNode(null, null);
        SuffixTrieNode suffixTrieNode0 = new SuffixTrieNode(stringArray[0], suffixTrieNodeRoot);
        SuffixTrieNode suffixTrieNode1 = new SuffixTrieNode(stringArray[1], suffixTrieNodeRoot);
        SuffixTrieNode suffixTrieNode2 = new SuffixTrieNode(stringArray[2], suffixTrieNodeRoot);
        SuffixTrieNode suffixTrieNode3 = new SuffixTrieNode(stringArray[3], suffixTrieNodeRoot);

        suffixTrieNodeRoot.addChild(suffixTrieNode0);
        suffixTrieNodeRoot.addChild(suffixTrieNode1);
        suffixTrieNodeRoot.addChild(suffixTrieNode2);
        suffixTrieNodeRoot.addChild(suffixTrieNode3);

        System.out.println(suffixTrieNodeRoot.element());
        System.out.println(suffixTrieNodeRoot.getChildren().size());

        for (SuffixTrieNode child: (ArrayList<SuffixTrieNode>) suffixTrieNodeRoot.getChildren()) {
            System.out.println(child);
        }



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
    public class SuffixTrieNode<String> {


        private String element;  // element stored at this node
        private SuffixTrieNode<String> parent;  // adjacent node
        private ArrayList<SuffixTrieNode> children = new ArrayList<SuffixTrieNode>(ALPHABET_SIZE+1); //TODO: check that the max size of CompressedSuffixTrie children is AlphabetSize;  // children nodes

        /** Default constructor */
        public SuffixTrieNode() { }

        /** Main constructor */
        public SuffixTrieNode(String element, SuffixTrieNode<String> parent) {
            setElement(element);
            setParent(parent);
//            setChildren(children);
        }
        /** Returns the element stored at this position */
        public String element() { return element; }
        /** Sets the element stored at this position */
        public void setElement(String o) { element=o; }
        /** Returns the children of this position */
        public ArrayList<SuffixTrieNode> getChildren() { return children; }
        /** Sets the right child of this position */
        public void setChildren(ArrayList<SuffixTrieNode> c) { children=c; }
        /** Returns the parent of this position */
        public SuffixTrieNode<String> getParent() { return parent; }
        /** Sets the parent of this position */
        public void setParent(SuffixTrieNode<String> v) { parent=v; }

        /** Add individual child node based on label **/
        public void addChild(SuffixTrieNode child) {
            this.children.add(child);
        }


        public java.lang.String toString() { return (java.lang.String) this.element;}


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