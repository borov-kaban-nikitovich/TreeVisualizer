package treevisualizer;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private static Canvas img;

    private GraphicsContext gc;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        img = new Canvas(300, 300);
        gc = img.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, img.getWidth(), img.getHeight());
    }
}