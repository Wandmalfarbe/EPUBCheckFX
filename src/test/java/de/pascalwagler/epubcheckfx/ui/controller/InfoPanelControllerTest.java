package de.pascalwagler.epubcheckfx.ui.controller;

import com.adobe.epubcheck.util.FeatureEnum;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.hamcrest.core.StringEndsWith;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.LabeledMatchers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

@ExtendWith(ApplicationExtension.class)
class InfoPanelControllerTest {

    private Button button;

    GridPane pane;
    InfoPanelController controller;

    @Start
    public void start(Stage stage) throws IOException {
        ResourceBundle bundle = ResourceBundle.getBundle("i18n.LangBundle", Locale.getDefault());
        FXMLLoader loader = new FXMLLoader(InfoPanelControllerTest.class.getResource("/fxml/InfoPanel.fxml"), bundle);
        this.pane = loader.load();
        this.controller = loader.getController();
        stage.setScene(new Scene(pane));
        stage.show();
        stage.toFront();
    }

    @Test
    void valuesShouldBeEmpty() {
        FxAssert.verifyThat("#file", LabeledMatchers.hasText(""));
        FxAssert.verifyThat("#title", LabeledMatchers.hasText(""));
        FxAssert.verifyThat("#creator", LabeledMatchers.hasText(""));
    }

    @Test
    void valuesFileShouldBeSet() {
        // Arrange
        Map<String, List<Pair<FeatureEnum, String>>> infoMap = new HashMap<>();
        File file = new File("some/file.txt");

        // Act
        Platform.runLater(() -> controller.updateFromInfoList(infoMap, file));

        // Assert
        FxAssert.verifyThat("#file", LabeledMatchers.hasText(StringEndsWith.endsWith("some/file.txt")));
    }
}
