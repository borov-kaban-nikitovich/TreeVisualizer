package trees;

import java.util.HashMap;
import java.util.Map;

public class Trie {
    private class Node {
        private char key;
        private int value;
        private Map<Character, Node> bindedNodes = new HashMap<>();

        public Node() {}
        public Node(char key) {
            this.key = key;
        }
        public Node(char key, int value) {
            this.key = key;
            this.value = value;
        }

        private void bind(char key) {
            bindedNodes.put(key, new Node(key));
        }
        private void bind(char key, int value) {
            bindedNodes.put(key, new Node(key, value));
        }
        public void bind(String s, int value) {
            if (s.length() == 0) {
                this.value = value;
            } else {
                char firstChar = s.charAt(0);
                if (s.length() == 1) {
                    bind(firstChar, value);
                } else {
                    bind(firstChar);
                    bindedNodes.get(firstChar).bind(s.substring(1), value);
                }
            }
        }
    }

    private Node root = new Node();

    public void put(String s, int value) {
        root.bind(s, value);
    }

    public void clear() {
        root = new Node();
    }
}
