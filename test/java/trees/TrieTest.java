package trees;

import org.junit.Test;
import trees.Trie.TrieNode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class TrieTest {

    public static class TrieNodeTest {

        TrieNode node;

        @Test
        public void getValue() {
            node = new TrieNode();
            assertEquals(0, node.getValue());

            node.bind("a");
            assertEquals('a', node.getChild('a').getValue());
        }

        @Test
        public void hasChild() {
            node = new TrieNode();
            assertFalse(node.hasChild('щ'));

            node.bind("щи");
            node.bind("ща");
            assertTrue(node.hasChild('щ'));
            assertTrue(node.getChild('щ').hasChild('и'));
            assertTrue(node.getChild('щ').hasChild('а'));
        }

        @Test
        public void getChild() {
            node = new TrieNode();
            assertNull(node.getChild('_'));

            node.bind("щи");
            node.bind("ща");
            assertTrue(node.hasChild('щ'));
            assertTrue(node.getChild('щ').hasChild('и'));
            assertTrue(node.getChild('щ').hasChild('а'));
        }

        @Test
        public void removeChild() {
            node = new TrieNode();
            node.bind("qwerty");
            node.bind("queue");

            node.getChild('q').getChild('w').removeChild('e');
            assertFalse(node.getChild('q').getChild('w').hasChild('e'));

            node.removeChild('q');
            assertFalse(node.hasChild('h'));
        }

        @Test
        public void getChildren() {
            node = new TrieNode();
            node.bind("а");
            node.bind("б");
            node.bind("ц");
            node.bind("д");

            Set<TrieNode> expectedChildren = new HashSet<>();
            for (char c : "абцд".toCharArray())
                expectedChildren.add(node.getChild(c));

            Set<TrieNode> actualChildren = node.getChildren();

            assertEquals(expectedChildren, actualChildren);
        }

        @Test
        public void getParent() {
            node = new TrieNode();
            node.bind("hi");
            assertEquals(
                    node,
                    node.getChild('h').getParent()
            );
            assertEquals(
                    node.getChild('h'),
                    node.getChild('h').getChild('i').getParent()
            );
        }

        @Test
        public void bind() {
            node = new TrieNode();
            node.bind("ab");
            node.bind("acb");

            assertEquals(0, node.getValue());
            assertTrue(node.hasChild('a'));
            assertTrue(node.getChild('a').hasChild('b'));
            assertTrue(node.getChild('a').hasChild('c'));
        }

        @Test
        public void getLayer() {
            node = new TrieNode();
            node.bind("аб");
            assertEquals(0, node.getLayer());
            assertEquals(1, node.getChild('а').getLayer());
            assertEquals(2, node.getChild('а').getChild('б').getLayer());
        }

    }

    Trie tree;

    @Test
    public void getRoot() {
        tree = new Trie();
        assertEquals(0, tree.getRoot().getValue());
    }

    @Test
    public void put() {
        tree = new Trie();
        tree.put("ab");
        tree.put("ac");
        assertTrue(tree.getRoot().hasChild('a'));
        assertTrue(tree.getRoot().getChild('a').hasChild('b'));
        assertTrue(tree.getRoot().getChild('a').hasChild('c'));
    }

    @Test
    public void getPathTo() {
        tree = new Trie();
        tree.put("hello");
        tree.put("helmet");
        tree.put("hero");
        tree.put("world");

        List<TrieNode> expectedPath = new ArrayList<>();
        expectedPath.add(tree.getRoot());
        TrieNode n = tree.getRoot();
        for (char c : "helmet".toCharArray()) {
            n = n.getChild(c);
            expectedPath.add(n);
        }

        List<TrieNode> actualPath = tree.getPathTo("helmet");

        assertEquals(expectedPath, actualPath);
    }

    @Test
    public void remove() {
        tree = new Trie();
        tree.put("кот");
        tree.put("комната");
        tree.put("коробка");

        tree.remove("кот");
        assertFalse(tree.getRoot().getChild('к').getChild('о').hasChild('т'));

        tree.remove("ком"); // слово "ком" всё равно останется в дереве, потому что это подстрока
        assertTrue(tree.getRoot().getChild('к').getChild('о').hasChild('м'));
    }

    @Test
    public void clear() {
        tree = new Trie();

        tree.clear();
        assertEquals(0, tree.getRoot().getValue());

        tree.put("колобок");
        tree.put("повесился");
        tree.put("буратино");
        tree.put("утонул");
        tree.put("сессия");
        tree.put("закрыта");
        tree.clear();
        assertEquals(0, tree.getRoot().getValue());
    }

    @Test
    public void getLayerWidth() {
        tree = new Trie();
        tree.put("аа");
        tree.put("аб");
        tree.put("ав");
        tree.put("адд");
        tree.put("айй");

        assertEquals(1, tree.getLayerWidth(0));
        assertEquals(1, tree.getLayerWidth(1));
        assertEquals(5, tree.getLayerWidth(2));
        assertEquals(2, tree.getLayerWidth(3));
    }

}
