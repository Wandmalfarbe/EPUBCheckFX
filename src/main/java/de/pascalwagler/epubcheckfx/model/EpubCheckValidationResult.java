package de.pascalwagler.epubcheckfx.model;

import com.adobe.epubcheck.util.FeatureEnum;
import javafx.util.Pair;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Builder
@Data
public class EpubCheckValidationResult {
    @Builder.Default
    public final List<InfoMessage> infoList = new ArrayList<>();
    @Builder.Default
    public final List<CheckMessage> errorList = new ArrayList<>();
    @Builder.Default
    Map<String, List<Pair<FeatureEnum, String>>> infoMap = new HashMap<>();
}
