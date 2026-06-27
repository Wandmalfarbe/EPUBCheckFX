package de.pascalwagler.epubcheckfx.model.ace;

import lombok.Data;

@Data
public class Image {
    private String src;
    private String alt;
    private String role;
    private String html;
    private String location;
}
