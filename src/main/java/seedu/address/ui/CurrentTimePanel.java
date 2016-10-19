package seedu.address.ui;



import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import seedu.address.commons.util.FxViewUtil;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandResult;
import seedu.address.model.person.ReadOnlyTask;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.IncorrectCommandAttemptedEvent;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

/**
 * The CurrentTime Panel of the App.
 */
public class CurrentTimePanel extends UiPart{

    private final Logger logger = LogsCenter.getLogger(CurrentTimePanel.class);
    private static final String FXML = "CurrentTimePanel.fxml";

    private AnchorPane placeHolderPane;
    private AnchorPane currentTimePane;
    private Label time;

    private Logic logic;

    @FXML
    private TextField commandTextField;

    public static CurrentTimePanel load(Stage primaryStage, AnchorPane currentTimePanelPlaceholder, Logic logic) {
        CurrentTimePanel currentTimePanel = UiPartLoader.loadUiPart(primaryStage, currentTimePanelPlaceholder, new CurrentTimePanel());
        currentTimePanel.addToPlaceholder();
        return currentTimePanel;
    }

//    public void configure(ResultDisplay resultDisplay, Logic logic) {
//        this.resultDisplay = resultDisplay;
//        this.logic = logic;
//        registerAsAnEventHandler(this);
//    }

    private void addToPlaceholder() {
        //SplitPane.setResizableWithParent(placeHolderPane, false);
        placeHolderPane.getChildren().add(time);
        FxViewUtil.applyAnchorBoundaryParameters(currentTimePane, 20.0, 20.0, 0.0, 0.0);
        //FxViewUtil.applyAnchorBoundaryParameters(commandTextField, 20.0, 20.0, 0.0, 0.0);
    }

    @Override
    public void setNode(Node node) {
        currentTimePane = (AnchorPane) node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }

    @Override
    public void setPlaceholder(AnchorPane pane) {
        this.placeHolderPane = pane;
    }

}
    