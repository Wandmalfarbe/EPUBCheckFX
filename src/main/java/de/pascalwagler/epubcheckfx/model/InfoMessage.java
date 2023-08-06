package de.pascalwagler.epubcheckfx.model;

import com.adobe.epubcheck.util.FeatureEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InfoMessage {
    String resource;
    FeatureEnum feature;
    String value;
}
