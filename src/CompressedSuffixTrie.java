import com.sun.jmx.remote.internal.ArrayQueue;
import net.datastructures.Node;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.io.IOException;
import java.io.BufferedWriter;
import java.util.*;

import net.datastructures.Position;
import net.datastructures.PositionList;
import net.datastructures.TreeNode;
import sun.awt.image.ImageWatched;

import javax.naming.directory.InvalidAttributeValueException;

/**
 * Created by christophernheu on 17/10/2015.
 */
public class CompressedSuffixTrie {
    //TODO: Define data structures for the compressed trie
    int ALPHABET_SIZE = 4;
    SuffixTrieNode root;
    char[] sourceArray;
    int sourceSize;

    /** Constructor */
    public CompressedSuffixTrie (String f) {

        // Initialise class attributes
        root = new SuffixTrieNode(null,null,-1);
        String sourceString = fileToString(f);
        sourceArray = sourceString.toCharArray();
        sourceSize = sourceArray.length;

        System.out.println(sourceString);

        int[] suffixStartIndexArray = new int[sourceArray.length];
        String[] suffixStringArray = new String[sourceArray.length];
        generateSuffixes(sourceString, suffixStringArray, suffixStartIndexArray);

        for (int i = 0; i< suffixStringArray.length; i++) {
            addSuffix(suffixStringArray[i], suffixStartIndexArray[i]);
        }
//
        compressSuffixTrie();
//        this.printLabels();

    }

    /** Method for finding the first occurrence of a pattern s in the DNA sequence.
     * Designed for non-compressed suffixTrie */
    public int findString (String query) {
        int indexOfFirstChar = -1;
        int childIndex;
        SuffixTrieNode node = this.root;

        LinkedList queryList = new LinkedList<>(); // O(1) - peek, pop, add functions.

        char[] queryArray = query.toCharArray();
        for (char c : queryArray) {
            queryList.add(c);
        }
        int sizeOfQuery = queryList.size();


        try {

            /**
             1. Visit the childNode with compact label starting at current queryIndex
             2. Check compactLabel endIndex
             3. for that node, from start to end of compact label, cross-reference each char in queryArray, If it fails
             4. doesn't end the same we kow we failed.
             */

            while (!queryList.isEmpty()) {
                childIndex = mapCharToIndex((char) queryList.peekFirst());
                if (childIndex == -1) {
                    throw new InvalidAttributeValueException("Invalid character not part of alphabet ACGT");
                }

                if (node.getChild(childIndex) == null) {
                    System.out.println("Suffix: " + query + " was not found");
                    return -1;
                } else {
                    int startIndex = node.getChild(childIndex).compactLabel()[0];
                    int endIndex = node.getChild(childIndex).compactLabel()[1];
//                    if (indexOfFirstChar < 0) indexOfFirstChar = startIndex;

                    for (int i = startIndex; i <= endIndex; i++) { // cross reference each char in the queryList with the char at index in the original sourceArray
                        char charInQuery = (char) queryList.pop();
                        if (sourceArray[i] != charInQuery) {
                            System.out.println("Suffix: " + query + " was not found at char: " + charInQuery);
                            return -1;
                        }
                        if (queryList.isEmpty()) {
                            indexOfFirstChar = i - (sizeOfQuery - 1);
                            break;
                        }
                    }
                    node = node.getChild(childIndex); // advance the node
                }
            }
            System.out.println(query);

        }
        catch (InvalidAttributeValueException e) {
            System.out.println("Invalid character in string to be found that is not in the alphabet ACGT");
            return -1;
        }
        return indexOfFirstChar;
    }

    /** Method for computing the degree of similarity of two DNA sequences stored in the text files f1 and f2 */
    public static float similarityAnalyser(String f1, String f2, String f3) {
        return 0;
    }

    /** Nested protected class SuffixTrieNode which adapts the Node class for our purposes */
    public class SuffixTrieNode {


        protected String label;  // label stored at this node
        protected int[] compactLabel;
        protected SuffixTrieNode parent;  // adjacent node
        protected ArrayList<SuffixTrieNode> children = new ArrayList<>(ALPHABET_SIZE+1); //TODO: check that the max size of CompressedSuffixTrie children is AlphabetSize;  // children nodes
        protected int stringIndex;
        protected int numOfChildren;
//        /** Default constructor */
//        public SuffixTrieNode() {  }

        //TODO: consider adding sentinel node
        
        /** Main constructor */
        public SuffixTrieNode(String label, SuffixTrieNode parent, int index) {
            setLabel(label);
            setParent(parent); // TODO: may not need parent
            numOfChildren = 0;
            compactLabel = new int[2];
            stringIndex = index;
            compactLabel[0] = index;
            compactLabel[1] = index;
            
            for (int i = 0; i < ALPHABET_SIZE + 1; i++) {
                children.add(i,null);
            }

        }
        /** Returns the label stored at this position */
        public String label() { return label; }

        /** Sets the label stored at this position */
        public void setLabel(String o) { label=o; }

        /** Returns the compactLabel stored at this position */
        public int[] compactLabel() { return compactLabel; }

        /** Sets the second integer of compactLabel stored at this position, used for updating during construction */
        public void setCompactLabel(int newIndex) { compactLabel[1] = newIndex; }

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
        public void setParent(SuffixTrieNode v) { parent=v; } // TODO: may not need parent

        /** Add individual child node based on index **/
        public void setChild(int index, SuffixTrieNode child) {
            this.children.set(index, child);
            numOfChildren ++;
        }

//        /** Remove individual child node based on index */
//        public SuffixTrieNode removeChild(int index) {
//            SuffixTrieNode removedChild = this.children.get(index);
//            this.children.set(index, null);
//            numOfChildren --;
//            return removedChild;
//        }
        protected int locateSingleChildIndex() {
            for (int childIndex = 0; childIndex < 5; childIndex ++) {
                if (this.getChild(childIndex) != null) {
                    return childIndex;
                }
            }
            return -1;
        }

        public String toString() { return this.label; }
    }

    /** Convert file into String */
    public String fileToString(String fileName) {
        Path path = Paths.get("");
        fileName = path.toAbsolutePath().toString() + "/" + fileName + ".txt";
        System.out.println(fileName);
        File file = new File(fileName);
        String inputString = ""; // O(1)
        char[] next;

        try {
            Scanner input = new Scanner(file);
            while (input.hasNext()) { // O(n) - at worst, if each task attribute is on a separate line
                next = input.next().toCharArray();
                // filter each char to make sure they are inside the alphabet
                for (char c: next) {
                    if (c == 'A' || c == 'C' || c == 'G' || c == 'T') inputString += Character.toString(c);
                }
                // Check each iteration of inputString for diagnostics
//                System.out.println(inputString);
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
        return inputString+"$";
    }


    /** Generate suffixes from inputString */
    public void generateSuffixes(String inputString, String[] suffixStringArray, int[] suffixStartIndexArray) {
//        inputString += "$";
        int size = inputString.length();
//        String[] outputStingArray = new String[size];

        for (int i = 0; i < size; i++) {
            suffixStringArray[i] = inputString.substring(i,size);
            suffixStartIndexArray[i] = i;
        }
//        return outputStingArray;
    }

    /** Add a suffix to the CompressedSuffixTrie */
    public void addSuffix(String suffix, int suffixStartIndex) { // O(n) - n is the number of characters
        char[] suffixArray = suffix.toCharArray(); // O(n)
        SuffixTrieNode node = this.root;
        int childIndex = 0;
//        ArrayList<SuffixTrieNode> childrenArrayList;

        for (int stringIndex = 0; stringIndex < suffixArray.length; stringIndex ++) {
            // get the index
            childIndex = mapCharToIndex(suffixArray[stringIndex]);

            // check to see that the child exists
            if (node.getChild(childIndex) == null) {

                SuffixTrieNode childNode = new SuffixTrieNode(Character.toString(suffixArray[stringIndex]), node, suffixStartIndex+stringIndex);
                node.setChild(childIndex, childNode);
            }
            // now move to the next node
            node = node.getChild(childIndex);
//            System.out.println(node); // print out newly added childNode
        }
    }

    /** Simple map from char to index in the children ArrayList */
    public static int mapCharToIndex(char c) { // O(1)
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
        else if (c == '$'){
            return 4;
        }
        else return -1;
    }

    public void printLabels() {
        SuffixTrieNode node = this.root;
//        LinkedList<String> store = new LinkedList<String>();
        int queueSize = (sourceSize * (sourceSize+1)) / 2;
        ArrayQueue<SuffixTrieNode> store = new ArrayQueue<>(queueSize);
        printLabelsHelper(node, store);
    }

    /** Level Order Print */
    public void printLabelsHelper(SuffixTrieNode root, ArrayQueue<SuffixTrieNode> store) {
        store.add(root);

        while (!store.isEmpty()) {
            SuffixTrieNode node = store.remove(0);

            // perform visit
            if (node.label!=null) {
                String output = node.label() + " - (" + node.compactLabel()[0] + ", " + node.compactLabel()[1] + ")";
                System.out.println(output);
            }

            // loop through children and append them as required
            for (int childIndex = 0; childIndex < 5; childIndex++) {
                if (node.getChild(childIndex) != null) {
                    store.add(node.getChild(childIndex));
                }
            }
        }
    }

    public void compressSuffixTrie() {
        SuffixTrieNode node = this.root;
        compressSuffixTrieHelper(node);

    }

    protected void compressSuffixTrieHelper(SuffixTrieNode node) {
        if (node.label!= null && node.numOfChildren == 1) { // not at root and we have one child
            SuffixTrieNode childNode;
            // find the child node which we know exists
            int childIndex = node.locateSingleChildIndex(); // O(5)

            childNode = node.getChild(childIndex);

            // concatenate current node's label to include childNode's label
            node.setLabel(node.label() + childNode.label());

            // update current node's compactLabel, second index to the childNode's index
            node.setCompactLabel(childNode.compactLabel()[0]);

            // re-set numOfChildren for this node == childNode
            node.numOfChildren = childNode.numOfChildren;
            // delete currentNode.children, delete childNode

            // make currentNode children point childNode's children
            node.setChildren(childNode.getChildren());

            if (childNode.label().equals("$")) return;
            else {
                compressSuffixTrieHelper(node);
            }
        }
        else {
            for (int childIndex = 0; childIndex < 5; childIndex++) {
                if (node.getChild(childIndex) != null) {
                    compressSuffixTrieHelper(node.getChild(childIndex));
                }

            }
        }
        return;
    }


    public static void main(String args[]) throws Exception{

    /** Construct a trie named trie1 */
        CompressedSuffixTrie trie1 = new CompressedSuffixTrie("file1");

        System.out.println("ACTTCGTAAG is at: " + trie1.findString("ACTTCGTAAG")); // 5

        System.out.println("AAAACAACTTCG is at: " + trie1.findString("AAAACAACTTCG")); // 18

        System.out.println("ACTTCGTAAGGTT : " + trie1.findString("ACTTCGTAAGGTT")); // -1

        System.out.println(CompressedSuffixTrie.similarityAnalyser("file2", "file3", "file4"));

//        CompressedSuffixTrie trie2 = new CompressedSuffixTrie("file5");
//        System.out.println(trie2.findString("AC"));
//        System.out.println(trie2.findString("ACCGTAC"));
//        System.out.println(trie2.findString("B"));
//        System.out.println(trie2.findString("ACC"));
//        System.out.println(trie2.findString("TAC"));
//        System.out.println(trie2.findString("TA"));
//        System.out.println(trie2.findString("CAT"));

//        CompressedSuffixTrie trie3 = new CompressedSuffixTrie("file6");

    }

}

