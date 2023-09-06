package de.pascalwagler.epubcheckfx.service;

import de.pascalwagler.epubcheckfx.model.CheckMessage;

public class CheckMessageMapper {
    private CheckMessageMapper() {
    }

    public static String[] toStringArray(CheckMessage checkMessage) {
        return new String[]{
                checkMessage.getMessageId().toString(),
                checkMessage.getSeverity().toString(),
                checkMessage.getMessage(),
                checkMessage.getPath(),
                checkMessage.getLine() == null ? "" : String.valueOf(checkMessage.getLine()),
                checkMessage.getColumn() == null ? "" : String.valueOf(checkMessage.getColumn())
        };
    }
}
