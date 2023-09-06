package de.pascalwagler.epubcheckfx.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ReportTranslatedStrings {
    private String headingReport;
    private String headingValidationResults;

    private String messageId;
    private String severity;
    private String message;
    private String path;
    private String line;
    private String column;
}
