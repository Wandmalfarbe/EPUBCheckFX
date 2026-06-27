package de.pascalwagler.epubcheckfx.model.ace;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EarlResult {
    @JsonProperty("earl:outcome")
    private String outcome;

    @JsonProperty("dct:description")
    private String description;

    @JsonProperty("earl:pointer")
    private EarlPointer pointer;

    private String html;
}
