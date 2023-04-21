package trees;

public class Main {
    public static void main(String[] args) {
        var tree = new BinaryTree();
        tree.put(3);
        tree.put(2);
        tree.put(4);
        tree.put(6);
        tree.put(5);
        System.out.println(tree.find(0));
    }
}
