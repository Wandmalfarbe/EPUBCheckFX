package de.pascalwagler.epubcheckfx.model.ace;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@lombok.Data
public class AceReport {
    @JsonProperty("@type")
    private String type;

    @JsonProperty("@context")
    private String context;

    @JsonProperty("dct:title")
    private String title;

    @JsonProperty("dct:description")
    private String description;

    @JsonProperty("dct:date")
    private String date;

    @JsonProperty("earl:assertedBy")
    private Software assertedBy;

    private Outlines outlines;

    private Data data;

    private Properties properties;

    @JsonProperty("earl:testSubject")
    private TestSubject testSubject;

    @JsonProperty("a11y-metadata")
    private A11yMetadata a11yMetadata;

    private List<Assertion> assertions = new ArrayList<>();

    @JsonProperty("earl:result")
    private EarlResult result;
}
