package de.pascalwagler.epubcheckfx.model;

import com.adobe.epubcheck.messages.MessageId;
import com.adobe.epubcheck.messages.Severity;
import lombok.Builder;
import lombok.Data;

import java.net.URL;

@Data
@Builder
public class CheckMessage {
    private MessageId messageId;
    private URL url;
    private String path;
    private Integer line;
    private Integer column;
    private Object[] objects;

    private String message;
    private String localizedMessage;

    private Severity severity;
    private String localizedSeverity;

    private String suggestion;
    private String localizedSuggestion;
}
