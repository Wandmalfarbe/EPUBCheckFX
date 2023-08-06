package de.pascalwagler.epubcheckfx.model;

import de.pascalwagler.epubcheckfx.ui.Translatable;
import lombok.Getter;

public enum ExportFormat implements Translatable {
    HTML("html", "html", "export_format.html"),
    MARKDOWN("markdown", "md", "export_format.markdown"),
    MARKDOWN_LIST("markdown-list", "md", "export_format.markdown_list"),
    PLAINTEXT("plaintext", "txt", "export_format.plaintext"),
    EPUB_CHECKER_TXT("epub-checker-txt", "txt", "export_format.epub-checker-txt");

    ExportFormat(String name, String extension, String i18nKey) {
        this.name = name;
        this.extension = extension;
        this.i18nKey = i18nKey;
    }

    @Getter
    private final String name;
    @Getter
    private final String extension;
    @Getter
    private final String i18nKey;
}
