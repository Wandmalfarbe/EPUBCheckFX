package de.pascalwagler.epubcheckfx.ui;

import com.adobe.epubcheck.util.FeatureEnum;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.Pair;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class InfoPanelController implements Initializable {

    @FXML
    Label file;
    @FXML
    Label title;
    @FXML
    Label author;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void updateFromInfoList(Map<String, List<Pair<FeatureEnum, String>>> infoMap, File epubFile) {

        if (!infoMap.containsKey("general")) {
            System.out.println("No general info found.");
            return;
        }

        List<Pair<FeatureEnum, String>> generalInfoList = infoMap.get("general");

        file.setText(epubFile.getAbsolutePath());

        Optional<Pair<FeatureEnum, String>> dcTitle = generalInfoList.stream()
                .filter(pair -> pair.getKey().equals(FeatureEnum.DC_TITLE))
                .findFirst();
        if (dcTitle.isPresent()) {
            title.setText(dcTitle.get().getValue());
        }

        Optional<Pair<FeatureEnum, String>> dcCreator = generalInfoList.stream()
                .filter(pair -> pair.getKey().equals(FeatureEnum.DC_CREATOR))
                .findFirst();
        if (dcCreator.isPresent()) {
            author.setText(dcCreator.get().getValue());
        }
    }

    public void fill(String file, String title, String author) {
        this.file.setText(file);
        this.title.setText(title);
        this.author.setText(author);
    }
}
