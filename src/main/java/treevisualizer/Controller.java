package treevisualizer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import trees.BinaryTree;
import trees.BinaryTree.BinaryNode;
import trees.Trie;
import trees.Trie.TrieNode;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private final BinaryTree binaryTree = new BinaryTree();
    private final Trie trie = new Trie();

    @FXML
    private Canvas binaryCanvas;
    @FXML
    public TextField binaryElementToAdd;
    @FXML
    public TextField binaryElementToFind;
    @FXML
    public TextField binaryElementToRemove;

    @FXML
    private Canvas trieCanvas;
    @FXML
    public TextField trieElementToAdd;
    @FXML
    public TextField trieElementToFind;
    @FXML
    public TextField trieElementToRemove;

    private GraphicsContext binaryGC;
    private GraphicsContext trieGC;

    private final int circleSize = 40;
    private final int spaceBetween = 45;
    private final int binaryDepth = 7; // Максимальное число отображающихся слоёв бинарного дерева
    private final int trieDepth = 13 + 1; // Максимальная длина строки + место для корня
    private final Color mainColor = Color.WHITE;
    private final Color selectionColor = Color.PAPAYAWHIP;
    private final Color textColor = Color.BLACK;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        binaryGC = binaryCanvas.getGraphicsContext2D();
        binaryGC.setFont(new Font(14));
        binaryGC.setTextAlign(TextAlignment.CENTER);
        binaryGC.setTextBaseline(VPos.CENTER);
        binaryGC.setStroke(textColor);

        trieGC = trieCanvas.getGraphicsContext2D();
        trieGC.setFont(new Font(14));
        trieGC.setTextAlign(TextAlignment.CENTER);
        trieGC.setTextBaseline(VPos.CENTER);
        trieGC.setStroke(textColor);
        paintTrie();
    }


    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ BINARY TREE ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

    public void binaryAddElement() {
        String inputString = binaryElementToAdd.getText();
        binaryElementToAdd.clear();

        int number;
        try {
            number = Integer.parseInt(inputString);
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, String.format("\"%s\" не является integer!", inputString)).show();
            return;
        }
        binaryTree.put(number);
        paintBinaryTree();
    }

    // Paints a circle by its center coordinates
    private void binaryPaintCircle(double x, double y, boolean selected) {
        if (selected) binaryGC.setFill(selectionColor);
        else binaryGC.setFill(mainColor);
        binaryGC.fillOval(x - circleSize * 0.5, y - circleSize * 0.5, circleSize, circleSize);
        binaryGC.strokeOval(x - circleSize * 0.5, y - circleSize * 0.5, circleSize, circleSize);
    }

    private void paintBinaryTree() {
        paintBinaryTree(null);
    }

    private void paintBinaryTree(BinaryNode selected) {
        binaryGC.clearRect(0, 0, binaryCanvas.getWidth(), binaryCanvas.getHeight());
        paintBinaryNode(binaryTree.getRoot(), 0, selected);
    }

    private void paintBinaryNode(BinaryNode node, int order, BinaryNode selected) {
        int layer = node.getLayer();

        if (layer > binaryDepth)
            return;

        int newOrderLeft = order * 2;
        int newOrderRight = order * 2 + 2;

        // Рисуем линии
        if (node.hasLeft())
            paintLineToNewBinaryNode(layer, order, newOrderLeft);
        if (node.hasRight())
            paintLineToNewBinaryNode(layer, order, newOrderRight);

        // Рисуем кружочек
        binaryPaintCircle(
                binaryCanvas.getWidth() / Math.pow(2, layer + 1) * (order + 1),
                spaceBetween * (layer + 0.5),
                node == selected
        );

        // Выводим числа
        binaryGC.setFill(textColor);
        binaryGC.fillText(
                Integer.toString(node.getValue()),
                binaryCanvas.getWidth() / Math.pow(2, layer + 1) * (order + 1),
                spaceBetween * (0.5 + layer)
        );

        // Выводим детей
        if (node.hasLeft())
            paintBinaryNode(node.getLeft(), newOrderLeft, selected);
        if (node.hasRight())
            paintBinaryNode(node.getRight(), newOrderRight, selected);
    }

    private void paintLineToNewBinaryNode(int layer, int order, int newOrder) {
        int newLayer = layer + 1;
        binaryGC.strokeLine(
                binaryCanvas.getWidth() / Math.pow(2, layer + 1) * (order + 1),
                spaceBetween * layer + spaceBetween / 2.0,
                binaryCanvas.getWidth() / Math.pow(2, newLayer + 1) * (newOrder + 1),
                spaceBetween * newLayer + spaceBetween / 2.0
        );
    }


    public void binaryFindElement() {
        // Проверка на возможность поиска
        if (binaryTree.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "В двоичном дереве нет чисел... Здесь нечего искать...").show();
            binaryElementToFind.clear();
            return;
        }

        String inputString = binaryElementToFind.getText();
        binaryElementToFind.clear();

        int number;
        try {
            number = Integer.parseInt(inputString);
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, String.format("\"%s\" не является integer!", inputString)).show();
            return;
        }

        // Получим путь к узлу
        List<BinaryNode> path = binaryTree.getPathTo(number);

        // Составим таймлайн с анимацией поиска
        Timeline timeline = new Timeline();
        Duration pauseTime = Duration.millis(500);
        int frameOrder = 0;

        for (BinaryNode node : path) {
            KeyFrame frame = new KeyFrame(pauseTime.multiply(frameOrder++), event -> paintBinaryTree(node));
            timeline.getKeyFrames().add(frame);
        }

        if (path.get(path.size() - 1).getValue() != number) {
            timeline.getKeyFrames().addAll(
                    new KeyFrame(pauseTime.multiply(frameOrder), event -> paintBinaryTree()),
                    new KeyFrame(pauseTime.multiply(frameOrder), event ->
                            new Alert(
                                    Alert.AlertType.INFORMATION,
                                    String.format("Увы, числа %d здесь нет :(", number)
                            ).show()
                    )
            );
        }
        else
            timeline.getKeyFrames().add(
                    new KeyFrame(pauseTime.multiply(frameOrder), event ->
                            new Alert(
                                    Alert.AlertType.INFORMATION,
                                    String.format("Число %d успешно найдено! :)", number)
                            ).show()
                    )
            );

        // Проиграем таймлайн
        timeline.play();
    }

    public void binaryRemoveElement() {
        String inputString = binaryElementToRemove.getText();
        binaryElementToRemove.clear();

        int number;
        try {
            number = Integer.parseInt(inputString);
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, String.format("\"%s\" не является integer!", inputString)).show();
            return;
        }
        binaryTree.remove(number);
        paintBinaryTree();
    }

    public void binaryClear() {
        binaryTree.clear();
        binaryGC.clearRect(0, 0, binaryCanvas.getWidth(), binaryCanvas.getHeight());
    }


    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ TRIE ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

    public void trieAddElement() {
        String string = trieElementToAdd.getText();
        trieElementToAdd.clear();

        if (string.length() > trieDepth - 1)
            new Alert(
                    Alert.AlertType.ERROR,
                    String.format("Строка \"%s\" слишком длинная. Введите строку до %d символов длиной.", string, trieDepth - 1)
            ).show();
        else {
            trie.put(string);
            paintTrie();
        }
    }

    // Paints a circle by its center coordinates
    private void triePaintCircle(double x, double y, boolean selected) {
        if (selected) trieGC.setFill(selectionColor);
        else trieGC.setFill(mainColor);
        trieGC.fillOval(x - circleSize * 0.5, y - circleSize * 0.5, circleSize, circleSize);
        trieGC.strokeOval(x - circleSize * 0.5, y - circleSize * 0.5, circleSize, circleSize);
    }

    private final List<Integer> trieNodesAlreadyPainted = new ArrayList<>();

    private void paintTrie() {
        paintTrie(new ArrayList<>());
    }

    private void paintTrie(List<TrieNode> selected) {
        trieGC.clearRect(0, 0, trieCanvas.getWidth(), trieCanvas.getHeight());
        for (int i = 0; i < trieDepth; i++)
            trieNodesAlreadyPainted.add(0);
        paintTrieNode(trie.getRoot(), selected);
        trieNodesAlreadyPainted.clear();
    }

    private void paintTrieNode(TrieNode node, List<TrieNode> selected) {
        int layer = node.getLayer();

        if (layer >= trieDepth)
            return;

        int order = trieNodesAlreadyPainted.get(layer) + 1; // Какой по счёту элемент выводим
        trieNodesAlreadyPainted.set(layer, order);

        int parts = trie.getLayerWidth(node.getLayer()) + 1; // На сколько равных частей делим экран

        // Нарисуем линии от this до его детей
        if (layer + 1 < trieDepth) {
            int childOrder = trieNodesAlreadyPainted.get(layer + 1) + 1;
            for (TrieNode child : node.getChildren())
                trieGC.strokeLine(
                        trieCanvas.getWidth() / parts * order,
                        spaceBetween * (0.5 + layer),
                        trieCanvas.getWidth() / (trie.getLayerWidth(child.getLayer()) + 1) * childOrder++,
                        spaceBetween * (1.5 + layer)
                );
        }

        // Нарисуем кружочек
        triePaintCircle(trieCanvas.getWidth() / parts * order, spaceBetween * (layer + 0.5), selected.contains(node));

        // Выведем значение
        trieGC.setFill(textColor);
        trieGC.fillText(
                "" + node.getValue(),
                trieCanvas.getWidth() / parts * order,
                spaceBetween * (0.5 + layer)
        );

        // Выведем детей
        for (TrieNode child : node.getChildren())
            paintTrieNode(child, selected);
    }


    public void trieFindElement() {
        String string = trieElementToFind.getText();
        trieElementToFind.clear();

        // Получим путь к узлу
        List<TrieNode> path = trie.getPathTo(string);

        // Составим таймлайн с анимацией поиска
        Timeline timeline = new Timeline();
        Duration pauseTime = Duration.millis(500);
        int frameOrder = 0;
        List<TrieNode>[] toSelect = new ArrayList[path.size()];

        for (int i = 0; i < path.size(); i++) {
            toSelect[i] = new ArrayList<>();
            for (int j = 0; j <= i; j++) {
                toSelect[i].add(path.get(j));
            }
        }

        for (int i = 0; i < path.size(); i++) {
            int finalI = i;
            KeyFrame frame = new KeyFrame(pauseTime.multiply(frameOrder++), event -> paintTrie(toSelect[finalI]));
            timeline.getKeyFrames().add(frame);
        }
        if (path.size() != string.length() + 1) {
        timeline.getKeyFrames().addAll(
                new KeyFrame(pauseTime.multiply(frameOrder), event -> paintTrie()),
                new KeyFrame(pauseTime.multiply(frameOrder), event ->
                        new Alert(
                                Alert.AlertType.INFORMATION,
                                String.format("Строка \"%s\" не найдена :(", string)
                        ).show()
                )
        );
    }
        else
            timeline.getKeyFrames().add(
                    new KeyFrame(pauseTime.multiply(frameOrder), event ->
                            new Alert(
                                    Alert.AlertType.INFORMATION,
                                    String.format("Строка \"%s\" успешно найдена! :)", string)
                            ).show()
                    )
            );
        // Проиграем таймлайн
        timeline.play();
    }

    public void trieRemoveElement() {
        String string = trieElementToRemove.getText();
        trieElementToRemove.clear();

        trie.remove(string);
        paintTrie();
    }

    public void trieClear() {
        trie.clear();
        paintTrie();
    }
}