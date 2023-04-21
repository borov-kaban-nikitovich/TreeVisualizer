package trees;

import java.util.HashMap;
import java.util.Map;

public class Trie {
    private class Node {
        private char value;
        private final Map<Character, Node> boundNodes = new HashMap<>();

        public Node() {
        }

        public Node(char value) {
            this.value = value;
        }

        private void bind(char value) {
            boundNodes.put(value, new Node(value));
        }

        public void bind(String s) {
            char firstChar = s.charAt(0);
            if (s.length() == 1) {
                bind(firstChar);
            } else {
                bind(firstChar);
                boundNodes.get(firstChar).bind(s.substring(1));
            }
        }

        public boolean hasChild(char c) {
            return boundNodes.containsKey(c);
        }

        public Node getChild(char c) {
            return boundNodes.get(c);
        }

        public char getValue() {
            return value;
        }
    }

    private Node root = new Node();

    public void put(String s) {
        root.bind(s);
    }

    public boolean find(String inputString) {
        Node node = root;
        for (char c : inputString.toCharArray()) {
            if (node.hasChild(c))
                node = node.getChild(c);
            else
                return false;
        }
        return true;
    }

    public void clear() {
        root = new Node();
    }
}
