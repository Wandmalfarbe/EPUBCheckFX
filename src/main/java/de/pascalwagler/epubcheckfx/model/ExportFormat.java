package de.pascalwagler.epubcheckfx.model;

import de.pascalwagler.epubcheckfx.ui.Translatable;
import lombok.Getter;

@Getter
public enum ExportFormat implements Translatable {
    HTML("html",
            new String[]{"html", "htm"}, "export_format.html"),
    MARKDOWN("markdown",
            new String[]{"md", "markdown"}, "export_format.markdown"),
    ASCIIDOC("asciidoc",
            new String[]{"adoc", "asciidoc", "txt"}, "export_format.asciidoc"),
    RESTRUCTUREDTEXT("restructuredtext",
            new String[]{"rst"}, "export_format.restructuredtext"),
    TEXTILE("textile",
            new String[]{"textile"}, "export_format.textile"),
    PLAINTEXT("plaintext",
            new String[]{"txt"}, "export_format.plaintext"),
    EPUB_CHECKER_TXT("epub-checker-txt",
            new String[]{"txt"}, "export_format.epub-checker-txt"),
    CSV("comma-separated-values",
            new String[]{"csv"}, "export_format.csv"),
    TSV("tab-separated-values",
            new String[]{"tsv", "tab"}, "export_format.tsv");

    ExportFormat(String name, String[] extensions, String i18nKey) {
        this.name = name;
        this.extensions = extensions;
        this.i18nKey = i18nKey;
    }

    private final String name;
    private final String[] extensions;
    private final String i18nKey;
}
