package de.pascalwagler.epubcheckfx.model.ace;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class EarlTest {
    @JsonProperty("earl:impact")
    private Impact impact;

    @JsonProperty("dct:title")
    private String title;

    @JsonProperty("dct:description")
    private String description;

    private Help help;

    private List<String> rulesetTags = new ArrayList<>();
}
