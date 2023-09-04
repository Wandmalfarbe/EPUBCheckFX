package de.pascalwagler.epubcheckfx.model;

import de.pascalwagler.epubcheckfx.ui.Translatable;
import lombok.Getter;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.material2.Material2OutlinedAL;

@Getter
public enum Severity implements Translatable {

    SUPPRESSED(com.adobe.epubcheck.messages.Severity.SUPPRESSED, Material2OutlinedAL.DO_NOT_DISTURB_ALT, "color-suppressed", "severity.suppressed"),

    USAGE(com.adobe.epubcheck.messages.Severity.USAGE, Material2OutlinedAL.ADJUST, "color-usage", "severity.usage"),

    INFO(com.adobe.epubcheck.messages.Severity.INFO, Material2OutlinedAL.INFO, "color-info", "severity.info"),

    WARNING(com.adobe.epubcheck.messages.Severity.WARNING, Material2OutlinedAL.ERROR_OUTLINE, "color-warning", "severity.warning"),

    ERROR(com.adobe.epubcheck.messages.Severity.ERROR, Material2OutlinedAL.ERROR_OUTLINE, "color-error", "severity.error"),

    FATAL(com.adobe.epubcheck.messages.Severity.FATAL, Material2OutlinedAL.ERROR, "color-fatal", "severity.fatal");

    Severity(com.adobe.epubcheck.messages.Severity ebubcheckSeverity, Ikon icon, String colorStyleClass, String i18nKey) {
        this.ebubcheckSeverity = ebubcheckSeverity;
        this.icon = icon;
        this.colorStyleClass = colorStyleClass;
        this.i18nKey = i18nKey;
    }

    private final com.adobe.epubcheck.messages.Severity ebubcheckSeverity;
    private final Ikon icon;
    private final String colorStyleClass;
    private final String i18nKey;

    public static Severity fromEpubcheckSeverity(com.adobe.epubcheck.messages.Severity ebubcheckSeverity) {
        for (Severity severity : values()) {
            if (severity.getEbubcheckSeverity().equals(ebubcheckSeverity)) {
                return severity;
            }
        }
        return SUPPRESSED;
    }
}
