package treevisualizer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.VPos;
import javafx.scene.control.Alert;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import trees.BinaryTree.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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
    private Pane binaryCanvasPane;
    @FXML
    private Canvas binaryCanvas;
    @FXML
    public TextField binaryElementToAdd;
    @FXML
    public TextField binaryElementToFind;

    @FXML
    private Pane trieCanvasPane;
    @FXML
    private Canvas trieCanvas;

    private GraphicsContext binaryGC;
    private GraphicsContext trieGC;
    private final int size = 40; // Определяет свободное пространство между выводимыми элементами деревьев.

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        final int treeDepth = 5; // Максимальное число отображающихся слоёв дерева. Влияет на высоту холста.
        binaryCanvasPane.setPrefHeight(size * treeDepth);
        binaryGC = binaryCanvas.getGraphicsContext2D();
        binaryGC.setFont(new Font(14));
        binaryGC.setTextAlign(TextAlignment.CENTER);
        binaryGC.setTextBaseline(VPos.CENTER);

        trieCanvasPane.setPrefHeight(size * treeDepth);
        trieGC = trieCanvas.getGraphicsContext2D();
        trieGC.setFont(new Font(14));
        trieGC.setTextAlign(TextAlignment.CENTER);
        trieGC.setTextBaseline(VPos.CENTER);
    }

    public void binaryAddElement() {
        String inputString = binaryElementToAdd.getText();
        Pattern intPattern = Pattern.compile("-?\\d+");
        Matcher intMatcher = intPattern.matcher(inputString);
        if (!intMatcher.matches())
            new Alert(Alert.AlertType.ERROR, String.format("\"%s\" is not an integer!", inputString)).show();
        else {
            int number = Integer.parseInt(inputString);
            binaryTree.put(number);
            System.out.printf("Number %s is added.\n", number);
            paintBinaryTree();
        }
        binaryElementToAdd.clear();
    }

    private void paintBinaryTree() {
        paintBinaryTree(null);
    }

    private void paintBinaryTree(BinaryNode selected) {
        binaryGC.clearRect(0, 0, binaryCanvas.getWidth(), binaryCanvas.getHeight());
        BinaryNode rootNode = binaryTree.getRoot();
        paintBinaryNode(rootNode, 0, 0, selected);
    }

    private void paintBinaryNode(BinaryNode node, int layer, int order, BinaryNode selected) {
        int newLayer = layer + 1;
        int newOrderLeft = order * 2;
        int newOrderRight = order * 2 + 2;

        // Нарисуем соединительные линии
        if (node.hasLeft())
            paintLineToNewBinaryNode(layer, order, newLayer, newOrderLeft);
        if (node.hasRight())
            paintLineToNewBinaryNode(layer, order, newLayer, newOrderRight);

        // Нарисуем кружочек
        if (node == selected)
            binaryGC.setFill(Color.CORAL);
        else
            binaryGC.setFill(Color.PAPAYAWHIP);
        binaryGC.fillOval(
                binaryCanvas.getWidth() / Math.pow(2, layer + 1) * (order + 1) - size / 2.0,
                size * layer,
                size,
                size
        );
        binaryGC.strokeOval(
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
            paintBinaryNode(node.getLeft(), newLayer, newOrderLeft, selected);
        if (node.hasRight())
            paintBinaryNode(node.getRight(), newLayer, newOrderRight, selected);
    }

    private void paintLineToNewBinaryNode(int layer, int order, int newLayer, int newOrder) {
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
        if (binaryTree.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "There's no element in the binary tree.").show();
            binaryElementToFind.clear();
            return;
        }
        String inputString = binaryElementToFind.getText();
        Pattern intPattern = Pattern.compile("-?\\d+");
        Matcher intMatcher = intPattern.matcher(inputString);
        if (!intMatcher.matches()) {
            new Alert(Alert.AlertType.ERROR, String.format("\"%s\" is not an integer!", inputString)).show();
            binaryElementToFind.clear();
            return;
        }
        int number = Integer.parseInt(inputString);
        System.out.printf("Searching for element %s...\n", number);

        BinaryNode node = binaryTree.getRoot();

        Duration timeBetweenFrames = Duration.millis(500);
        Timeline timeline = new Timeline();
        int i = 0;

        while (true) {
            BinaryNode finalNode1 = node;
            KeyFrame frame = new KeyFrame(timeBetweenFrames.multiply(i++), e -> paintBinaryTree(finalNode1));
            timeline.getKeyFrames().add(frame);

            if (number < node.getValue() && node.hasLeft()) {
                node = node.getLeft();
            }
            else if (number > node.getValue() && node.hasRight()) {
                node = node.getRight();
            }
            else if (number == node.getValue()) {
                frame = new KeyFrame(timeBetweenFrames.multiply(i), e ->
                        new Alert(Alert.AlertType.INFORMATION, String.format("Element %s was found!", number)).show()
                );
                timeline.getKeyFrames().add(frame);
                break;
            }
            else {
                frame = new KeyFrame(timeBetweenFrames.multiply(i), e ->
                        new Alert(Alert.AlertType.INFORMATION, "There's no such element.").show()
                );
                timeline.getKeyFrames().add(frame);
                break;
            }
        }
        timeline.play();
    }

    private void paintTrie() {

    }
}