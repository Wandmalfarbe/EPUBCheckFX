package de.pascalwagler.epubcheckfx.ui.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

@Slf4j
public class AboutPanelController implements Initializable {

    @FXML
    TextArea licenseTextArea;

    @FXML
    VBox thirdPartyLicensePane;

    private ResourceBundle resourceBundle;

    @Data
    private static class License {
        private final String name;
        private final String licenseFile;
    }

    private static final License[] licenses = {
            new License("AtlantaFX", "atlantafx"),
            new License("Apache Commons CSV", "commons-csv"),
            new License("EPUBCheck", "epubcheck"),
            new License("Ikonli", "ikonli"),
            new License("jSystemThemeDetector", "jsystemthemedetector"),
            new License("Logback", "logback"),
            new License("Mustache.java", "mustache-java"),
            new License("OpenJFX", "openjfx"),
            new License("Project Lombok", "project-lombok")
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resourceBundle = resources;
        try {
            this.licenseTextArea.setText(getFileContents("/license.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            initThirdPartyLicenses();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initThirdPartyLicenses() throws IOException {
        for (License license : licenses) {
            String licenseText = getFileContents("/licenses/" + license.getLicenseFile() + ".txt");
            TextArea textArea = new TextArea(licenseText);
            textArea.getStyleClass().add("license");
            TitledPane titledPane = new TitledPane(license.getName(), textArea);
            titledPane.getStyleClass().add("dense");
            titledPane.setExpanded(false);
            thirdPartyLicensePane.getChildren().add(titledPane);
        }
    }

    private String getFileContents(String path) throws IOException {
        URL resource = AboutPanelController.class.getResource(path);
        if (resource == null) {
            log.error("Could not open file at '{}'.", path);
            return null;
        }
        InputStreamReader inputStreamReader = new InputStreamReader(resource.openStream());
        Scanner s = new Scanner(inputStreamReader).useDelimiter("\\A");
        return s.hasNext() ? s.next() : null;
    }
}
