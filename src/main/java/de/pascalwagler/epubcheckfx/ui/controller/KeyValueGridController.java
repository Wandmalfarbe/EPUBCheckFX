package de.pascalwagler.epubcheckfx.ui.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class KeyValueGridController implements Initializable {

    @FXML
    private GridPane keyValueGrid;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        keyValueGrid.addRow(0, new Text("Hallo"));
        keyValueGrid.addRow(1, new Text("Welt"));
    }
}
