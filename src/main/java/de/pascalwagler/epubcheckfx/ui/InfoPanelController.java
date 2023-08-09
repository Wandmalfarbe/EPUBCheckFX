package de.pascalwagler.epubcheckfx.ui;

import atlantafx.base.theme.Styles;
import com.adobe.epubcheck.util.FeatureEnum;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.Pair;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class InfoPanelController implements Initializable {

    @FXML
    Label file;
    @FXML
    Label title;
    @FXML
    Label creator;

    private ResourceBundle resourceBundle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resourceBundle = resources;
    }

    public void updateFromInfoList(Map<String, List<Pair<FeatureEnum, String>>> infoMap, File epubFile) {
        file.setText(epubFile.getAbsolutePath());

        if (!infoMap.containsKey("general")) {
            System.out.println("No general info found.");
            setTitle(null);
            setCreator(null);
            return;
        }

        List<Pair<FeatureEnum, String>> generalInfoList = infoMap.get("general");

        String title = generalInfoList.stream()
                .filter(pair -> pair.getKey().equals(FeatureEnum.DC_TITLE))
                .findFirst()
                .map(Pair::getValue)
                .orElse(null);
        setTitle(title);

        List<String> creators = generalInfoList.stream()
                .filter(pair -> pair.getKey().equals(FeatureEnum.DC_CREATOR))
                .map(Pair::getValue)
                .collect(Collectors.toList());
        setCreator(String.join(", ", creators));
    }

    private void setTitle(String titleString) {
        if (titleString == null) {
            title.setText(resourceBundle.getString("info_panel.no_title"));
            title.getStyleClass().add(Styles.TEXT_SUBTLE);
            return;
        }
        title.getStyleClass().remove(Styles.TEXT_SUBTLE);
        title.setText(titleString);
    }

    private void setCreator(String titleString) {
        if (titleString == null) {
            creator.setText(resourceBundle.getString("info_panel.no_creator"));
            creator.getStyleClass().add(Styles.TEXT_SUBTLE);
            return;
        }
        creator.getStyleClass().remove(Styles.TEXT_SUBTLE);
        creator.setText(titleString);
    }
}
