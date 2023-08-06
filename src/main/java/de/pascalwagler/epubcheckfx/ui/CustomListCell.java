package de.pascalwagler.epubcheckfx.ui;

import atlantafx.base.theme.Styles;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import de.pascalwagler.epubcheckfx.model.CheckMessage;

import java.util.ResourceBundle;

public class CustomListCell extends ListCell<CheckMessage> {

    private final VBox iconPane;

    private final Label id;
    private final Label message;
    private final Label path;
    private final ResourceBundle resourceBundle;
    private final ListView listView;
    private HBox customListItem;

    public CustomListCell(ResourceBundle resourceBundle, ListView listView) {
        super();

        this.resourceBundle = resourceBundle;
        this.listView = listView;

        customListItem = new HBox();
        customListItem.setSpacing(10);

        iconPane = new VBox();
        VBox vBox = new VBox();

        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10, 0, 10, 0));

        id = new Label();
        id.getStyleClass().add(Styles.TITLE_4);
        message = new Label();
        message.setWrapText(true);
        path = new Label();
        path.getStyleClass().add(Styles.TEXT_SUBTLE);
        vBox.getChildren().addAll(id, message, path);

        HBox.setHgrow(iconPane, Priority.ALWAYS);
        customListItem.getChildren().add(iconPane);

        HBox.setHgrow(vBox, Priority.ALWAYS);
        customListItem.getChildren().add(vBox);
    }

    @Override
    public void updateItem(CheckMessage checkMessage, boolean empty) {
        super.updateItem(checkMessage, empty);
        if (!empty && checkMessage != null) {
            Text text = new Text();
            text.wrappingWidthProperty().bind(listView.widthProperty().subtract(15));
            text.setText(checkMessage.getMessage());

            setPrefWidth(0);
            setGraphic(text);
        } else {
            setText(null);
            setGraphic(null);
        }
    }

    /*public void apply(CheckMessage checkMessage) {

        iconPane.getChildren().clear();
        //FontIcon severityIcon = MainWindowController.createSeverityIcon(checkMessage.getSeverity());
        severityIcon.getStyleClass().add("icon-large");
        iconPane.setAlignment(Pos.CENTER);
        iconPane.setSpacing(10);
        iconPane.getChildren().add(severityIcon);

        this.id.setText(checkMessage.getMessageId().toString());
        this.message.setText(checkMessage.getMessage() + "asdjhkasd aksjdhaksjdh akjshdjasd akjsdha kd asdjhkasd aksjdhaksjdh akjshdjasd akjsdha kd asdjhkasd aksjdhaksjdh akjshdjasd akjsdha kd asdjhkasd aksjdhaksjdh akjshdjasd akjsdha kd asdjhkasd aksjdhaksjdh akjshdjasd akjsdha kd asdjhkasd aksjdhaksjdh akjshdjasd akjsdha kd asdjhkasd aksjdhaksjdh akjshdjasd akjsdha kd");
        this.path.setText(checkMessage.getPath());
    }*/
}
