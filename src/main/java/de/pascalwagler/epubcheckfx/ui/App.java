package de.pascalwagler.epubcheckfx.ui;

import atlantafx.base.theme.CupertinoDark;
import atlantafx.base.theme.CupertinoLight;
import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.awt.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class App extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    public static final Locale locale = Locale.GERMAN;

    @Override
    public void start(Stage stage) throws Exception {

        Locale.setDefault(locale);

        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");

        System.out.println("Java Version: " + javaVersion);
        System.out.println("JavaFX Version: " + javafxVersion);

        Application.setUserAgentStylesheet(new CupertinoLight().getUserAgentStylesheet());

        ResourceBundle bundle = ResourceBundle.getBundle("i18n.LangBundle", locale);
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