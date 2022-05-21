package com.example.zapoctovyprogramjava1;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for FXML of the main window
 */
public class MainPageController {
    /**
     * displays portfolio value
     */
    @FXML
    public Label portfolioValue;
    /**
     * displays portfolio revenue as a percentage
     */
    @FXML
    public Label portfolioGrowth;

    /**
     * Opens "Add a new investment" window
     */
    @FXML
    protected void portfolioManagement() {
        FXMLLoader fxmlLoaderP = new FXMLLoader(ManagerApplication.class.getResource("portfolio.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoaderP.load(), 420, 440);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Something went wrong");
            alert.show();
        }
        Stage stage = new Stage();
        stage.setTitle("Add a new investment");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Opens "Your portfolio" window
     */
    @FXML
    protected void portfolioShow() {
        FXMLLoader fxmlLoaderS = new FXMLLoader(ManagerApplication.class.getResource("show.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoaderS.load(), 420, 440);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Something went wrong");
            alert.show();
        }
        Stage stage = new Stage();
        stage.setTitle("Your portfolio");
        stage.setScene(scene);
        stage.show();
        ListController controller = fxmlLoaderS.getController();
        controller.show();
    }

    /**
     * Refreshes portfolio value and return on the main page.
     */
    @FXML
    protected void refresh() {
        double value = ManagerApplication.getPortfolioValue();
        portfolioValue.setText(String.format("Value of your portfolio: %.2f USD", value));
        double growth = ManagerApplication.getPortfolioGrowth();
        portfolioGrowth.setText(String.format("Return of your portfolio: %.4f %%", growth));
    }
}