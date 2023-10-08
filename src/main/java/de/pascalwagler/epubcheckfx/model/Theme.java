package de.pascalwagler.epubcheckfx.model;

import atlantafx.base.theme.CupertinoDark;
import atlantafx.base.theme.CupertinoLight;
import atlantafx.base.theme.Dracula;
import atlantafx.base.theme.NordDark;
import atlantafx.base.theme.NordLight;
import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import de.pascalwagler.epubcheckfx.ui.Translatable;
import lombok.Getter;

@Getter
public enum Theme implements Translatable {
    PRIMER(new PrimerLight(), new PrimerDark(), "menu.view.theme.primer"),
    NORD(new NordLight(), new NordDark(), "menu.view.theme.nord"),
    CUPERTINO(new CupertinoLight(), new CupertinoDark(), "menu.view.theme.cupertino"),
    DRACULA(new Dracula(), new Dracula(), "menu.view.theme.dracula");

    Theme(atlantafx.base.theme.Theme atlantaFxLightTheme, atlantafx.base.theme.Theme atlantaFxDarkTheme, String i18nKey) {
        this.atlantaFxLightTheme = atlantaFxLightTheme;
        this.atlantaFxDarkTheme = atlantaFxDarkTheme;
        this.i18nKey = i18nKey;
    }

    private final atlantafx.base.theme.Theme atlantaFxLightTheme;
    private final atlantafx.base.theme.Theme atlantaFxDarkTheme;
    private final String i18nKey;
}
