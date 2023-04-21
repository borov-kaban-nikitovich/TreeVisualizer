package trees;

public class BinaryTree {
    private class Node {
        private final int value;
        Node left;
        Node right;

        public Node(int value) {
            this.value = value;
        }

        public void bind(Node node) {
            if (node.value < this.value) {
                if (this.left == null)
                    this.left = node;
                else
                    this.left.bind(node);
            }
            else if (node.value > this.value) {
                if (this.right == null)
                    this.right = node;
                else
                    this.right.bind(node);
            }
        }
    }

    private Node root;

    public void add(int number) {
        if (root == null)
            root = new Node(number);
        else
            root.bind(new Node(number));
    }

    public void clear() {
        root = null;
    }
}
