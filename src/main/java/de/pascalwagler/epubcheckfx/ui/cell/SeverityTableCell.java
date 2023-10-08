package de.pascalwagler.epubcheckfx.ui.cell;

import de.pascalwagler.epubcheckfx.model.CheckMessage;
import de.pascalwagler.epubcheckfx.model.Severity;
import de.pascalwagler.epubcheckfx.ui.UiBuilder;
import javafx.scene.control.TableCell;

import java.util.ResourceBundle;

public class SeverityTableCell extends TableCell<CheckMessage, Severity> {

    private final ResourceBundle resourceBundle;

    public SeverityTableCell(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    @Override
    public void updateItem(de.pascalwagler.epubcheckfx.model.Severity severity, boolean empty) {
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
