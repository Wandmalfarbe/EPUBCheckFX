package de.pascalwagler.epubcheckfx.ui;

import atlantafx.base.controls.Card;
import atlantafx.base.controls.Tile;
import com.adobe.epubcheck.api.EpubCheck;
import com.adobe.epubcheck.messages.Severity;
import com.adobe.epubcheck.util.Archive;
import com.adobe.epubcheck.util.FeatureEnum;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import de.pascalwagler.epubcheckfx.model.CheckMessage;
import de.pascalwagler.epubcheckfx.model.EpubProfile;
import de.pascalwagler.epubcheckfx.model.ExportFormat;
import de.pascalwagler.epubcheckfx.model.InfoMessage;
import de.pascalwagler.epubcheckfx.service.CustomReport;
import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Pair;
import lombok.Setter;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

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
    private TableView<InfoMessage> infoResultTable;
    @FXML
    private TreeTableColumn<InfoMessage, String> resource;
    @FXML
    private TreeTableColumn<InfoMessage, FeatureEnum> feature;
    @FXML
    private TreeTableColumn<InfoMessage, String> value;

    @FXML
    private Label epubcheckVersion;

    @FXML
    private ComboBox<EpubProfile> epubProfile;

    @FXML
    private ComboBox<ExportFormat> exportFormat;

    private final CustomReport customReport = new CustomReport();

    private ResourceBundle resourceBundle;

    private File epubFile;

    @FXML
    private TreeView<InfoMessage> infoTreeTableView;
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

    private static File tempDirectory = new File(System.getProperty("java.io.tmpdir"), "EPUBCheckFX");

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;

        try {
            initTempDir();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        initValidationResultTable();
        initInfoResultTable();
        initExportFormat();
        initEpubProfile();
        epubcheckVersion.setText("EPUBCheck Version " + EpubCheck.version() + " - (Built " + EpubCheck.buildDate() + ")");
    }

    private void initTempDir() throws IOException {
        if (!tempDirectory.exists()) {
            Files.createDirectory(tempDirectory.toPath());
        }
    }

    private void initInfoResultTable() {

        /*infoTreeTableView.setCellFactory(tv -> new TextFieldTreeCell<>(new StringConverter<>() {
            @Override
            public InfoMessage fromString(String text) {
                return null;
            }

            @Override
            public String toString(InfoMessage infoMessage) {
                if(infoMessage.getResource() == null) {
                    return infoMessage.getFeature() + ": " + infoMessage.getValue();
                } else {
                    return infoMessage.getResource();
                }
            }
        }));*/


        /*resource.setCellValueFactory(
                cellDataFeatures -> new SimpleStringProperty(cellDataFeatures.getValue().getValue().getResource())
        );
        feature.setCellValueFactory(
                cellDataFeatures -> new SimpleObjectProperty<FeatureEnum>(cellDataFeatures.getValue().getValue().getFeature())
        );
        value.setCellValueFactory(
                cellDataFeatures -> new SimpleStringProperty(cellDataFeatures.getValue().getValue().getValue())
        );*/

        /*resource.setCellValueFactory(new PropertyValueFactory<>("resource"));
        feature.setCellValueFactory(new PropertyValueFactory<>("feature"));
        value.setCellValueFactory(new PropertyValueFactory<>("value"));*/
    }

    private void initValidationResultTable() {

        Property<ObservableList<CheckMessage>> authorListProperty = new SimpleObjectProperty<>(customReport.errorList);
        validationResultTable.itemsProperty().bind(authorListProperty);

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
    private void runEpubCheck() {
        customReport.errorList.clear();
        customReport.infoList.clear();

        CompletableFuture.supplyAsync(() -> {
            System.out.println("Start Validation");

            File epubToValidate;
            if (epubFile.isDirectory()) {
                try {
                    epubToValidate = createTempEpubFromFolder();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                epubToValidate = epubFile;
            }
            EpubCheck epubcheck = new EpubCheck(epubToValidate, customReport, epubProfile.getValue().getEpubProfile());
            epubcheck.check();

            Map<String, List<Pair<FeatureEnum, String>>> infoMap = toMap();
            Node metadataPane = createMetadataPane(infoMap);
            Node infoPane = createInfoPane(infoMap);

            Platform.runLater(() -> {
                scrollMetadata.setContent(metadataPane);
                scrollInfo.setContent(infoPane);
                infoPanelController.updateFromInfoList(infoMap, epubFile);
            });

            System.out.println("End Validation");
            mainWindowController.validationDone();
            //Desktop.getDesktop().browseFileDirectory(epubFile);
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

    private Node createInfoPane(Map<String, List<Pair<FeatureEnum, String>>> infoMap) {
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10, 0, 0, 0));
        vBox.setSpacing(10);

        for (Map.Entry<String, List<Pair<FeatureEnum, String>>> entry : infoMap.entrySet()) {
            String resource = entry.getKey();

            // Skip the general section because it is already displayed unter the metadata tab.
            if ("general".equals(resource)) {
                continue;
            }
            List<Pair<FeatureEnum, String>> features = entry.getValue();

            Card card = new Card();
            VBox.setVgrow(card, Priority.ALWAYS);
            card.setHeader(new Tile(resource, null));
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(5);
            ColumnConstraints col1 = new ColumnConstraints();
            col1.setHalignment(HPos.RIGHT);
            col1.setHgrow(Priority.NEVER);
            col1.setPrefWidth(180);
            ColumnConstraints col2 = new ColumnConstraints();
            col2.setHgrow(Priority.ALWAYS);
            grid.getColumnConstraints().addAll(col1, col2);
            for (int i = 0; i < features.size(); i++) {
                Pair<FeatureEnum, String> pair = features.get(i);

                String featureEnumName = pair.getKey().name().toLowerCase();
                String labelTranslated;
                if (resourceBundle.containsKey("feature." + featureEnumName)) {
                    labelTranslated = resourceBundle.getString("feature." + featureEnumName);
                } else {
                    labelTranslated = pair.getKey().toString();
                }
                Label label = new Label(labelTranslated);
                label.setWrapText(true);
                label.getStyleClass().add("font-bold");

                TextField value = new TextField(pair.getValue());
                value.setEditable(false);

                Node[] row = new Node[]{label, value};
                grid.addRow(i, row);
            }
            card.setBody(grid);
            vBox.getChildren().add(card);
        }
        return vBox;
    }

    private Node createMetadataPane(Map<String, List<Pair<FeatureEnum, String>>> infoMap) {
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10, 0, 0, 0));
        vBox.setSpacing(10);

        List<Pair<FeatureEnum, String>> metadata = infoMap.get("general");
        if (metadata == null) {
            return vBox;
        }

        List<Pair<FeatureEnum, String>> dublinCoreMetadata = metadata.stream()
                .filter(m -> m.getKey().name().toLowerCase().startsWith("dc_"))
                .collect(Collectors.toList());
        List<Pair<FeatureEnum, String>> otherMetadata = metadata.stream()
                .filter(m -> !m.getKey().name().toLowerCase().startsWith("dc_"))
                .collect(Collectors.toList());

        List<List<Pair<FeatureEnum, String>>> metadataGroups = List.of(dublinCoreMetadata, otherMetadata);

        for (List<Pair<FeatureEnum, String>> metadataGroup : metadataGroups) {
            if (metadataGroup.isEmpty()) {
                continue;
            }

            Card card = new Card();
            VBox.setVgrow(card, Priority.ALWAYS);
            card.setHeader(new Tile(resourceBundle.getString("metadata.dublin_core"), null));

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(5);

            ColumnConstraints col1 = new ColumnConstraints();
            col1.setHalignment(HPos.RIGHT);
            col1.setHgrow(Priority.NEVER);
            col1.setPrefWidth(180);
            ColumnConstraints col2 = new ColumnConstraints();
            col2.setHgrow(Priority.ALWAYS);
            grid.getColumnConstraints().addAll(col1, col2);

            for (int i = 0; i < metadataGroup.size(); i++) {
                Pair<FeatureEnum, String> pair = metadataGroup.get(i);

                String featureEnumName = pair.getKey().name().toLowerCase();
                String labelTranslated;
                if (resourceBundle.containsKey("feature." + featureEnumName)) {
                    labelTranslated = resourceBundle.getString("feature." + featureEnumName);
                } else {
                    labelTranslated = pair.getKey().toString();
                }
                Label label = new Label(labelTranslated);
                label.setWrapText(true);
                label.getStyleClass().add("font-bold");

                TextField value = new TextField(pair.getValue());
                value.setEditable(false);

                Node[] row = new Node[]{label, value};
                grid.addRow(i, row);
            }
            card.setBody(grid);
            vBox.getChildren().add(card);
        }

        return vBox;
    }

    private static TreeItem<InfoMessage> toTreeItem(Map<String, List<Pair<FeatureEnum, String>>> infos) {
        TreeItem<InfoMessage> root = new TreeItem<>();
        root.setValue(InfoMessage.builder().build());
        for (Map.Entry<String, List<Pair<FeatureEnum, String>>> entry : infos.entrySet()) {
            String resource = entry.getKey();
            List<Pair<FeatureEnum, String>> features = entry.getValue();

            InfoMessage innerInfo = InfoMessage.builder()
                    .resource(resource)
                    .build();
            TreeItem<InfoMessage> innerItem = new TreeItem<>();
            innerItem.setValue(innerInfo);
            root.getChildren().add(innerItem);

            for (Pair<FeatureEnum, String> feature : features) {
                InfoMessage leafInfo = InfoMessage.builder()
                        .feature(feature.getKey())
                        .value(feature.getValue())
                        .build();
                TreeItem<InfoMessage> leafItem = new TreeItem<>();
                leafItem.setValue(leafInfo);
                innerItem.getChildren().add(leafItem);
            }
        }
        //Platform.runLater(() -> infoTreeTableView.setRoot(root));

        return root;
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
