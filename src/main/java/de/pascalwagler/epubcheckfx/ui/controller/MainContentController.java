package de.pascalwagler.epubcheckfx.ui.controller;

import com.adobe.epubcheck.api.EpubCheck;
import com.adobe.epubcheck.util.Archive;
import com.adobe.epubcheck.util.FeatureEnum;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import de.pascalwagler.epubcheckfx.model.CheckMessage;
import de.pascalwagler.epubcheckfx.model.EpubProfile;
import de.pascalwagler.epubcheckfx.model.ExportFormat;
import de.pascalwagler.epubcheckfx.model.InfoMessage;
import de.pascalwagler.epubcheckfx.model.Severity;
import de.pascalwagler.epubcheckfx.service.CustomReport;
import de.pascalwagler.epubcheckfx.ui.SeverityListCell;
import de.pascalwagler.epubcheckfx.ui.SeverityTableCell;
import de.pascalwagler.epubcheckfx.ui.TranslatableListCell;
import de.pascalwagler.epubcheckfx.ui.UiHelper;
import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Pair;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

@Slf4j
public class MainContentController implements Initializable {

    @FXML
    private TableView<CheckMessage> validationResultTable;
    @FXML
    private TableColumn<CheckMessage, String> messageId;
    @FXML
    private TableColumn<CheckMessage, Severity> severity;
    @FXML
    private TableColumn<CheckMessage, String> message;
    @FXML
    private TableColumn<CheckMessage, String> path;
    @FXML
    private TableColumn<CheckMessage, Integer> line;
    @FXML
    private TableColumn<CheckMessage, Integer> column;

    @FXML
    private Label epubcheckVersion;

    @FXML
    private ComboBox<EpubProfile> epubProfile;

    @FXML
    private TextField resultTableSearchFilter;
    @FXML
    private ComboBox<de.pascalwagler.epubcheckfx.model.Severity> resultTableSeverityFilter;

    @FXML
    private ComboBox<ExportFormat> exportFormat;

    private final CustomReport customReport = new CustomReport();

    private ResourceBundle resourceBundle;

    private File epubFile;

    @FXML
    private ScrollPane scrollMetadata;
    @FXML
    private ScrollPane scrollInfo;

    @FXML
    @Setter
    private MainWindowController mainWindowController;

    @FXML
    @Setter
    private InfoPanelController infoPanelController;

    @FXML
    @Setter
    private SummaryPanelController summaryPanelController;

    private FilteredList<CheckMessage> filteredErrorList;

    private static final File tempDirectory = new File(System.getProperty("java.io.tmpdir"), "EPUBCheckFX");

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;

        try {
            if (!tempDirectory.exists()) {
                Files.createDirectory(tempDirectory.toPath());
            }
        } catch (IOException e) {
            log.error("Unexpected Exception when creating the temporary directory.");
            throw new RuntimeException(e);
        }
        initValidationResultTable();
        initSeverityFilter();
        initExportFormat();
        initEpubProfile();
        epubcheckVersion.setText("EPUBCheck Version " + EpubCheck.version() + " - (Built " + EpubCheck.buildDate() + ")");
    }

    private Predicate<CheckMessage> tableFilterPredicate(String search, Severity severity) {
        String searchLower = search.toLowerCase();
        return checkMessage -> {

            boolean matchesLine = checkMessage.getLine() != null && checkMessage.getLine().toString().contains(searchLower);
            boolean matchesColumn = checkMessage.getColumn() != null && checkMessage.getColumn().toString().contains(searchLower);

            boolean matchesSearchString =
                    search.isEmpty() // Special case: The empty search matches always.
                            || checkMessage.getMessageId().toString().toLowerCase().contains(searchLower)
                            || checkMessage.getMessage().toLowerCase().contains(searchLower)
                            || checkMessage.getSeverity().toString().contains(searchLower)
                            || checkMessage.getPath().contains(searchLower)
                            || matchesLine
                            || matchesColumn;
            return matchesSearchString && isGreaterOrEqualSeverity(checkMessage.getSeverity(), severity);
        };
    }

    private boolean isGreaterOrEqualSeverity(Severity a, Severity b) {
        return a.ordinal() >= b.ordinal();
    }

    public void clearFilter() {
        resultTableSearchFilter.setText("");
        resultTableSeverityFilter.getSelectionModel().select(de.pascalwagler.epubcheckfx.model.Severity.INFO);
    }

    private void initValidationResultTable() {

        resultTableSearchFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            log.info("search changed from " + oldValue + " to " + newValue);
            if (Objects.equals(oldValue, newValue)) {
                return;
            }
            filteredErrorList.setPredicate(tableFilterPredicate(newValue, resultTableSeverityFilter.getValue()));
        });

        resultTableSeverityFilter.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            log.info("severity changed from " + oldValue + " to " + newValue);
            if (Objects.equals(oldValue, newValue)) {
                return;
            }
            filteredErrorList.setPredicate(tableFilterPredicate(resultTableSearchFilter.getText(), newValue));
        });

        filteredErrorList = customReport.errorList.filtered(checkMessage -> true);
        Property<ObservableList<CheckMessage>> authorListProperty = new SimpleObjectProperty<>(filteredErrorList);
        validationResultTable.itemsProperty().bind(authorListProperty);

        summaryPanelController.setFilteredErrorList(filteredErrorList);

        messageId.setCellValueFactory(new PropertyValueFactory<>("messageId"));
        severity.setCellValueFactory(new PropertyValueFactory<>("severity"));
        severity.setCellFactory(tableColumn -> new SeverityTableCell(resourceBundle));
        message.setCellValueFactory(new PropertyValueFactory<>("message"));
        message.setCellFactory(this::createWrappingTableCell);
        path.setCellValueFactory(new PropertyValueFactory<>("path"));
        line.setCellValueFactory(new PropertyValueFactory<>("line"));
        column.setCellValueFactory(new PropertyValueFactory<>("column"));
    }

    private TableCell<CheckMessage, String> createWrappingTableCell(TableColumn<CheckMessage, String> tableColumn) {
        TableCell<CheckMessage, String> cell = new TableCell<>();
        Text text = new Text();
        cell.setGraphic(text);
        cell.setPadding(new Insets(8, 10, 5, 10));
        text.wrappingWidthProperty().bind(tableColumn.widthProperty().subtract(20));
        text.textProperty().bind(cell.itemProperty());
        return cell;
    }

    private void initSeverityFilter() {
        resultTableSeverityFilter.setCellFactory(listView -> new TranslatableListCell<>(resourceBundle));
        resultTableSeverityFilter.getItems().addAll(
                de.pascalwagler.epubcheckfx.model.Severity.SUPPRESSED,
                de.pascalwagler.epubcheckfx.model.Severity.USAGE,
                de.pascalwagler.epubcheckfx.model.Severity.INFO,
                de.pascalwagler.epubcheckfx.model.Severity.WARNING,
                de.pascalwagler.epubcheckfx.model.Severity.ERROR,
                de.pascalwagler.epubcheckfx.model.Severity.FATAL
        );
        resultTableSeverityFilter.setButtonCell(resultTableSeverityFilter.getCellFactory().call(null));
        resultTableSeverityFilter.setCellFactory(cell -> new SeverityListCell(resourceBundle));
        resultTableSeverityFilter.getSelectionModel().select(de.pascalwagler.epubcheckfx.model.Severity.INFO);
    }

    private void initExportFormat() {
        exportFormat.setCellFactory(listView -> new TranslatableListCell<>(resourceBundle));
        exportFormat.getItems().addAll(
                ExportFormat.HTML,
                ExportFormat.MARKDOWN,
                ExportFormat.MARKDOWN_LIST,
                ExportFormat.PLAINTEXT,
                ExportFormat.EPUB_CHECKER_TXT);
        exportFormat.setButtonCell(exportFormat.getCellFactory().call(null));
        exportFormat.getSelectionModel().selectFirst();
    }

    private void initEpubProfile() {
        epubProfile.setCellFactory(listView -> new TranslatableListCell<>(resourceBundle));
        epubProfile.getItems().addAll(
                EpubProfile.DEFAULT,
                EpubProfile.DICT,
                EpubProfile.EDUPUB,
                EpubProfile.IDX,
                EpubProfile.PREVIEW);
        epubProfile.setButtonCell(epubProfile.getCellFactory().call(null));
        epubProfile.getSelectionModel().selectFirst();
    }

    public void runEpubCheck(File file) {
        this.epubFile = file;
        runEpubCheck();
    }

    @FXML
    public void chooseFile() {
        mainWindowController.chooseFile();
    }

    @FXML
    public void chooseFolder() {
        mainWindowController.chooseFolder();
    }

    @FXML
    private void runEpubCheck() {
        customReport.errorList.clear();
        customReport.infoList.clear();

        CompletableFuture.supplyAsync(() -> {
            try {
                log.debug("Start Validation");

                File epubToValidate;
                if (epubFile.isDirectory()) {
                    epubToValidate = createTempEpubFromFolder();
                } else {
                    epubToValidate = epubFile;
                }
                EpubCheck epubcheck = new EpubCheck(epubToValidate, customReport, epubProfile.getValue().getEpubProfile());
                epubcheck.check();

                Map<String, List<Pair<FeatureEnum, String>>> infoMap = toMap();
                Node metadataPane = UiHelper.createMetadataPane(infoMap, resourceBundle);
                Node infoPane = UiHelper.createInfoPane(infoMap, resourceBundle);

                Platform.runLater(() -> {
                    scrollMetadata.setContent(metadataPane);
                    scrollInfo.setContent(infoPane);
                    infoPanelController.updateFromInfoList(infoMap, epubFile);
                });
            } catch (Exception exception) {
                log.error("An unexpected error occurred during the validation.", exception);
            } finally {
                log.debug("End Validation");
                mainWindowController.validationDone();
            }
            return null;
        });
    }

    private File createTempEpubFromFolder() throws IOException {
        Archive epub = new Archive(epubFile.getPath(), true);
        File temporaryEpubFile = new File(tempDirectory, epub.getEpubName());

        if (temporaryEpubFile.exists()) {
            Files.delete(temporaryEpubFile.toPath());
        }

        epub.createArchive(temporaryEpubFile);
        return temporaryEpubFile;
    }

    private Map<String, List<Pair<FeatureEnum, String>>> toMap() {
        Map<String, List<Pair<FeatureEnum, String>>> infos = new HashMap<>();

        for (InfoMessage item : customReport.infoList) {

            String resource2 = item.getResource() == null ? "general" : item.getResource();

            if (infos.containsKey(resource2)) {
                List<Pair<FeatureEnum, String>> list = infos.get(resource2);
                list.add(new Pair<>(item.getFeature(), item.getValue()));
            } else {
                List<Pair<FeatureEnum, String>> list = new ArrayList<>();
                list.add(new Pair<>(item.getFeature(), item.getValue()));
                infos.put(resource2, list);
            }
        }
        return infos;
    }

    public void export() throws IOException {

        ExportFormat selectedExportFormat = exportFormat.getValue();

        MustacheFactory mf = new DefaultMustacheFactory();
        InputStreamReader inputStreamReader = new InputStreamReader(getClass().getResource("/mustache/" + selectedExportFormat.getName() + ".mustache").openStream());
        Mustache mustache = mf.compile(inputStreamReader, "Blah");

        Map<String, Object> context = new HashMap<>();
        context.put("validationResult", customReport.errorList);
        context.put("infoResult", customReport.infoList);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.setInitialFileName("EPUBCheckFX-Report");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*." + selectedExportFormat.getExtension()));
        File file = fileChooser.showSaveDialog(exportFormat.getScene().getWindow());

        if (file != null) {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            mustache.execute(writer, context).flush();
        }
    }
}
