package de.pascalwagler.epubcheckfx.model.ace;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Assertion {
    @JsonProperty("@type")
    private String type;

    @JsonProperty("earl:result")
    private EarlResult result;

    private List<Assertion> assertions = new ArrayList<>();

    @JsonProperty("earl:testSubject")
    private TestSubject testSubject;

    @JsonProperty("earl:assertedBy")
    private String assertedBy;

    @JsonProperty("earl:mode")
    private String mode;

    @JsonProperty("earl:test")
    private EarlTest test;
}
