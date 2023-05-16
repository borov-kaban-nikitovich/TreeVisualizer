package trees;

public class BinaryTree {
    public class BinaryNode {
        private final int value;
        private BinaryNode left;
        private BinaryNode right;

        public BinaryNode(int value) {
            this.value = value;
        }

        public void bind(BinaryNode node) {
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

        public BinaryNode getLeft() {
            return left;
        }

        public boolean hasLeft() {
            return left != null;
        }

        public BinaryNode getRight() {
            return right;
        }

        public boolean hasRight() {
            return right != null;
        }
    }

    private BinaryNode root;

    public void put(int number) {
        if (root == null)
            root = new BinaryNode(number);
        else
            root.bind(new BinaryNode(number));
    }

//    public boolean find(int number) {
//        BinaryNode node = root;
//        if (node == null)
//            return false;
//        while (node.getValue() != number) {
//            if (number < node.getValue()) {
//                if (node.hasLeft()) node = node.getLeft();
//                else break;
//            } else {
//                if (node.hasRight()) node = node.getRight();
//                else break;
//            }
//        }
//        return node.getValue() == number;
//    }

    public void clear() {
        root = null;
    }

    public BinaryNode getRoot() {
        return root;
    }

    public boolean isEmpty() {
        return root == null;
    }
}
