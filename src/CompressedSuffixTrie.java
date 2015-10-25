import net.datastructures.NodeQueue;
import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.lang.Math;
import javax.naming.directory.InvalidAttributeValueException;


/**
 * Title: Assignment 4
 * Course: COMP9024 - Semester 2, 2015
 * Author: Christopher Nheu
 * Author ID: z3240967
 */
public class CompressedSuffixTrie {
    int ALPHABET_SIZE = 4;
    SuffixTrieNode root;
    char[] sourceArray;
    int sourceSize;

    /**
     * Constructor - the overall strategy is to 1) create non-compressed suffixTrie, 2) do a pass through the suffixTrie and compress/compactify it
     * We store both a label as a String and the compactLabel as an array of Integers to represent the CompressedSuffixTrie ADT.
     *
     * Time Complexity: O(n2) - where n is the number of characters in the original source string.
     * Note: if time permits we could carry out Ukkonen's SuffixTrie constructor algorithm
     *
     * @param f
     * @throws Exception
     */

    public CompressedSuffixTrie (String f) throws Exception {

        // Initialise class attributes
        root = new SuffixTrieNode(null,-1); // O(1)
        String sourceString = fileToString(f);
        if (sourceString == null) return; // early return if the file did not exist or it was empty.

        sourceString += "$";
        sourceArray = sourceString.toCharArray();
        sourceSize = sourceArray.length;

        constructSuffixTrie(sourceString); // O(n2)
        compressSuffixTrie(root); // O(n2)

    }

    /**
     * findString - finding the first occurrence of a pattern s in the DNA sequence.
     * Returns -1 if unsuccessful, or index of first char of queryString in the sourceString.
     *
     * Time Complexity: O(|s|) - where s is the number of characters in the query string
     * 
     * Each char can be visited/assessed at most 1 time, (since it's popped each time). Since each visit takes O(1) time, therefore total time complexity is O(|s|)
     *
     * @param queryString
     * @return
     */
    public int findString (String queryString) {
        int indexOfFirstChar = -1; // O(1)
        int childIndex; // O(1)
        SuffixTrieNode node = this.root; // O(1)
        LinkedList queryList = new LinkedList<>(); // O(1)
        char[] queryArray = queryString.toCharArray(); // O(|s|)

        // Firstly add all characters to a linked list queryList.
        for (char c : queryArray) { // O(|s|)
            queryList.add(c); // O(1)
        }
        // Store the original size of queryList
        int sizeOfQuery = queryList.size();


        try {
            while (!queryList.isEmpty()) {
                // Map the first char in queryList to the childIndex
                childIndex = mapCharToIndex((char) queryList.peekFirst());

                // If it's not a part of the alphabet found, throw exception.
                if (childIndex == -1) {
                    throw new InvalidAttributeValueException("[Error] Query: " + queryString + " has invalid character at " + queryList.peekFirst());
                }

                if (node.getChild(childIndex) == null) {
                    throw new InvalidAttributeValueException("[Error] Query: " + queryString + " was not found at char: " + queryList.peekFirst());
                }

                // Store start and end indices in the sourceArray by checking the compactLabel
                int startIndex = node.getChild(childIndex).compactLabel()[0]; // O(1)
                int endIndex = node.getChild(childIndex).compactLabel()[1]; // O(1)

                // We want to cross-reference each charInQuery with the sourceArray
                for (int i = startIndex; i <= endIndex; i++) { 
                    char charInQuery = (char) queryList.pop(); // O(1)
                    // If the charInQuery, does not match the sourceArray at the index i, then the queryString was not found.
                    if (sourceArray[i] != charInQuery) { // O(1)
                        throw new InvalidAttributeValueException("[Error] Query: " + queryString + " was not found at char: " + charInQuery);
                    }
                    // If we've reached the end of the queryString, save the indexOfFirstChar based on the the current i and original sizeOfQuery
                    if (queryList.isEmpty()) { // O(1)
                        indexOfFirstChar = i - (sizeOfQuery - 1); // O(1)
                        break;
                    }
                }
                // Advance to the next childNode 
                node = node.getChild(childIndex); // O(1) 
            }
//            System.out.println(queryString);
        }
        catch (InvalidAttributeValueException e) {
            System.out.println(e.getMessage());
            return -1;
        }
        return indexOfFirstChar;
    }

    /**
     * similarityAnalyser - compare two strings in f1, f2 and determine similarity with ratio |lcs|/max(|f1|,|f2|).
     *
     * Time Complexity: O(mn) - where m and n are the lengths of string f1 and f2 respectively.
     *
     * @param f1
     * @param f2
     * @param f3
     * @return
     */
    public static float similarityAnalyser(String f1, String f2, String f3) {
        char[] f1Array = fileToString(f1).toCharArray(); // O(1)
        char[] f2Array = fileToString(f2).toCharArray(); // O(1)

        String lcs = longestCommonSubsequence(f1Array, f2Array, f3); // O(mn) - see below

        if (!lcs.equals("")) stringToFile(f3,lcs); // O(|s|) - where s is the number of chars in lcs. It is <= m+n.

        float num = (float) lcs.length(); // O(1)
        float den = (float) Math.max(f1Array.length, f2Array.length); // O(1)
        return num/den;
    }

    /**
     * longestCommonSubsequence - uses dynamic programming to iteratively generate the longest subsequence
     *
     * Time Complexity: O(mn) - where m and n are the lengths of string f1 and f2 respectively.
     *
     * Helper function for similarityAnalyser.
     * @param f1
     * @param f2
     * @param f3
     * @return
     */
    public static String longestCommonSubsequence(char[] f1, char[] f2, String f3) {

        String[][] store = new String[f1.length][f2.length];
        String lcs = "";

        for (int i = 0; i < f1.length; i++) {
            for (int j = 0; j < f2.length; j++) {
                // If the chars at i and j don't save "" in store[i][j].
                if (f1[i] != f2[j]) store[i][j] = "";
                else { // matched at this i,j
                    // If we're on the top row or left col, simply store the char as a string
                    if (i == 0 || j == 0) {
                        store[i][j] = Character.toString(f1[i]);
                    } else { // store the concatenation of current char and string at i-1,j-1 (building a subsequence)
                        store[i][j] = store[i - 1][j - 1] + Character.toString(f1[i]);
                    }
                    // After concatenation/ visiting this i,j, if the length of string is greater than current LCS, re-assign LCS.
                    if (store[i][j].length() >= lcs.length()) lcs = store[i][j];
                }
            }
        }
//        System.out.println(lcs);
        return lcs;
    }

    /** Nested protected class SuffixTrieNode which adapts the Node class for our purposes */
    protected class SuffixTrieNode {

        protected String label;  // label stored at this node, note: internally we include $ within label
        protected int[] compactLabel; // compact version of the label stored at this node
        protected ArrayList<SuffixTrieNode> children = new ArrayList<>(ALPHABET_SIZE+1);  // store pointers to other child nodes inside an ArrayList
        protected int stringIndex;
        protected int numOfChildren;

        /** Main constructor
         *  Time Complexity: O(1)
         */
        protected SuffixTrieNode(String label, int index) {
            setLabel(label); // O(1)
            numOfChildren = 0; // O(1)
            compactLabel = new int[2]; // O(1)
            stringIndex = index; // O(1)
            compactLabel[0] = index; // O(1)
            compactLabel[1] = index; // O(1)

            for (int i = 0; i < ALPHABET_SIZE + 1; i++) { // O(5)
                children.add(i,null);
            }
        }

        /** Returns the label stored at this position */
        protected String label() { return label; }

        /** Sets the label stored at this position */
        protected void setLabel(String o) { label=o; }

        /** Returns the compactLabel stored at this position */
        protected int[] compactLabel() { return compactLabel; }

        /** Sets the second integer of compactLabel stored at this position, used for updating during construction */
        protected void setCompactLabel(int newIndex) { compactLabel[1] = newIndex; }

        /** Returns the children ArrayList at this node */
        protected ArrayList<SuffixTrieNode> getChildren() { return children; }

        /** Returns the child at a given index in children */
        protected SuffixTrieNode getChild(int index) {
            return children.get(index);
        }

        /** Sets the children ArrayList at this position */
        protected void setChildren(ArrayList<SuffixTrieNode> c) { children=c; }

        /** Add individual child node based on index **/
        protected void setChild(int index, SuffixTrieNode child) {
            this.children.set(index, child);
            numOfChildren ++;
        }

        /** Determine if the non-compressed node has a child or not. If it does have a child, return the index of the child */
        protected int locateSingleChildIndex() { // O(1)
            for (int childIndex = 0; childIndex < 5; childIndex ++) { // O(5)
                if (this.getChild(childIndex) != null) {
                    return childIndex;
                }
            }
            return -1;
        }

        /** Used for when the node is printed. Presents nodes in a format that includes both compacted version AND non-compacted version: E.g. AC - (0, 1) */
        public String toString() { return this.label + " - (" + this.compactLabel[0] + ", " + this.compactLabel[1] + ")"; }
    }

    /**
     * fileToString - Reads the file and outputs a string
     *
     * Time Complexity: O(n) - where n is the number of characters in the file
     *
     * @param file
     * @return
     */
    protected static String fileToString(String file) {
        Path path = Paths.get("");
        file = path.toAbsolutePath().toString() + "/" + file + ".txt";
        File f = new File(file);
        String inputString = "";
        char[] next;

        try {
            Scanner input = new Scanner(f);
            while (input.hasNext()) { // O(n) - where n is the number of characters in the file
                next = input.next().toCharArray();  // O(|s|) - where s is the number of characters in each iteration
                // Filter each char to make sure they are inside the alphabet
                for (char c: next) { // O(|s|) - where s is the number of characters in each iteration
                    if (mapCharToIndex(c) >= 0 && mapCharToIndex(c) < 4 ) inputString += Character.toString(c); // O(1)
                }
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

    /**
     * stringToFile - take the string and output it to a file
     *
     * Time Complexity - O(1)
     *
     * @param file
     * @param outputString
     */
    protected static void stringToFile(String file, String outputString) {
        Path path = Paths.get(""); // O(1)
        file = path.toAbsolutePath().toString() + "/" + file + ".txt"; // O(1)
        File f = new File(file);

        try {
            if(f.exists()) throw new FileAlreadyExistsException("");
            PrintWriter outputStream = new PrintWriter(f); // O(1)
            outputStream.println(outputString); // O(1)
            outputStream.close(); // O(1)
        }
        catch (IOException e) {
            System.out.println("[ERROR] File: " + file + " was not created as it already exists in this directory.");
        }
    }

    /** Generate suffixes from inputString */
    /**
     * generateSuffixes - takes the inputString and stores data into suffixStringArray and suffixStartIndexArray
     *
     * Time Complexity: O(n2)
     *
     * @param inputString
     * @param suffixStringArray
     * @param suffixStartIndexArray
     */
    protected void generateSuffixes(String inputString, String[] suffixStringArray, int[] suffixStartIndexArray) {
        int size = inputString.length(); // O(1)

        // As we loop through the inputString, we want to both the actual characters of the substring
        for (int i = 0; i < size; i++) { // O(n2) - number of char in the the source inputString
            suffixStringArray[i] = inputString.substring(i,size); // O(|s|) - s is the number of characters in the suffix, since substring creates a
            suffixStartIndexArray[i] = i; // O(1)
        }
    }

    /**
     * constructSuffixTrie - builds a non-compressed, non-compact suffixTrie
     *
     * Time Complexity: O(n2)
     *
     * 1. Generates suffixes storing labels and starting indices inside suffixStringArray, suffixStartIndexArray
     * 2. Add each suffix to the suffixTrie
     *
     * @param sourceString
     */
    protected void constructSuffixTrie(String sourceString) {

        int[] suffixStartIndexArray = new int[sourceArray.length]; // O(1)
        String[] suffixStringArray = new String[sourceArray.length]; // O(1)

        generateSuffixes(sourceString, suffixStringArray, suffixStartIndexArray); // O(n2)

        // We go through each suffix, and in each suffix we iteratively add each character
        for (int i = 0; i< suffixStringArray.length; i++) { // O(n2) - since the total number of chars  (n * (n+1) / 2)
            addSuffix(suffixStringArray[i], suffixStartIndexArray[i]); // O(|s|) - s is the number of chars in the suffix
        }
    }

    /**
     * addSuffix - adds a suffix to the CompressedSuffixTrie
     *
     * Time Complexity: O(|s|) {where s is the number of characters in the suffix}
     *
     * @param suffix
     * @param suffixStartIndex
     */
    protected void addSuffix(String suffix, int suffixStartIndex) { // O(|s|) - s is the number of characters in suffix
        char[] suffixArray = suffix.toCharArray(); // O(n)
        SuffixTrieNode node = this.root; // O(1)
        int childIndex; // O(1)

        // Loop through all the characters in the suffix one at a time
        for (int stringIndex = 0; stringIndex < suffixArray.length; stringIndex ++) { // O(|s|)
            // Get the index of the char
            childIndex = mapCharToIndex(suffixArray[stringIndex]); // O(1)

            // If the child doesn't exist, we instantiate a new node and we set it as the child
            if (node.getChild(childIndex) == null) { // O(1)

                SuffixTrieNode childNode = new SuffixTrieNode(Character.toString(suffixArray[stringIndex]), suffixStartIndex+stringIndex); // O(1)
                node.setChild(childIndex, childNode); // O(1)
            }
            // Now we move to the child.
            node = node.getChild(childIndex); // O(1)
        }
    }

    /**
     * compressSuffixTrie - performs recursive, depth first search and iteratively updates labels and compactLabels. The suffixTrie becomes compact and compressed.
     *
     * Time Complexity: O(n2) - where n is the total number of characters in the source string. I.e. n * (n+1) / 2
     *
     * @param node
     */
    public void compressSuffixTrie(SuffixTrieNode node) {
        // If the node has only one child, we want to concatenate the node and the childNode's label, and also update the compactLabel.
        if (node.label!= null && node.numOfChildren == 1) { // not at root and we have one child

            SuffixTrieNode childNode;

            // Find the child node which we know exists
            int childIndex = node.locateSingleChildIndex(); // O(1)

            // Select the childNode
            childNode = node.getChild(childIndex); // O(1)

            if (childNode.label().equals("$")) return; // If we're at "$" node, we don't need to go anything

            // Concatenate childNode's label to current node's label
            node.setLabel(node.label() + childNode.label()); // O(1)

            // Update current node's compactLabel, second index to the childNode's index
            node.setCompactLabel(childNode.compactLabel()[0]); // O(1)

            // Update numOfChildren for this node == childNode
            node.numOfChildren = childNode.numOfChildren; // O(1)

            // Update pointer for current node to point at childNode's children
            node.setChildren(childNode.getChildren()); // O(1)

            // Make recursive call for this node again
            compressSuffixTrie(node);
        }
        else { // We are at root or a node with more than one child, we cannot compress/compactify this node any further
            // Hence we need to go into each of this node's children.
            for (int childIndex = 0; childIndex < 5; childIndex++) {
                if (node.getChild(childIndex) != null) {
                    compressSuffixTrie(node.getChild(childIndex));
                }
            }
        }
        return;
    }

    /**
     * mapCharToIndex - map the char c to an index of a char in the Alphabet
     *
     * Time Complexity: O(1)
     *
     * @param c
     * @return
     */
    protected static int mapCharToIndex(char c) { // O(1)
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

    /**
     * printLabels - performs level order traversal to view each node's label and compactLabel. It could be vastly improved.
     *
     * Time Complexity: O(|s|) - where s is the number of nodes in a compressedSuffixTrie
     */
    protected void printLabels() {
        // Do not print if empty
        if (sourceArray == null) {
            System.out.println("[ERROR] This suffixTrie is empty");
            return;
        }

        SuffixTrieNode root = this.root;
        NodeQueue<SuffixTrieNode> store = new NodeQueue<>();
        store.enqueue(root);

        // Only visit each node once
        while (!store.isEmpty()) {
            // Dequeue the node to be visited
            SuffixTrieNode node = store.dequeue(); // O(1)

            // Perform visit by printing
            if (node.label!=null) {
                if (!node.label.equals("$")) System.out.println(node); // O(1)
            }

            // Enqueue all available children
            for (int childIndex = 0; childIndex < 5; childIndex++) {
                if (node.getChild(childIndex) != null) {
                    store.enqueue(node.getChild(childIndex)); // O(1)
                }
            }
        }
    }

    public static void main(String args[]) throws Exception{

    /** Construct a trie named trie1 */
//        CompressedSuffixTrie trie1 = new CompressedSuffixTrie("file1");
//
//        System.out.println("ACTTCGTAAG is at: " + trie1.findString("ACTTCGTAAG")); // 5
//
//        System.out.println("AAAACAACTTCG is at: " + trie1.findString("AAAACAACTTCG")); // 18
//
//        System.out.println("ACTTCGTAAGGTT : " + trie1.findString("ACTTCGTAAGGTT")); // -1
//
//        System.out.println(CompressedSuffixTrie.similarityAnalyser("file2", "file3", "file4")); // Solution: 0.12048193
//
//        CompressedSuffixTrie trie2 = new CompressedSuffixTrie("file5");
//        trie2.printLabels();
//
//        System.out.println("AC is at: " + trie2.findString("AC")); // 0
//        System.out.println("ACCGTAC is at: " + trie2.findString("ACCGTAC")); // 0
//        System.out.println("ACCGTT is at: " + trie2.findString("ACCGTT")); // 0
//        System.out.println("B is at: " + trie2.findString("B")); // -1
//        System.out.println("ACC is at: " + trie2.findString("ACC")); // 0
//        System.out.println("TAC is at: " + trie2.findString("TAC")); // 4
//        System.out.println("TA is at: " + trie2.findString("TA")); // 4
//        System.out.println("CAT is at: " + trie2.findString("CAT")); // -1

        CompressedSuffixTrie trie3 = new CompressedSuffixTrie("file6");
        System.out.println(CompressedSuffixTrie.similarityAnalyser("file7", "file8", "file9")); // Solution: 0.23809524


    }

}

