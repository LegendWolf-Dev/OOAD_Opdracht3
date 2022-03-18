package main.userInterfaceLaag;

import com.sun.tools.javac.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import main.domeinLaag.Luchthaven;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.IntStream;

public class BoekVluchtController implements Initializable {
    @FXML
    private ComboBox vertrekPuntBox;
    @FXML
    private ComboBox bestemmingBox;
    @FXML
    private ComboBox vertrekTijdDatumBox;
    @FXML
    private ComboBox vertrekTijdUurBox;
    @FXML
    private ComboBox vertrekTijdMinuutBox;
    @FXML
    private Button confirmBoekingButton;
    @FXML
    private Button cancelBoekingButton;
    @FXML
    private Label boekingInformatie;
    @FXML
    private TextField voornaamTextField;
    @FXML
    private TextField achternaamTextField;
    @FXML
    private TextField straatTextField;
    @FXML
    private TextField huisNummerTextField;
    @FXML
    private TextField woonPlaatsTextField;
    @FXML
    private ChoiceBox klasseBox;
    @FXML
    private Button confirmRegisterenKlant;
    @FXML
    private Button cancelRegisterenKlant;

    private ArrayList<String> luchthavensNamen;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getDataLists();

        fillVertrekPuntBox();
        fillBestemmingBox();
        fillVertrekTijdUurBox();
        fillVertrekTijdMinuutBox();
    }

    private void getDataLists() {
        luchthavensNamen = new ArrayList<>();

        TreeMap<String, Luchthaven> luchthavens = Luchthaven.geefAlle();
        Set<String> lSet = luchthavens.keySet();
        for (String lNaam : lSet) {
            Luchthaven luchthaven = (Luchthaven) luchthavens.get(lNaam);

            this.luchthavensNamen.add(luchthaven.geefNaam());
        }
    }

    private void fillVertrekPuntBox() {
        ObservableList<String> list = FXCollections.observableArrayList(this.luchthavensNamen);
        this.vertrekPuntBox.setItems(list);
    }

    private void fillBestemmingBox() {
        ObservableList<String> list = FXCollections.observableArrayList(this.luchthavensNamen);
        this.bestemmingBox.setItems(list);
    }

    private void fillVertrekTijdUurBox() {
        IntStream.rangeClosed(0, 23).boxed().forEach(this.vertrekTijdUurBox.getItems()::add);
    }

    private void fillVertrekTijdMinuutBox() {
        IntStream.rangeClosed(0, 59).boxed().forEach(this.vertrekTijdMinuutBox.getItems()::add);
    }
}
