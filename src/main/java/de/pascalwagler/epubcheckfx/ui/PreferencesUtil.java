package de.pascalwagler.epubcheckfx.ui;

import de.pascalwagler.epubcheckfx.App;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PreferencesUtil {

    /**
     * Restores the last selected value of the comboBox from Preferences or applies a defaultValue.
     * This method also adds a Listener to save the last selected value in the Preferences under preferencesKey.
     */
    public static <SomeComboBox extends javafx.scene.control.ComboBox<SomeEnum>, SomeEnum extends Enum<?>> void syncWithPreferences(
            SomeComboBox someComboBox, SomeEnum defaultValue, String preferencesKey) {

        // select the previously saved option (or the default value)
        String selectedSeverity = App.userPreferences.get(preferencesKey, defaultValue.name());
        SomeEnum selected = (SomeEnum) SomeEnum.valueOf(defaultValue.getClass(), selectedSeverity);
        someComboBox.getSelectionModel().select(selected);

        // save selection in preferences
        someComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) ->
                App.userPreferences.put(preferencesKey, newValue.name()));
    }
}
