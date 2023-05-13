package treevisualizer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.VPos;
import javafx.scene.control.Alert;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import trees.BinaryTree.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import trees.BinaryTree;
import trees.Trie;

import java.net.URL;
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
    private final int size = 40; // Определяет свободное пространство между выводимыми элементами деревьев

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        binaryGC = binaryCanvas.getGraphicsContext2D();
        binaryGC.setFont(new Font(14));
        binaryGC.setTextAlign(TextAlignment.CENTER);
        binaryGC.setTextBaseline(VPos.CENTER);

        trieGC = trieCanvas.getGraphicsContext2D();
        trieGC.setFont(new Font(14));
        trieGC.setTextAlign(TextAlignment.CENTER);
        trieGC.setTextBaseline(VPos.CENTER);
    }

    public void binaryAddElement() {
        String inputString = binaryElementToAdd.getText();
        Pattern intPattern = Pattern.compile("-?\\d{1,}");
        Matcher intMatcher = intPattern.matcher(inputString);
        if (!intMatcher.matches()) {
            new Alert(Alert.AlertType.ERROR, String.format("\"%s\" is not an integer!", inputString)).show();
            binaryElementToAdd.setText("");
            return;
        }
        int number = Integer.parseInt(inputString);
        binaryTree.put(number);
        System.out.printf("Number %s is added.\n", number);
        paintBinaryTree();
    }

    private void paintBinaryTree() {
        binaryGC.clearRect(0, 0, binaryCanvas.getWidth(), binaryCanvas.getHeight());
        Node rootNode = binaryTree.getRoot();
        paintBinaryNode(rootNode, 0, 0);
    }

    private void paintBinaryNode(Node node, int layer, int order) {
        int newLayer = layer + 1;
        int newOrderLeft = order * 2;
        int newOrderRight = order * 2 + 2;

        if (node.hasLeft())
            paintLineToNewNode(layer, order, newLayer, newOrderLeft);
        if (node.hasRight())
            paintLineToNewNode(layer, order, newLayer, newOrderRight);

        binaryGC.setFill(Color.PAPAYAWHIP);
        binaryGC.fillOval(
                binaryCanvas.getWidth() / Math.pow(2, layer + 1) * (order + 1) - size / 2.0,
                size * layer,
                size,
                size
        );
        binaryGC.setFill(Color.BLACK);
        binaryGC.fillText(
                Integer.toString(node.getValue()),
                binaryCanvas.getWidth() / Math.pow(2, layer + 1) * (order + 1),
                size * layer + size / 2.0
        );

        if (node.hasLeft())
            paintBinaryNode(node.getLeft(), newLayer, newOrderLeft);
        if (node.hasRight())
            paintBinaryNode(node.getRight(), newLayer, newOrderRight);
    }

    private void paintLineToNewNode(int layer, int order, int newLayer, int newOrder) {
        binaryGC.strokeLine(
                binaryCanvas.getWidth() / Math.pow(2, layer + 1) * (order + 1),
                size * layer + size / 2.0,
                binaryCanvas.getWidth() / Math.pow(2, newLayer + 1) * (newOrder + 1),
                size * newLayer + size / 2.0
        );
    }

    public void binaryClear() {
        binaryTree.clear();
        binaryGC.clearRect(0, 0, binaryCanvas.getWidth(), binaryCanvas.getHeight());
        System.out.println("Binary tree is empty now.");
    }

    public void binaryFindElement() {

    }

    private void paintTrie() {

    }
}