package trees;

import java.util.ArrayList;
import java.util.List;

public class BinaryTree {
    public static class BinaryNode {
        private final int value;
        private BinaryNode left;
        private BinaryNode right;
        private final int layer;

        public BinaryNode(int value, int layer) {
            this.value = value;
            this.layer = layer;
        }

        public void bind(int number) {
            if (number < this.value) {
                if (this.left == null)
                    this.left = new BinaryNode(number, this.layer + 1);
                else
                    this.left.bind(number);
            } else if (number > this.value) {
                if (this.right == null)
                    this.right = new BinaryNode(number, this.layer + 1);
                else
                    this.right.bind(number);
            }
        }

        public int getValue() {
            return value;
        }

        public int getLayer() {
            return layer;
        }

        public BinaryNode getLeft() {
            return left;
        }

        public void setLeft(BinaryNode left) {
            this.left = left;
        }

        public boolean hasLeft() {
            return left != null;
        }

        public BinaryNode getRight() {
            return right;
        }

        public void setRight(BinaryNode right) {
            this.right = right;
        }

        public boolean hasRight() {
            return right != null;
        }
    }

    private BinaryNode root;

    public void put(int number) {
        if (root == null)
            root = new BinaryNode(number, 0);
        else
            root.bind(number);
    }

    public List<BinaryNode> getPathTo(int number) {
        BinaryNode node = root;
        List<BinaryNode> path = new ArrayList<>();
        if (node == null)
            return path;
        path.add(root);

        while (number != node.getValue()) {
            if (number < node.getValue()) {
                if (node.hasLeft()) {
                    node = node.getLeft();
                    path.add(node);
                } else break;
            } else if (number > node.getValue()) {
                if (node.hasRight()) {
                    node = node.getRight();
                    path.add(node);
                } else break;
            }
        }
        return path;
    }

    public void remove(int number) {
        if (root == null)
            return;
        if (root.getValue() == number) {
            root = null;
            return;
        }
        BinaryNode node = root;
        while (true) {
            if (number > node.getValue()) {
                if (node.hasRight()) {
                    if (node.getRight().getValue() == number) {
                        node.setRight(null);
                        return;
                    } else node = node.getRight();
                } else return;
            }

            else if (number < node.getValue()) {
                if (node.hasLeft()) {
                    if (node.getLeft().getValue() == number) {
                        node.setLeft(null);
                        return;
                    } else node = node.getLeft();
                } else return;
            }
        }
    }

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
