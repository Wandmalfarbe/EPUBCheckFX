package de.pascalwagler.epubcheckfx.service;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import de.pascalwagler.epubcheckfx.model.CheckMessage;
import de.pascalwagler.epubcheckfx.model.ExportFormat;
import de.pascalwagler.epubcheckfx.model.ReportMetadata;
import de.pascalwagler.epubcheckfx.model.ReportTranslatedStrings;
import javafx.collections.ObservableList;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

@Slf4j
public class ExportService {

    private ExportService() {
    }

    public static void exportCsvTsv(ObservableList<CheckMessage> errorList, ExportFormat exportFormat,
            File file, ResourceBundle resourceBundle) throws IOException {

        CSVFormat.Builder csvFormatBuilder = CSVFormat.DEFAULT.builder();
        if (exportFormat == ExportFormat.TSV) {
            csvFormatBuilder = CSVFormat.MONGODB_TSV.builder();
        }
        CSVFormat csvFormat = csvFormatBuilder
                .setHeader(getHeaderNames(resourceBundle))
                .build();

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        try (final CSVPrinter printer = new CSVPrinter(writer, csvFormat)) {
            errorList.forEach(checkMessage -> {
                try {
                    printer.printRecord((Object[]) CheckMessageMapper.toStringArray(checkMessage));
                } catch (IOException e) {
                    log.error("Could not append row to CSV or TSV.", e);
                }
            });
            printer.flush();
        }
    }

    public static void exportWithMustache(ObservableList<CheckMessage> errorList, ExportFormat exportFormat,
            File file, ResourceBundle resourceBundle) throws IOException {

        MustacheFactory mf = new DefaultMustacheFactory();
        String mustacheTemplate = "/mustache/" + exportFormat.getName() + ".mustache";
        URL resource = ExportService.class.getResource(mustacheTemplate);
        if (resource == null) {
            log.error("Could not open mustache template {}", mustacheTemplate);
            return;
        }
        InputStreamReader inputStreamReader = new InputStreamReader(resource.openStream());
        Mustache mustache = mf.compile(inputStreamReader, "Blah");

        Map<String, Object> context = new HashMap<>();
        context.put("translatedStrings", ExportService.getTranslatedStrings(resourceBundle));
        context.put("metadata", ExportService.getReportMetadata());
        context.put("validationResult", errorList);

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        mustache.execute(writer, context).flush();
    }

    private static String[] getHeaderNames(ResourceBundle resourceBundle) {
        return new String[]{
                resourceBundle.getString("result_table.column_message_id"),
                resourceBundle.getString("result_table.column_severity"),
                resourceBundle.getString("result_table.column_message"),
                resourceBundle.getString("result_table.column_path"),
                resourceBundle.getString("result_table.column_line"),
                resourceBundle.getString("result_table.column_column")
        };
    }

    private static ReportTranslatedStrings getTranslatedStrings(ResourceBundle resourceBundle) {
        return ReportTranslatedStrings.builder()
                .headingReport(resourceBundle.getString("report.heading_report"))
                .headingValidationResults(resourceBundle.getString("report.heading_validation_results"))
                .messageId(resourceBundle.getString("result_table.column_message_id"))
                .severity(resourceBundle.getString("result_table.column_severity"))
                .message(resourceBundle.getString("result_table.column_message"))
                .path(resourceBundle.getString("result_table.column_path"))
                .line(resourceBundle.getString("result_table.column_line"))
                .column(resourceBundle.getString("result_table.column_column"))
                .build();
    }

    private static ReportMetadata getReportMetadata() {
        return ReportMetadata.builder()
                .language(Locale.getDefault().getLanguage())
                .build();
    }
}
