package de.pascalwagler.epubcheckfx;

import com.jthemedetecor.OsThemeDetector;
import de.pascalwagler.epubcheckfx.model.Theme;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.awt.Taskbar;
import java.awt.Toolkit;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

@Slf4j
public class App extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    private static final OsThemeDetector detector = OsThemeDetector.getDetector();
    private static Theme currentTheme;

    public static final Preferences userPreferences = Preferences.userNodeForPackage(App.class);

    private static final String VERSION = "1.1.0";

    public static final String PREFERENCES_VERSION = "version";
    public static final String PREFERENCES_SEVERITY = "severity";
    public static final String PREFERENCES_EXPORT_FORMAT = "export_format";
    public static final String PREFERENCES_EPUB_PROFILE = "epub_profile";
    public static final String PREFERENCES_THEME = "theme";
    public static final String PREFERENCES_VIEW = "view";

    @Override
    public void start(Stage stage) throws Exception {

        log.info("Java Version: " + System.getProperty("java.version"));
        log.info("JavaFX Version: " + System.getProperty("javafx.version"));

        clearPreferencesWhenChangedVersion();

        ResourceBundle bundle = ResourceBundle.getBundle("i18n.LangBundle", Locale.getDefault());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainWindow.fxml"), bundle);
        Parent root = loader.load();
        Scene scene = new Scene(root);

        initStyles(scene);
        applyIcon(stage);

        stage.setTitle(bundle.getString("app.name"));
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Just delete all the preferences when a new (or old) version is run for the first time.
     * There are no preferences migrations.
     */
    private static void clearPreferencesWhenChangedVersion() throws BackingStoreException {
        String version = userPreferences.get(PREFERENCES_VERSION, VERSION);
        if (!version.equals(VERSION)) {
            userPreferences.clear();
        }
        userPreferences.put(PREFERENCES_VERSION, VERSION);
    }

    private void applyIcon(Stage stage) {

        // app icons
        stage.getIcons().add(new Image("/img/icons/icon_16x16.png"));
        stage.getIcons().add(new Image("/img/icons/icon_20x20.png"));
        stage.getIcons().add(new Image("/img/icons/icon_32x32.png"));
        stage.getIcons().add(new Image("/img/icons/icon_40x40.png"));
        stage.getIcons().add(new Image("/img/icons/icon_44x44.png"));
        stage.getIcons().add(new Image("/img/icons/icon_64x64.png"));
        stage.getIcons().add(new Image("/img/icons/icon_128x128.png"));
        stage.getIcons().add(new Image("/img/icons/icon_256x256.png"));
        stage.getIcons().add(new Image("/img/icons/icon_512x512.png"));

        // taskbar icons
        if (Taskbar.isTaskbarSupported()) {
            var taskbar = Taskbar.getTaskbar();
            if (taskbar.isSupported(Taskbar.Feature.ICON_IMAGE)) {
                final Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
                java.awt.Image dockIcon = defaultToolkit.getImage(getClass().getResource("/img/icons/icon_256x256.png"));
                taskbar.setIconImage(dockIcon);
            }
        }
    }

    private void initStyles(Scene scene) {

        String selectedThemeFromPreferences = App.userPreferences.get(PREFERENCES_THEME, Theme.PRIMER.name());
        setTheme(Theme.valueOf(selectedThemeFromPreferences));
        detector.registerListener(isDark -> Platform.runLater(() -> setTheme(currentTheme)));

        scene.getStylesheets().add("/css/style.css");
    }

    public static void setTheme(Theme theme) {
        App.currentTheme = theme;
        App.userPreferences.put(PREFERENCES_THEME, theme.name());

        // The theme detection code currently logs a NullPointerException
        // (see https://github.com/Dansoftowner/jSystemThemeDetector/issues/18).
        if (detector.isDark()) {
            Application.setUserAgentStylesheet(App.currentTheme.getAtlantaFxDarkTheme().getUserAgentStylesheet());
        } else {
            Application.setUserAgentStylesheet(App.currentTheme.getAtlantaFxLightTheme().getUserAgentStylesheet());
        }
    }
}
