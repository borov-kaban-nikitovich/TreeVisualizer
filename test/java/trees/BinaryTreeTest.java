package trees;

import org.junit.Test;
import trees.BinaryTree.BinaryNode;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class BinaryTreeTest {

    public static class BinaryNodeTest {

        BinaryNode node;

        @Test
        public void getValue() {
            int value = 1337;

            node = new BinaryNode(value);
            assertEquals(value, node.getValue());
        }

        @Test
        public void hasLeft() {
            node = new BinaryNode(4321);
            assertFalse(node.hasLeft());
            node.bind(1234); // goes left
            assertTrue(node.hasLeft());
        }

        @Test
        public void removeLeft() {
            node = new BinaryNode(777);
            node.bind(666); // goes left
            assertTrue(node.hasLeft());
            node.removeLeft();
            assertFalse(node.hasLeft());
        }

        @Test
        public void getLeft() {
            node = new BinaryNode(1702);
            node.bind(988); // goes left
            assertEquals(988, node.getLeft().getValue());
        }

        @Test
        public void hasRight() {
            node = new BinaryNode(4);
            assertFalse(node.hasRight());
            node.bind(400); // goes right
            assertTrue(node.hasRight());
        }

        @Test
        public void removeRight() {
            node = new BinaryNode(0);
            node.bind(1); // goes right
            assertTrue(node.hasRight());
            node.removeRight();
            assertFalse(node.hasRight());
        }

        @Test
        public void getRight() {
            node = new BinaryNode(7);
            node.bind(1094949); // goes right
            assertEquals(1094949, node.getRight().getValue());
        }

        @Test
        public void bindSmaller() {
            node = new BinaryNode(1707);
            node.bind(42); // goes left
            assertTrue(node.hasLeft());
            assertEquals(42, node.getLeft().getValue());
        }

        @Test
        public void bindBigger() {
            node = new BinaryNode(-911);
            node.bind(0); // goes right
            assertTrue(node.hasRight());
            assertEquals(0, node.getRight().getValue());
        }

        @Test
        public void getLayer() {
            node = new BinaryNode(1000);
            assertEquals(0, node.getLayer());

            node.bind(1002); // goes right after 1000
            assertEquals(1, node.getRight().getLayer());

            node.bind(1001); // goes left after 1002
            assertEquals(2, node.getRight().getLeft().getLayer());
        }

    }

    BinaryTree tree;

    @Test
    public void getRoot() {
        tree = new BinaryTree();
        assertNull(tree.getRoot());

        tree.put(0);
        assertEquals(0, tree.getRoot().getValue());
    }

    @Test
    public void put() {
        tree = new BinaryTree();

        tree.put(100);
        assertEquals(100, tree.getRoot().getValue());

        tree.put(-100);
        assertEquals(-100, tree.getRoot().getLeft().getValue());

        tree.put(-50);
        assertEquals(-50, tree.getRoot().getLeft().getRight().getValue());
    }

    @Test
    public void getPathTo() {
        tree = new BinaryTree();
        tree.put(5);  // root
        tree.put(10); // goes right after 5
        tree.put(7);  // goes left  after 10
        tree.put(6);  // goes left  after 7

        List<BinaryNode> expectedPath = new ArrayList<>();
        expectedPath.add(tree.getRoot());
        expectedPath.add(tree.getRoot().getRight());
        expectedPath.add(tree.getRoot().getRight().getLeft());
        expectedPath.add(tree.getRoot().getRight().getLeft().getLeft());

        List<BinaryNode> actualPath = tree.getPathTo(6);

        assertEquals(expectedPath, actualPath);
    }

    @Test
    public void remove() {
        tree = new BinaryTree();
        tree.put(5);  // root
        tree.put(10); // goes right after 5
        tree.put(7);  // goes left  after 10
        tree.put(6);  // goes left  after 6

        tree.remove(7);

        assertNull(tree.getRoot().getRight().getLeft());
    }

    @Test
    public void clear() {
        tree = new BinaryTree();
        tree.put(10); // root
        tree.put(5);  // goes left  after 10
        tree.put(15); // goes right after 10
        tree.put(3);  // goes left  after 5
        tree.put(8);  // goes right after 5
        tree.put(12); // goes left  after 15
        tree.put(17); // goes right after 15

        tree.clear();

        assertNull(tree.getRoot());
    }

    @Test
    public void isEmpty() {
        tree = new BinaryTree();
        assertTrue(tree.isEmpty());

        tree.put(100);
        tree.clear();
        assertTrue(tree.isEmpty());
    }

}
