package de.pascalwagler.epubcheckfx.model;

import com.adobe.epubcheck.api.EPUBProfile;
import de.pascalwagler.epubcheckfx.ui.Translatable;
import lombok.Getter;

@Getter
public enum EpubProfile implements Translatable {
    DEFAULT("epub_profile.default", EPUBProfile.DEFAULT),
    IDX("epub_profile.idx", EPUBProfile.IDX),
    DICT("epub_profile.dict", EPUBProfile.DICT),
    EDUPUB("epub_profile.edupub", EPUBProfile.EDUPUB),
    PREVIEW("epub_profile.preview", EPUBProfile.PREVIEW);

    EpubProfile(String i18nKey, EPUBProfile epubcheckEpubProfile) {
        this.i18nKey = i18nKey;
        this.epubcheckEpubProfile = epubcheckEpubProfile;
    }

    private final String i18nKey;
    private final EPUBProfile epubcheckEpubProfile;
}
