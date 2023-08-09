package de.pascalwagler.epubcheckfx.ui;

import atlantafx.base.theme.Styles;
import com.adobe.epubcheck.messages.Severity;
import de.pascalwagler.epubcheckfx.model.CheckMessage;
import javafx.scene.control.TableCell;
import javafx.util.Pair;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2OutlinedAL;

import java.util.Map;
import java.util.ResourceBundle;

public class SeverityTableCell extends TableCell<CheckMessage, Severity> {

    private final ResourceBundle resourceBundle;

    public SeverityTableCell(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    public static FontIcon createSeverityIcon(Severity severity) {
        if (!severityToIcon.containsKey(severity)) {
            FontIcon fontIcon = new FontIcon(Material2OutlinedAL.ERROR_OUTLINE);
            fontIcon.getStyleClass().add(Styles.DANGER);
            return fontIcon;
        }
        FontIcon fontIcon = new FontIcon(severityToIcon.get(severity).getKey());
        fontIcon.getStyleClass().add(severityToIcon.get(severity).getValue());
        return fontIcon;
    }

    public static final Map<Severity, Pair<Ikon, String>> severityToIcon = Map.of(
            Severity.SUPPRESSED, new Pair<>(Material2OutlinedAL.INFO, Styles.TEXT_SUBTLE),
            Severity.INFO, new Pair<>(Material2OutlinedAL.INFO, Styles.ACCENT),
            Severity.USAGE, new Pair<>(Material2OutlinedAL.INFO, Styles.TEXT_MUTED),
            Severity.WARNING, new Pair<>(Material2OutlinedAL.ERROR_OUTLINE, Styles.WARNING),
            Severity.ERROR, new Pair<>(Material2OutlinedAL.ERROR_OUTLINE, Styles.DANGER),
            Severity.FATAL, new Pair<>(Material2OutlinedAL.ERROR_OUTLINE, Styles.DANGER)
    );

    public static final Map<Severity, String> severityToI18nString = Map.of(
            Severity.INFO, "severity.info",
            Severity.SUPPRESSED, "severity.suppressed",
            Severity.USAGE, "severity.usage",
            Severity.WARNING, "severity.warning",
            Severity.ERROR, "severity.error",
            Severity.FATAL, "severity.fatal"
    );

    @Override
    public void updateItem(Severity severity, boolean empty) {
        super.updateItem(severity, empty);
        if (!empty && severity != null) {
            setText(resourceBundle.getString(severityToI18nString.get(severity)));
            setGraphic(createSeverityIcon(severity));
        } else {
            setText(null);
            setGraphic(null);
        }
    }
}
