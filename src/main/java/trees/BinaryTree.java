package trees;

public class BinaryTree {
    private class Node {
        private final int value;
        private Node left;
        private Node right;

        public Node(int value) {
            this.value = value;
        }

        public void bind(Node node) {
            if (node.value < this.value) {
                if (this.left == null)
                    this.left = node;
                else
                    this.left.bind(node);
            } else if (node.value > this.value) {
                if (this.right == null)
                    this.right = node;
                else
                    this.right.bind(node);
            }
        }

        public int getValue() {
            return value;
        }

        public Node getLeft() {
            return left;
        }

        public boolean hasLeft() {
            return left != null;
        }

        public Node getRight() {
            return right;
        }

        public boolean hasRight() {
            return right != null;
        }
    }

    private Node root;

    public void put(int number) {
        if (root == null)
            root = new Node(number);
        else
            root.bind(new Node(number));
    }

    public boolean find(int number) {
        Node node = root;
        if (node == null)
            return false;
        while (node.getValue() != number) {
//            System.out.println(node.getValue());
            if (number < node.getValue()) {
                if (node.hasLeft()) node = node.getLeft();
                else break;
            } else {
                if (node.hasRight()) node = node.getRight();
                else break;
            }
        }
        return node.getValue() == number;
    }

    public void clear() {
        root = null;
    }
}
