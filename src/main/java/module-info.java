module com.example.treevisualisator {
    requires javafx.controls;
    requires javafx.fxml;

    exports treevisualizer;
    opens treevisualizer to javafx.fxml;
}