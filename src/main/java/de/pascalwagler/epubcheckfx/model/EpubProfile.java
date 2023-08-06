package de.pascalwagler.epubcheckfx.model;

import com.adobe.epubcheck.api.EPUBProfile;
import de.pascalwagler.epubcheckfx.ui.Translatable;
import lombok.Getter;

public enum EpubProfile implements Translatable {
    DEFAULT("epub_profile.default", EPUBProfile.DEFAULT),
    IDX("epub_profile.idx", EPUBProfile.IDX),
    DICT("epub_profile.dict", EPUBProfile.DICT),
    EDUPUB("epub_profile.edupub", EPUBProfile.EDUPUB),
    PREVIEW("epub_profile.preview", EPUBProfile.PREVIEW);

    EpubProfile(String i18nKey, EPUBProfile epubProfile) {
        this.i18nKey = i18nKey;
        this.epubProfile = epubProfile;
    }

    @Getter
    private final String i18nKey;

    @Getter
    private final EPUBProfile epubProfile;
}
