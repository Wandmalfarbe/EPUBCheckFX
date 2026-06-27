package de.pascalwagler.epubcheckfx.model.ace;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class EarlPointer {
    private List<String> cfi = new ArrayList<>();
    private List<String> css = new ArrayList<>();
}
