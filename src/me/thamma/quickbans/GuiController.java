package me.thamma.quickbans;

import javafx.fxml.FXML ;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class GuiController implements Initializable {

    @FXML
    private Pane mainPane;

    @FXML
    private Label scrambleLabel, timerLabel;

    @FXML
    private Label sessionLabel1, sessionLabel2;

    @FXML
    private Button buttonRight, buttonLeft;

    @FXML
    private ListView<String> listView;

    @FXML
    private Label ao5Label, ao12Label, ao100Label, bao5Label, bao12Label, bao100Label, meanLabel, medianLabel, standartDerivationLabel, bestLabel, worstLabel, solvesLabel;

    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {

    }

}

