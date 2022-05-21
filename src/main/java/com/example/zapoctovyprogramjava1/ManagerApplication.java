package com.example.zapoctovyprogramjava1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * The main application
 */
public class ManagerApplication extends Application {
    public FXMLLoader fxmlLoader = new FXMLLoader(ManagerApplication.class.getResource("main_page.fxml"));

    /**
     * Starts the whole application
     * @param stage of JavaFX
     * @throws IOException if launch was not successful
     */
    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Portfolio manager");
        stage.setScene(scene);
        stage.show();
        MainPageController controller = fxmlLoader.getController();
        controller.refresh();
    }

    /**
     * Returns the value of an investment
     * @param i the investment we need the value of
     * @return the value of an investment on the market from Yahoo finance, Cryptocompare or calculated from a fixed annual rate
     * @throws IOException if it is unable to read from URL
     * @throws ParseException if it is unable to parse JSON from URL
     */
    public static double getValue(Investment i) throws IOException, ParseException {
        double value = 0;
        if (i.type.equals("Fixed rate")) {
            long diff = i.date.until(LocalDate.now(), ChronoUnit.DAYS);
            value = i.amount * i.price * (1 + Double.parseDouble(i.specific_info) * (diff / 365.0));
        }
        if (i.type.equals("Stock")) {
            String url = "https://query1.finance.yahoo.com/v7/finance/quote?lang=en-US&region=US&corsDomain=finance.yahoo.com&symbols=AAPL"
                    + "&limit=1";
            InputStream is = new URL(url).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            JSONParser jsonparser = new JSONParser();
            JSONObject json = (JSONObject) jsonparser.parse(sb.toString());
            JSONArray arr = (JSONArray) ((JSONObject) json.get("quoteResponse")).get("result");
            value = (double) ((JSONObject) arr.get(0)).get("ask");
        }
        if (i.type.equals("Crypto")) {
            String url = "https://min-api.cryptocompare.com/data/price?fsym="
                    + i.specific_info
                    + "&tsyms=USD";
            InputStream is = new URL(url).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                if (Character.isDigit((char) cp) | (char) cp == '.') sb.append((char) cp);
            }
            value = Double.parseDouble(sb.toString()) * i.amount;
        }
        return value;
    }

    /**
     * Returns a JSONObject of the portfolio from portfolio.json
     * @return JSONObject of the portfolio from portfolio.json
     */
    public static JSONObject getPortfolioJSON() {
        JSONParser parser = new JSONParser();
        JSONObject portfolio;
        try {
            portfolio = (JSONObject) parser.parse(new FileReader("portfolio.json"));
        } catch (IOException | ParseException e) {
            portfolio = new JSONObject();
        }
        return portfolio;
    }

    /**
     * Returns an arraylist of the portfolio to be able to process it further easily.
     * If it is unable to read from portfolio file, returns an empty list.
     * @return an arraylist of Investment objects from portfolio.json
     */
    public static ArrayList<Investment> getPortfolioValues() {
        JSONParser parser = new JSONParser();
        JSONObject portfolio;
        ArrayList<Investment> portfolioList = new ArrayList<>();
        try {
            portfolio = (JSONObject) parser.parse(new FileReader("portfolio.json"));
            Set investments = portfolio.keySet();
            for (Object inv : investments) {
                String n = ((JSONObject) portfolio.get(inv)).get("name").toString();
                String t = ((JSONObject) portfolio.get(inv)).get("type").toString();
                LocalDate d = LocalDate.parse(((JSONObject) portfolio.get(inv)).get("date").toString());
                double p = Double.parseDouble(((JSONObject) portfolio.get(inv)).get("price").toString());
                double a = Double.parseDouble(((JSONObject) portfolio.get(inv)).get("amount").toString());
                String s = ((JSONObject) portfolio.get(inv)).get("specific_info").toString();
                Investment i = new Investment(n, t, d, p, a, s);
                i.value = getValue(i);
                portfolioList.add(i);
            }
        } catch (IOException | ParseException e) {
            // just return an empty list
        }
        return portfolioList;
    }

    /**
     * Adds a new investment to the portfolio, saves it to portfolio.json.
     * @param i an Investment object to add to the portfolio
     * @throws IOException if it is unable to write to the file
     */
    public static void addToPortfolio(Investment i) throws IOException {
        JSONObject portfolio = getPortfolioJSON();
        JSONObject investmentObject = new JSONObject();
        if (i.date.isAfter(LocalDate.now())) throw new InvalidParameterException();
        investmentObject.put("name", i.name);
        investmentObject.put("date", i.date.toString());
        investmentObject.put("type", i.type);
        investmentObject.put("amount", i.amount);
        investmentObject.put("specific_info", i.specific_info);
        investmentObject.put("price", i.price);
        portfolio.put(i.name, investmentObject);
        try (FileWriter file = new FileWriter("portfolio.json")) {
            file.write(portfolio.toJSONString());
            file.flush();
        }
    }

    /**
     * Removes an investment specified by name from the portfolio
     * @param name the name property of an Investment object
     */
    public static void removeFromPortfolio(String name) {
        JSONObject portfolio = getPortfolioJSON();
        portfolio.remove(name);
        try (FileWriter file = new FileWriter("portfolio.json")) {
            file.write(portfolio.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the value of the whole portfolio according to its market valuation
     * @return the value of the whole portfolio from portfolio.json
     */
    public static double getPortfolioValue() {
        double value = 0.0;
        List<Investment> portfolio = getPortfolioValues();
        for (Investment inv : portfolio) {
            value += inv.value;
        }
        return value;
    }

    /**
     * Returns the return of the portfolio in %
     * @return the return of the portfolio in %
     */
    public static double getPortfolioGrowth() {
        double value = getPortfolioValue();
        double cost = 0.0;
        List<Investment> portfolio = getPortfolioValues();
        for (Investment inv : portfolio) {
            cost += inv.price;
        }
        return (value / cost - 1) * 100;
    }

    public static void main(String[] args) {
        launch();
    }
}