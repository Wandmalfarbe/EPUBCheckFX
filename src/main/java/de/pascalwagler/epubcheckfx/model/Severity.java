package de.pascalwagler.epubcheckfx.model;

import atlantafx.base.theme.Styles;
import de.pascalwagler.epubcheckfx.ui.Translatable;
import lombok.Getter;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.material2.Material2OutlinedAL;

public enum Severity implements Translatable {


    SUPPRESSED(com.adobe.epubcheck.messages.Severity.SUPPRESSED, Material2OutlinedAL.INFO, Styles.TEXT_SUBTLE, "severity.suppressed"),

    USAGE(com.adobe.epubcheck.messages.Severity.USAGE, Material2OutlinedAL.INFO, Styles.TEXT_MUTED, "severity.usage"),

    INFO(com.adobe.epubcheck.messages.Severity.INFO, Material2OutlinedAL.INFO, Styles.ACCENT, "severity.info"),

    WARNING(com.adobe.epubcheck.messages.Severity.WARNING, Material2OutlinedAL.ERROR_OUTLINE, Styles.WARNING, "severity.warning"),

    ERROR(com.adobe.epubcheck.messages.Severity.ERROR, Material2OutlinedAL.ERROR_OUTLINE, Styles.DANGER, "severity.error"),

    FATAL(com.adobe.epubcheck.messages.Severity.FATAL, Material2OutlinedAL.ERROR_OUTLINE, Styles.DANGER, "severity.fatal");

    Severity(com.adobe.epubcheck.messages.Severity ebubcheckSeverity, Ikon icon, String colorStyleClass, String i18nKey) {
        this.ebubcheckSeverity = ebubcheckSeverity;
        this.icon = icon;
        this.colorStyleClass = colorStyleClass;
        this.i18nKey = i18nKey;
    }

    @Getter
    private final com.adobe.epubcheck.messages.Severity ebubcheckSeverity;
    @Getter
    private final Ikon icon;
    @Getter
    private final String colorStyleClass;
    @Getter
    private final String i18nKey;
}
