package com.example.zapoctovyprogramjava1;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

/**
 * Controller for FXML of the "Add a new investment" window
 */
public class PortfolioController {
    /**
     * date of purchase selection
     */
    @FXML
    public DatePicker selectdate;
    /**
     * type of investment selection
     */
    @FXML
    public ChoiceBox<String> typeselect;
    /**
     * enter additional information about the investment
     */
    @FXML
    public TextField selectinfo;
    /**
     * enter buy price
     */
    @FXML
    public TextField selectprice;
    /**
     * enter amount bought
     */
    @FXML
    public TextField selectamount;
    /**
     * enter name of the investmet
     */
    @FXML
    public TextField selectname;

    /**
     * A function that adds a new investment to the portfolio and writes it into portfolio.json.
     * The investment characteristics are specified by the user in "Add a new investment" window.
     * If there are any exceptions during the process, it shows an error message. If there are none, shows a success message.
     */
    @FXML
    protected void addInvestment() {
        try {
            Investment i = new Investment(
                    selectname.getText(),
                    typeselect.getValue(),
                    selectdate.getValue(),
                    Double.parseDouble(selectprice.getText()),
                    Double.parseDouble(selectamount.getText()),
                    selectinfo.getText());
            ManagerApplication.addToPortfolio(i);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Investment added");
            alert.setContentText("Investment was successfully added to your portfolio");
            alert.show();
            selectamount.clear();
            selectprice.clear();
            selectinfo.clear();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid investment");
            alert.setContentText("Investment could not be added");
            alert.show();
        }
    }
}
