package me.thamma.quickbans;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import org.xml.sax.SAXException;

import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GuiController implements Initializable {

    @FXML
    private ComboBox regionChoice;

    @FXML
    private Button runButton;

    @FXML
    private TextField summonerField, amountField;

    @FXML
    private RadioButton personalRadio, leagueRadio;

    private String summoner, region, mode;
    private int amount;

    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        ObservableList<String> l = FXCollections.observableArrayList();
        summonerField.textProperty().addListener(e -> summoner = summonerField.getText());
        amountField.textProperty().addListener(e ->
                amount = Integer.parseInt(amountField.getText()));
        region = "EUW";
        mode = "PERSONAL";
        l.addAll(("BR\n" +
                "EUNE\n" +
                "EUW\n" +
                "KR\n" +
                "LAN\n" +
                "LAS\n" +
                "NA\n" +
                "OCE\n" +
                "RU\n" +
                "TR").split("\n"));
        regionChoice.setItems(l);
        regionChoice.getSelectionModel().select(6);
        runButton.setOnAction(e -> {
                    System.out.println(summoner + "" + region + "" + mode + "" + amount);
                    Main.loadQuickBans(summoner, region, mode, amount);
                }
        );
    }


}

