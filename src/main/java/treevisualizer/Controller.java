package treevisualizer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Font;
import trees.BinaryTree.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import trees.BinaryTree;
import trees.Trie;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller implements Initializable {
    private final BinaryTree binaryTree = new BinaryTree();
    private final Trie trie = new Trie();
    @FXML
    public TextField binaryElementToAdd;
    @FXML
    private Canvas binaryCanvas;
    @FXML
    private Canvas trieCanvas;
    @FXML
    private ScrollPane binaryCanvasPane;
    @FXML
    private Pane trieCanvasPane;
    private GraphicsContext binaryGC;
    private GraphicsContext trieGC;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        binaryGC = binaryCanvas.getGraphicsContext2D();
        binaryGC.setFont(new Font(20));
        trieGC = trieCanvas.getGraphicsContext2D();
    }

    public void binaryAddElement(ActionEvent actionEvent) {
        String inputString = binaryElementToAdd.getText();
        Pattern intPattern = Pattern.compile("-?\\d{1,}");
        Matcher intMatcher = intPattern.matcher(inputString);
        if (!intMatcher.matches()) {

            return;
        }
        int number = Integer.parseInt(inputString);
        binaryTree.put(number);
        System.out.printf("Number %s is added!\n", number);
        paintBinaryTree();
    }


    private void paintBinaryTree() {
        binaryGC.clearRect(0, 0, binaryCanvas.getWidth(), binaryCanvas.getHeight());
        int size = 35; // Размер кружочков в пикселях
        Node rootNode = binaryTree.getRoot();
        List<Node> nodes = new ArrayList<>();
        nodes.add(rootNode);
        int layer = 1;
        while (layer < 20) {
            int amount = nodes.size();
            for (int i = 0; i < amount; i++)
                binaryGC.strokeText(
                        Integer.toString(nodes.get(i).getValue()),
                        binaryCanvas.getWidth() / (Math.pow(2.0, layer) + 1) * (i + 1),
                        size * (layer - 1) + size / 2.0
                );
            List<Node> newNodes = new ArrayList<>();
            for (Node node : nodes) {
                if (node.hasLeft())
                    newNodes.add(node.getLeft());
                if (node.hasRight())
                    newNodes.add(node.getRight());
            }
            nodes = newNodes;
            layer++;
        }
    }

    private void paintTrie() {

    }
}