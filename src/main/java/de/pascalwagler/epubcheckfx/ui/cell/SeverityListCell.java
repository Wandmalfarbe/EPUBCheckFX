package de.pascalwagler.epubcheckfx.ui.cell;

import de.pascalwagler.epubcheckfx.model.Severity;
import de.pascalwagler.epubcheckfx.ui.UiBuilder;
import javafx.scene.control.ListCell;

import java.util.ResourceBundle;

public class SeverityListCell extends ListCell<Severity> {

    private final ResourceBundle resourceBundle;

    public SeverityListCell(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    @Override
    public void updateItem(Severity severity, boolean empty) {
        super.updateItem(severity, empty);
        if (!empty && severity != null) {
            setText(resourceBundle.getString(severity.getI18nKey()));
            setGraphic(UiBuilder.createSeverityIcon(severity));
        } else {
            setText(null);
            setGraphic(null);
        }
    }
}
