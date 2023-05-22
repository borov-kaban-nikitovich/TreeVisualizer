package trees;


import java.util.*;

public class Trie {
    public static class TrieNode {
        private final Character value;
        private final Map<Character, TrieNode> boundNodes = new HashMap<>();
        private final int layer;
        private final TrieNode parent;

        public TrieNode() {
            this.value = '\u0000';
            this.layer = 0;
            parent = null;
        }

        public TrieNode(char value, TrieNode parent) {
            this.value = value;
            this.layer = parent.layer + 1;
            this.parent = parent;
        }

        public void bind(String s) {
            if (s.isEmpty())
                return;
            char firstChar = s.charAt(0);
            if (!boundNodes.containsKey(firstChar))
                boundNodes.put(firstChar, new TrieNode(firstChar, this));
            if (s.length() > 1)
                boundNodes.get(firstChar).bind(s.substring(1));
        }

        public TrieNode getParent() {
            return parent;
        }

        public char getValue() {
            return value;
        }

        public boolean hasChild(char c) {
            return boundNodes.containsKey(c);
        }

        public TrieNode getChild(char c) {
            return boundNodes.get(c);
        }

        public void removeChild(char c) {
            boundNodes.remove(c);
        }

        public HashSet<TrieNode> getChildren() {
            return new HashSet<>(boundNodes.values());
        }

        public int getLayer() {
            return layer;
        }
    }

    private TrieNode root = new TrieNode();

    public void put(String s) {
        root.bind(s);
    }

    public List<TrieNode> getPathTo(String inputString) {
        TrieNode node = root;
        List<TrieNode> path = new ArrayList<>();
        path.add(root);
        for (char c : inputString.toCharArray()) {
            if (node.hasChild(c)) {
                node = node.boundNodes.get(c);
                path.add(node);
            } else break;
        }
        return path;
    }

    public void remove(String string) {
        TrieNode node = root;

        // Дойдём до конца и проверим, существует ли такая строка и не является ли она подстрокой
        for (char c : string.toCharArray()) {
            if (node.hasChild(c)) {
                node = node.getChild(c);
            } else return;
        }
        if (node.getChildren().size() != 0)
            return;

        // Пойдём в начало и отыщем момент, где можно обрезать цепочку символов
        node = node.getParent();
        for (int i = string.length() - 2; i >= 0; i--) {
            if (node == null)
                return;
            if (node.getChildren().size() > 1) {
                node.removeChild(string.charAt(i + 1));
                return;
            }
            node = node.getParent();
        }

        // Если ничего не найдено
        root.removeChild(string.charAt(0));
    }

    public void clear() {
        root = new TrieNode();
    }

    public TrieNode getRoot() {
        return root;
    }

    /**
     * How many nodes there are at the same layer
     **/
    public int getLayerWidth(int layer) {
        return getLayerWidth(root, layer);
    }

    private int getLayerWidth(TrieNode currentNode, int layer) {
        if (currentNode.getLayer() == layer) return 1;
        int sum = 0;
        for (TrieNode node : currentNode.getChildren())
            sum += getLayerWidth(node, layer);
        return sum;
    }

}
