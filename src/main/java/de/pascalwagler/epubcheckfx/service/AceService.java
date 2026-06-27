package de.pascalwagler.epubcheckfx.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.pascalwagler.epubcheckfx.model.CheckMessage;
import de.pascalwagler.epubcheckfx.model.EpubCheckValidationResult;
import de.pascalwagler.epubcheckfx.model.Severity;
import de.pascalwagler.epubcheckfx.model.ace.AceReport;
import de.pascalwagler.epubcheckfx.model.ace.Impact;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

@Slf4j
public class AceService {

    private AceService() {
    }

    private static final File tempDirectory = new File(System.getProperty("java.io.tmpdir"), "EPUBCheckFX");
    private static final File aceReportJson = new File(tempDirectory, "report.json");
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static String getVersion() {
        CommandLine cmdLine = new CommandLine("ace");
        cmdLine.addArgument("--version");

        DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler() {
            @Override
            public void onProcessComplete(int exitValue) {
                super.onProcessComplete(exitValue);
                log.info("Process finished");
            }

            @Override
            public void onProcessFailed(ExecuteException e) {
                super.onProcessFailed(e);
                log.error("Process failed", e);
            }
        };
        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        PumpStreamHandler psh = new PumpStreamHandler(stdout);

        ExecuteWatchdog watchdog = ExecuteWatchdog.builder().setTimeout(Duration.ofSeconds(60)).get();
        Executor executor = DefaultExecutor.builder().get();
        executor.setStreamHandler(psh);
        executor.setWatchdog(watchdog);
        try {
            executor.execute(cmdLine, resultHandler);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // some time later the result handler callback was invoked so we
        // can safely request the exit value
        try {
            resultHandler.waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        int exitValue = resultHandler.getExitValue();
        return stdout.toString().trim();
    }

    public static EpubCheckValidationResult runAce(File epubFile) {
        Locale.setDefault(Locale.GERMAN);
        CommandLine cmdLine = new CommandLine("ace");
        cmdLine.addArgument("${epubFile}");
        cmdLine.addArgument("--outdir");
        cmdLine.addArgument("${tempDirectory}");
        cmdLine.addArgument("--force");

        HashMap<String, Object> map = new HashMap<>();
        map.put("epubFile", epubFile.toString());
        map.put("tempDirectory", tempDirectory.toString());
        cmdLine.setSubstitutionMap(map);

        log.info("Executing ace with command: {}", cmdLine);


        DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler() {
            @Override
            public void onProcessComplete(int exitValue) {
                super.onProcessComplete(exitValue);
                log.info("Process finished");
            }

            @Override
            public void onProcessFailed(ExecuteException e) {
                super.onProcessFailed(e);
                log.error("Process failed", e);
            }
        };
        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        PumpStreamHandler psh = new PumpStreamHandler(stdout);

        ExecuteWatchdog watchdog = ExecuteWatchdog.builder().setTimeout(Duration.ofSeconds(60)).get();
        Executor executor = DefaultExecutor.builder().get();
        executor.setStreamHandler(psh);
        executor.setWatchdog(watchdog);
        try {
            executor.execute(cmdLine, resultHandler);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // some time later the result handler callback was invoked so we
        // can safely request the exit value
        try {
            resultHandler.waitFor();
            if (resultHandler.getExitValue() != 0) {
                log.error("Process finished with exit code {}", resultHandler.getExitValue());
                final String msgWithoutColorCodes = stdout.toString().replaceAll("\u001B\\[[;\\d]*m", "");
                System.out.println(msgWithoutColorCodes);
                return EpubCheckValidationResult.builder()
                        .build();
            }
            return readReport();
        } catch (InterruptedException e) {
            log.error("Process interrupted", e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            log.error("Caught IOException", e);
            throw new RuntimeException(e);
        }
    }

    private static EpubCheckValidationResult readReport() throws IOException {
        String reportJson = Files.readString(aceReportJson.toPath());

        AceReport report = objectMapper.readValue(reportJson, AceReport.class);
        return mapResult(report);
    }

    private static EpubCheckValidationResult mapResult(AceReport aceReport) {
        List<CheckMessage> assertionList = aceReport.getAssertions().stream()
                .filter(a -> "fail".equalsIgnoreCase(a.getResult().getOutcome()))
                .flatMap(a -> a.getAssertions().stream()
                        .filter(n -> "fail".equalsIgnoreCase(n.getResult().getOutcome()))
                        .map(n -> CheckMessage.builder()
                                .messageId(n.getTest().getTitle())
                                .severity(mapImpactToSeverity(n.getTest().getImpact()))
                                .suggestion(n.getTest().getHelp().getDescription())
                                .path(a.getTestSubject().getUrl())
                                .message(n.getResult().getDescription())
                                .build()))
                .collect(Collectors.toList());

        return EpubCheckValidationResult.builder()
                .errorList(assertionList)
                .build();
    }

    private static Severity mapImpactToSeverity(Impact impact) {
        switch (impact) {
            case CRITICAL:
                return Severity.FATAL;
            case SERIOUS:
                return Severity.ERROR;
            case MODERATE:
                return Severity.WARNING;
            case MINOR:
                return Severity.INFO;
            default:
                return Severity.SUPPRESSED;
        }
    }
}
