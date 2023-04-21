module com.example.treevisualisator {
    requires javafx.controls;
    requires javafx.fxml;
    requires annotations;

    exports treevisualizer;
    opens treevisualizer to javafx.fxml;
}