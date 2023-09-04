package de.pascalwagler.epubcheckfx.ui;

import atlantafx.base.controls.Card;
import atlantafx.base.controls.Tile;
import com.adobe.epubcheck.util.FeatureEnum;
import de.pascalwagler.epubcheckfx.model.Severity;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.util.Pair;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class UiHelper {

    private UiHelper() {
    }

    public static FontIcon createSeverityIcon(Severity severity) {
        FontIcon fontIcon = new FontIcon(severity.getIcon());
        fontIcon.getStyleClass().add(severity.getColorStyleClass());
        return fontIcon;
    }

    public static Node createInfoPane(Map<String, List<Pair<FeatureEnum, String>>> infoMap, ResourceBundle resourceBundle) {
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10, 0, 0, 0));
        vBox.setSpacing(10);

        for (Map.Entry<String, List<Pair<FeatureEnum, String>>> entry : infoMap.entrySet()) {
            String resource = entry.getKey();

            // Skip the general section because it is already displayed under the metadata tab.
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

                TextField valueTextField = new TextField(pair.getValue());
                valueTextField.setEditable(false);

                Node[] row = new Node[]{label, valueTextField};
                grid.addRow(i, row);
            }
            card.setBody(grid);
            vBox.getChildren().add(card);
        }
        return vBox;
    }

    public static Node createMetadataPane(Map<String, List<Pair<FeatureEnum, String>>> infoMap, ResourceBundle resourceBundle) {
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

                TextField valueTextField = new TextField(pair.getValue());
                valueTextField.setEditable(false);

                Node[] row = new Node[]{label, valueTextField};
                grid.addRow(i, row);
            }
            card.setBody(grid);
            vBox.getChildren().add(card);
        }

        return vBox;
    }
}
