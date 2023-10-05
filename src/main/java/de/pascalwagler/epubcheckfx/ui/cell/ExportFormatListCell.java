package de.pascalwagler.epubcheckfx.ui.cell;

import atlantafx.base.theme.Styles;
import de.pascalwagler.epubcheckfx.model.ExportFormat;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

import java.util.ResourceBundle;

public class ExportFormatListCell extends ListCell<ExportFormat> {

    private final ResourceBundle resourceBundle;

    public ExportFormatListCell(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    @Override
    protected void updateItem(ExportFormat exportFormat, boolean empty) {
        super.updateItem(exportFormat, empty);
        if (!empty && exportFormat != null) {

            Pane pane = new Pane();
            HBox.setHgrow(pane, Priority.ALWAYS);
            Label l1 = new Label(resourceBundle.getString(exportFormat.getI18nKey()));
            Label l2 = new Label(exportFormat.getExtensions()[0]);
            l2.getStyleClass().add(Styles.TEXT_SUBTLE);

            HBox hBox = new HBox(l1, pane, l2);
            setText(null);
            setGraphic(hBox);
        } else {
            setText(null);
            setGraphic(null);
        }
    }
}
