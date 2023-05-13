module com.example.treevisualizer {
    requires javafx.controls;
    requires javafx.fxml;

    exports treevisualizer;
    opens treevisualizer to javafx.fxml;
}