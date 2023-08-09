package de.pascalwagler.epubcheckfx.ui;

import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.Locale;
import java.util.ResourceBundle;

@Slf4j
public class App extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        log.info("Java Version: " + System.getProperty("java.version"));
        log.info("JavaFX Version: " + System.getProperty("javafx.version"));

        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());

        ResourceBundle bundle = ResourceBundle.getBundle("i18n.LangBundle", Locale.getDefault());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainWindow.fxml"), bundle);
        Parent root = loader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add("/css/style.css");

        stage.getIcons().add(new Image("/img/icons/icon_16x16.png"));
        stage.getIcons().add(new Image("/img/icons/icon_20x20.png"));
        stage.getIcons().add(new Image("/img/icons/icon_32x32.png"));
        stage.getIcons().add(new Image("/img/icons/icon_40x40.png"));
        stage.getIcons().add(new Image("/img/icons/icon_44x44.png"));
        stage.getIcons().add(new Image("/img/icons/icon_64x64.png"));
        stage.getIcons().add(new Image("/img/icons/icon_128x128.png"));
        stage.getIcons().add(new Image("/img/icons/icon_256x256.png"));
        stage.getIcons().add(new Image("/img/icons/icon_512x512.png"));

        if (Taskbar.isTaskbarSupported()) {
            var taskbar = Taskbar.getTaskbar();
            if (taskbar.isSupported(Taskbar.Feature.ICON_IMAGE)) {
                final Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
                java.awt.Image dockIcon = defaultToolkit.getImage(getClass().getResource("/img/icons/icon_256x256.png"));
                taskbar.setIconImage(dockIcon);
            }
        }

        stage.setTitle(bundle.getString("app.name"));
        stage.setScene(scene);
        stage.show();
    }
}
