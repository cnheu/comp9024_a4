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
    SuffixTrieNode root;

    /** Constructor */
    public CompressedSuffixTrie (String f) {
        //TODO: create a compressed suffix trie from file f

        root = new SuffixTrieNode(null,null);
        String analysisString = fileToString(f);

//        String[] myString = generateSuffixes(analysisString);
        String[] suffixArray = generateSuffixes("ACCGTAC$");

        for (int i = 0; i< suffixArray.length; i++) {
//            System.out.println(myString[i]);
            addSuffix(suffixArray[i]);
        }






//        String[] stringArray = new String[4];
//        stringArray[0] = "Happy";
//        stringArray[1] = "Birthday";
//        stringArray[2] = "to";
//        stringArray[3] = "me!";
//
//        SuffixTrieNode suffixTrieNodeRoot = new SuffixTrieNode(null, null);
//        SuffixTrieNode suffixTrieNode0 = new SuffixTrieNode(stringArray[0], suffixTrieNodeRoot);
//        SuffixTrieNode suffixTrieNode1 = new SuffixTrieNode(stringArray[1], suffixTrieNodeRoot);
//        SuffixTrieNode suffixTrieNode2 = new SuffixTrieNode(stringArray[2], suffixTrieNodeRoot);
//        SuffixTrieNode suffixTrieNode3 = new SuffixTrieNode(stringArray[3], suffixTrieNodeRoot);
//
//        suffixTrieNodeRoot.addChild(suffixTrieNode0);
//        suffixTrieNodeRoot.addChild(suffixTrieNode1);
//        suffixTrieNodeRoot.addChild(suffixTrieNode2);
//        suffixTrieNodeRoot.addChild(suffixTrieNode3);
//
//        System.out.println(suffixTrieNodeRoot.label());
//        System.out.println(suffixTrieNodeRoot.getChildren().size());
//
//        for (SuffixTrieNode child: (ArrayList<SuffixTrieNode>) suffixTrieNodeRoot.getChildren()) {
//            System.out.println(child);
//        }



    }

    /** Method for finding the first occurrence of a pattern s in the DNA sequence */
    public int findString (String suffix) {
        //TODO: define method
        SuffixTrieNode node = this.root;
        char[] suffixArray = suffix.toCharArray();

        for (char c: suffixArray) {

        }
        return 0;
    }

    /** Method for computing the degree of similarity of two DNA sequences stored in the text files f1 and f2 */
    public static float similarityAnalyser(String f1, String f2, String f3) {
        return 0;
    }

    /** Nested protected class SuffixTrieNode which adapts the Node class for our purposes */
    public class SuffixTrieNode {


        protected String label;  // label stored at this node
        protected SuffixTrieNode parent;  // adjacent node
        protected ArrayList<SuffixTrieNode> children = new ArrayList<>(ALPHABET_SIZE+1); //TODO: check that the max size of CompressedSuffixTrie children is AlphabetSize;  // children nodes

//        /** Default constructor */
//        public SuffixTrieNode() {  }

        /** Main constructor */
        public SuffixTrieNode(String label, SuffixTrieNode parent) {
            setLabel(label);
            setParent(parent);
//            setChildren(children);

            for (int i = 0; i < ALPHABET_SIZE + 1; i++) {
                children.add(i,null);
            }
        }
        /** Returns the label stored at this position */
        public String label() { return label; }
        /** Sets the label stored at this position */
        public void setLabel(String o) { label=o; }
        /** Returns the children of this position */
        public ArrayList<SuffixTrieNode> getChildren() { return children; }

        /** Returns the child at a given index */
        public SuffixTrieNode getChild(int index) {
            return children.get(index);
        }

        /** Sets the right child of this position */
        public void setChildren(ArrayList<SuffixTrieNode> c) { children=c; }
        /** Returns the parent of this position */
        public SuffixTrieNode getParent() { return parent; }
        /** Sets the parent of this position */
        public void setParent(SuffixTrieNode v) { parent=v; }

        /** Add individual child node based on label **/
        public void addChild(int index, SuffixTrieNode child) {
            this.children.set(index, child);
        }

        public String toString() { return this.label;}
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

    /** Generate suffixes from inputString */
    public String[] generateSuffixes(String inputString) {
        inputString += "$";
        int size = inputString.length();
//        System.out.println(inputString);
        String[] outputStingArray = new String[size];

        for (int i = 0; i < size; i++) {
            outputStingArray[i] = inputString.substring(i,size);
        }
        return outputStingArray;
    }

    /** Add a suffix to the CompressedSuffixTrie */
    public void addSuffix(String suffix) { // O(n) - n is the number of characters
        char[] suffixArray = suffix.toCharArray(); // O(n)
        SuffixTrieNode node = this.root;
        int childIndex = 0;
        ArrayList<SuffixTrieNode> childrenArrayList;

        for (char c: suffixArray) {

//            if (c == 'A') {
//                childIndex = 0;
//            }
//            else if (c == 'C') {
//                childIndex = 1;
//            }
//            else if (c == 'G') {
//                childIndex = 2;
//            }
//            else if (c == 'T') {
//                childIndex = 3;
//            }
//            else if (c == '$') {
//                childIndex = 4;
//            }
//
//            // check if there's not node at childIndex, then add a node there
//            if (node.getChildren().get(childIndex) == null) {
//                SuffixTrieNode childNode = new SuffixTrieNode(Character.toString(c), node);
//                node.addChild(childIndex, childNode);
//            }
            childIndex = getIndex(c);
            if (node.getChild(childIndex) == null) {
                SuffixTrieNode childNode = new SuffixTrieNode(Character.toString(c), node);
                node.addChild(childIndex, childNode);
            }
            System.out.println(node);
            // now move to the next node
            node = node.getChild(childIndex);

        }
    }

    public static int getIndex(char c) {
        if (c == 'A') {
            return 0;
        }
        else if (c == 'C') {
            return 1;
        }
        else if (c == 'G') {
            return 2;
        }
        else if (c == 'T') {
            return 3;
        }
        else {
            return 4;
        }
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
