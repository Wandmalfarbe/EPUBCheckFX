package de.pascalwagler.epubcheckfx.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    @FXML
    private MainContentController mainContentController;

    @FXML
    private AnchorPane mainContentAnchorPane;
    @FXML
    private AnchorPane dragAndDropAnchorPane;
    @FXML
    private AnchorPane loadingPane;

    @FXML
    private VBox dropArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainContentAnchorPane.setVisible(false);
        loadingPane.setVisible(false);
        dragAndDropAnchorPane.setVisible(true);

        mainContentController.setMainWindowController(this);
    }

    @FXML
    private void onDragOver(DragEvent event) {
        event.acceptTransferModes(TransferMode.ANY);
        event.consume();
    }

    @FXML
    private void onDragEntered(DragEvent event) {
        dropArea.getStyleClass().add("active");
        event.consume();
    }

    @FXML
    private void onDragExited(DragEvent event) {
        dropArea.getStyleClass().remove("active");
        event.consume();
    }

    @FXML
    private void onDragDropped(DragEvent event) {

        Dragboard db = event.getDragboard();
        event.setDropCompleted(true);
        event.consume();

        if (db.hasFiles()) {
            mainContentAnchorPane.setVisible(false);
            dragAndDropAnchorPane.setVisible(false);
            loadingPane.setVisible(true);
            mainContentController.runEpubCheck(db.getFiles().get(0));
        }
    }

    @FXML
    public void chooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"));
        File file = fileChooser.showOpenDialog(this.dragAndDropAnchorPane.getScene().getWindow());

        if (file != null) {
            mainContentAnchorPane.setVisible(false);
            dragAndDropAnchorPane.setVisible(false);
            loadingPane.setVisible(true);
            mainContentController.runEpubCheck(file);
        }
    }

    public void validationDone() {
        mainContentAnchorPane.setVisible(true);
        dragAndDropAnchorPane.setVisible(false);
        loadingPane.setVisible(false);
    }

    public void cancelValidation() {
        mainContentAnchorPane.setVisible(true);
        dragAndDropAnchorPane.setVisible(false);
        loadingPane.setVisible(false);
    }
}
