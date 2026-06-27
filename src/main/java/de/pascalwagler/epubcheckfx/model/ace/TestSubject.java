package de.pascalwagler.epubcheckfx.model.ace;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class TestSubject {
    private String url;
    private Map<String, Object> metadata = new HashMap<>();
    private Map<String, Object> links = new HashMap<>();
    private String epubVersion;

    @JsonProperty("dct:title")
    private String title;
}
