package de.pascalwagler.epubcheckfx.ui.controller;

import de.pascalwagler.epubcheckfx.model.CheckMessage;
import de.pascalwagler.epubcheckfx.model.Severity;
import de.pascalwagler.epubcheckfx.ui.UiHelper;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.ResourceBundle;

@Slf4j
public class SummaryPanelController implements Initializable {

    private ResourceBundle resourceBundle;

    @FXML
    private HBox summaryPanel;

    Label[] severitySummaryLabels = new Label[Severity.values().length];

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resourceBundle = resources;

        final Severity[] severities = Severity.values();

        for (int i = 0; i < severities.length; i++) {
            Label severitySummaryLabel = new Label("", UiHelper.createSeverityIcon(severities[i]));
            summaryPanel.getChildren().add(severitySummaryLabel);
            severitySummaryLabels[i] = severitySummaryLabel;
        }
    }

    public void setFilteredErrorList(FilteredList<CheckMessage> filteredErrorList) {

        final Severity[] severityValues = Severity.values();
        for (int i = 0; i < severityValues.length; i++) {
            Severity severity = severityValues[i];
            ObservableList<CheckMessage> filteredBySeverity = filteredErrorList.filtered(
                    checkMessage -> checkMessage.getSeverity().equals(severity));

            IntegerBinding listSize = Bindings.size(filteredBySeverity);
            BooleanBinding hasItems = listSize.greaterThan(0);
            severitySummaryLabels[i].visibleProperty().bind(hasItems);
            severitySummaryLabels[i].managedProperty().bind(hasItems);
            severitySummaryLabels[i].textProperty().bind(Bindings.concat(listSize).concat(" ").concat(resourceBundle.getString(severity.getI18nKey())));
        }
    }
}
