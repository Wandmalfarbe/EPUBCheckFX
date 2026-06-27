package de.pascalwagler.epubcheckfx.model.ace;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Software {
    @JsonProperty("@type")
    private String type;

    @JsonProperty("doap:name")
    private String name;

    @JsonProperty("doap:description")
    private String description;

    @JsonProperty("doap:homepage")
    private String homepage;

    @JsonProperty("doap:created")
    private String created;

    @JsonProperty("doap:release")
    private Release release;
}
