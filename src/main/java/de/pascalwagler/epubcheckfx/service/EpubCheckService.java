package de.pascalwagler.epubcheckfx.service;

import com.adobe.epubcheck.api.EPUBProfile;
import com.adobe.epubcheck.api.EpubCheck;
import com.adobe.epubcheck.util.Archive;
import com.adobe.epubcheck.util.FeatureEnum;
import de.pascalwagler.epubcheckfx.model.EpubCheckValidationResult;
import de.pascalwagler.epubcheckfx.model.InfoMessage;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class EpubCheckService {

    private EpubCheckService() {
    }

    private static final File tempDirectory = new File(System.getProperty("java.io.tmpdir"), "EPUBCheckFX");

    public static EpubCheckValidationResult runEpubCheck(File epubFile, EPUBProfile epubProfile) {
        try {
            log.debug("Start Validation");
            File epubToValidate;
            if (epubFile.isDirectory()) {
                epubToValidate = createTempEpubFromFolder(epubFile);
            } else {
                epubToValidate = epubFile;
            }
            final CustomReport customReport = new CustomReport();
            EpubCheck epubcheck = new EpubCheck(epubToValidate, customReport, epubProfile);
            epubcheck.check();
            return EpubCheckValidationResult.builder()
                    .infoList(customReport.infoList)
                    .errorList(customReport.errorList)
                    .infoMap(mapCustomReportToInfoMap(customReport))
                    .build();
        } catch (Exception exception) {
            log.error("An unexpected error occurred during the validation.", exception);
            throw new RuntimeException(exception);
        } finally {
            log.debug("End Validation");
        }
    }

    private static Map<String, List<Pair<FeatureEnum, String>>> mapCustomReportToInfoMap(CustomReport customReport) {
        Map<String, List<Pair<FeatureEnum, String>>> infos = new HashMap<>();

        for (InfoMessage item : customReport.infoList) {

            String resource2 = item.getResource() == null ? "general" : item.getResource();

            if (infos.containsKey(resource2)) {
                List<Pair<FeatureEnum, String>> list = infos.get(resource2);
                list.add(new Pair<>(item.getFeature(), item.getValue()));
            } else {
                List<Pair<FeatureEnum, String>> list = new ArrayList<>();
                list.add(new Pair<>(item.getFeature(), item.getValue()));
                infos.put(resource2, list);
            }
        }
        return infos;
    }

    private static File createTempEpubFromFolder(File epubDirectory) throws IOException {
        Archive epub = new Archive(epubDirectory.getPath(), true);
        File temporaryEpubFile = new File(tempDirectory, epub.getEpubName());

        if (temporaryEpubFile.exists()) {
            Files.delete(temporaryEpubFile.toPath());
        }

        epub.createArchive(temporaryEpubFile);
        return temporaryEpubFile;
    }
}
