package de.pascalwagler.epubcheckfx.ui.controller;

import de.pascalwagler.epubcheckfx.App;
import de.pascalwagler.epubcheckfx.model.Theme;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

@Slf4j
public class MenuPanelController implements Initializable {

    @FXML
    private Menu menuViewTheme;

    private ResourceBundle resourceBundle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resourceBundle = resources;

        // Don't use native menu bar on macOS because there are no icons (might be a JavaFX Bug?).
        /*final String os = System.getProperty("os.name");
        if (os != null && os.startsWith("Mac")) {
            menuBar.useSystemMenuBarProperty().set(true);
            menuBar.setVisible(false);
            menuBar.setManaged(false);
        }*/

        for (Theme theme : Theme.values()) {
            CheckMenuItem menuItem = new CheckMenuItem(resourceBundle.getString(theme.getI18nKey()));
            menuItem.setId(theme.name());
            menuItem.setOnAction(this::selectTheme);
            menuViewTheme.getItems().add(menuItem);
        }
        String theme = App.userPreferences.get(App.PREFERENCES_THEME, Theme.PRIMER.name());
        Optional<CheckMenuItem> selectedMenuItem = menuViewTheme.getItems().stream()
                .map(CheckMenuItem.class::cast)
                .filter(item -> item.getId().equals(Theme.valueOf(theme).name()))
                .findFirst();
        selectedMenuItem.ifPresent(checkMenuItem -> checkMenuItem.setSelected(true));
    }

    private void selectTheme(ActionEvent actionEvent) {
        // Unselect all other MenuItems
        menuViewTheme.getItems().forEach(item -> {
            CheckMenuItem checkMenuItem = (CheckMenuItem) item;
            checkMenuItem.setSelected(false);
        });
        // Select current
        CheckMenuItem source = ((CheckMenuItem) actionEvent.getSource());
        source.setSelected(true);
        App.setTheme(Theme.valueOf(source.getId()));
    }

    public void openDocumentation() {
        openWebpage(resourceBundle.getString("url.documentation"));
    }

    public void openReportABug() {
        openWebpage(resourceBundle.getString("url.report_a_bug"));
    }

    private static void openWebpage(String url) {
        try {
            Desktop.getDesktop().browse(new URL(url).toURI());
        } catch (IOException | URISyntaxException exception) {
            log.error("Could not open URL '{}'.", url, exception);
        }
    }

    public void openAboutWindow() {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("i18n.LangBundle", Locale.getDefault());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LicencePanel.fxml"), bundle);
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle(resourceBundle.getString("about.title"));
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            log.error("Could not open about window.", e);
        }
    }
}
