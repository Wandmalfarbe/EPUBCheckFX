package de.pascalwagler.epubcheckfx.model.ace;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Help {
    private String url;

    @JsonProperty("dct:title")
    private String title;

    @JsonProperty("dct:description")
    private String description;
}
