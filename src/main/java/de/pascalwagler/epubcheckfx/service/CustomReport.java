package de.pascalwagler.epubcheckfx.service;

import com.adobe.epubcheck.api.EPUBLocation;
import com.adobe.epubcheck.api.Report;
import com.adobe.epubcheck.messages.LocalizedMessages;
import com.adobe.epubcheck.messages.Message;
import com.adobe.epubcheck.messages.MessageDictionary;
import com.adobe.epubcheck.messages.MessageId;
import com.adobe.epubcheck.messages.Severity;
import com.adobe.epubcheck.util.FeatureEnum;
import de.pascalwagler.epubcheckfx.model.CheckMessage;
import de.pascalwagler.epubcheckfx.model.InfoMessage;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.util.Locale;

public class CustomReport implements Report {
    private final LocalizedMessages localizedMessages = new LocalizedMessages(Locale.getDefault());

    public final ObservableList<InfoMessage> infoList = FXCollections.observableArrayList();
    public final ObservableList<CheckMessage> errorList = FXCollections.observableArrayList();

    @Override
    public void initialize() {
        // There is no initialization to be performed.
    }

    @Override
    public void message(MessageId messageId, EPUBLocation epubLocation, Object... objects) {
        String localizedMessage = localizedMessages.getMessage(messageId).getMessage();
        String formattedMessage = String.format(localizedMessage, objects);
        Severity severity = localizedMessages.getMessage(messageId).getSeverity();

        Platform.runLater(() -> errorList.add(CheckMessage.builder()
                .messageId(messageId)
                .message(formattedMessage)
                .severity(de.pascalwagler.epubcheckfx.model.Severity.fromEpubcheckSeverity(severity))
                .suggestion(localizedMessages.getSuggestion(messageId))
                .path(epubLocation.path)
                .line(epubLocation.line != -1 ? epubLocation.line : null)
                .column(epubLocation.column != -1 ? epubLocation.column : null)
                .objects(objects)
                .build()));
    }

    @Override
    public void message(Message message, EPUBLocation epubLocation, Object... objects) {
        // I don't really know what this callback does. the message callback above seems to suffice.
    }

    @Override
    public void info(String resource, FeatureEnum feature, String value) {
        infoList.add(InfoMessage.builder()
                .resource(resource)
                .feature(feature)
                .value(value)
                .build());
    }

    @Override
    public int getErrorCount() {
        return (int) errorList.stream()
                .filter(checkMessage -> Severity.ERROR.equals(checkMessage.getSeverity().getEbubcheckSeverity()))
                .count();
    }

    @Override
    public int getWarningCount() {
        return (int) errorList.stream()
                .filter(checkMessage -> Severity.WARNING.equals(checkMessage.getSeverity().getEbubcheckSeverity()))
                .count();
    }

    @Override
    public int getFatalErrorCount() {
        return (int) errorList.stream()
                .filter(checkMessage -> Severity.FATAL.equals(checkMessage.getSeverity().getEbubcheckSeverity()))
                .count();
    }

    @Override
    public int getInfoCount() {
        return (int) errorList.stream()
                .filter(checkMessage -> Severity.INFO.equals(checkMessage.getSeverity().getEbubcheckSeverity()))
                .count();
    }

    @Override
    public int getUsageCount() {
        return (int) errorList.stream()
                .filter(checkMessage -> Severity.ERROR.equals(checkMessage.getSeverity().getEbubcheckSeverity()))
                .count();
    }

    @Override
    public int generate() {
        return 0;
    }

    @Override
    public void setEpubFileName(String s) {
        // Unnecessary for current report generation
    }

    @Override
    public String getEpubFileName() {
        return null;
    }

    @Override
    public void setCustomMessageFile(String s) {
        // Unnecessary for current report generation
    }

    @Override
    public String getCustomMessageFile() {
        return null;
    }

    @Override
    public int getReportingLevel() {
        return 0;
    }

    @Override
    public void setReportingLevel(int i) {
        // Unnecessary for current report generation
    }

    @Override
    public void close() {
        // Unnecessary for current report generation
    }

    @Override
    public void setOverrideFile(File file) {
        // Unnecessary for current report generation
    }

    @Override
    public MessageDictionary getDictionary() {
        return null;
    }
}
