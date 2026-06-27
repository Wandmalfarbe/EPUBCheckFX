package de.pascalwagler.epubcheckfx.model.ace;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Release {
    @JsonProperty("doap:revision")
    private String revision;
}
