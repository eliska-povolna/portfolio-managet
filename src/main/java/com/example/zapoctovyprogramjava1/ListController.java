package com.example.zapoctovyprogramjava1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for FXML of the "Your portfolio" window
 */
public class ListController {
    /**
     * listview of the portfolio
     */
    @FXML
    public ListView<String> listview;
    /**
     * name of the investment to remove
     */
    @FXML
    public TextField name;
    /**
     * submit remove button
     */
    @FXML
    public Button submit;

    /**
     * Removes an investment specified by user in a text field from the portfolio.
     * If successful, shows a success message. It succeeds even if the investment is not found in the portfolio, then it does nothing.
     * If not successful, shows and error message.
     */
    public void remove() {
        try {
            ManagerApplication.removeFromPortfolio(name.getText());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Investment removed");
            alert.setContentText("Investment was removed from your portfolio if it existed");
            alert.show();
            show();
            name.clear();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid investment");
            alert.setContentText("Investment could not be removed");
            alert.show();
        }
    }

    /**
     * Updates values and shows the portfolio as a listview.
     */
    public void show() {
        try {
            List<String> namelist = ManagerApplication.getPortfolioValues().stream().map(Investment::getInfo).collect(Collectors.toList());
            ObservableList<String> observableList = FXCollections.observableArrayList();
            observableList.setAll(namelist);
            listview.setItems(observableList);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An error occured");
            alert.setContentText("One of possible reasons might be your internet connection or wrong format of portfolio.json.");
            alert.show();
        }
    }
}

