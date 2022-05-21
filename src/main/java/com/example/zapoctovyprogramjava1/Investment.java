package com.example.zapoctovyprogramjava1;

import java.time.LocalDate;

/**
 * A general class for all investments. It contains all the important characteristics of each investment.
 */
public class Investment {
    String name;
    String type;
    LocalDate date;
    double price;
    double amount;
    double value;
    String specific_info;

    Investment(String n, String t, LocalDate d, double p, double a, String s) {
        name = n;
        type = t;
        date = d;
        price = p;
        amount = a;
        specific_info = s;
    }

    /**
     * Returns a description of the object to be able to display its characteristics in a list.
     *
     * @return The string used to describe the investment in a portfolio
     */
    public String getInfo() {
        double ret = (value / price - 1) * 100;
        return String.format("%s: %s %s, amount: %.2f value: %.2f, return: %.2f %%", name, type, specific_info, amount, value, ret);
    }
}

