package de.pascalwagler.epubcheckfx.model.ace;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class A11yMetadata {
    private List<String> missing = new ArrayList<>();
    private List<String> empty = new ArrayList<>();
    private List<String> present = new ArrayList<>();
}
