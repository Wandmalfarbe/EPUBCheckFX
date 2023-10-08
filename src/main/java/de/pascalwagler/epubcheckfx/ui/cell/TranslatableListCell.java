package de.pascalwagler.epubcheckfx.ui.cell;

import de.pascalwagler.epubcheckfx.ui.Translatable;
import javafx.scene.control.ListCell;

import java.util.ResourceBundle;

public class TranslatableListCell<T extends Translatable> extends ListCell<T> {

    private final ResourceBundle resourceBundle;

    public TranslatableListCell(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    @Override
    protected void updateItem(T cellContent, boolean empty) {
        super.updateItem(cellContent, empty);
        setText(empty || cellContent == null ? "" : resourceBundle.getString(cellContent.getI18nKey()));
    }
}
