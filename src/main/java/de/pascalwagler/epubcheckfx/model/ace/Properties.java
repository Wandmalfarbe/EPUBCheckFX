package de.pascalwagler.epubcheckfx.model.ace;

import lombok.Data;

@Data
public class Properties {
    private boolean hasManifestFallbacks;
    private boolean hasBindings;
    private boolean hasSVGContentDocuments;
    private boolean hasFormElements;
    private boolean hasMathML;
    private boolean hasPageBreaks;
}
