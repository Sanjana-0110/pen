package model;

import java.util.Arrays;

public class OrderCalculator {
    // Method to calculate actual price without GST
	public static double calculateActualPrice(double priceWithGST, double gstPercentage) {
        return priceWithGST / (1 + (gstPercentage / 100));
    }

    // Method to calculate total price for an order
    public static double calculateTotalPrice(double[] pricesWithGST, double[] gstPercentages, int[] quantities, double shippingCharges) {
        // Calculate total order value
        double orderTotal = 0;
        for (int i = 0; i < pricesWithGST.length; i++) {
            orderTotal += pricesWithGST[i] * quantities[i];
            System.out.println("orderTotal:"+orderTotal);
        }

        // Calculate actual prices and GST for each product
        double[] actualPrices = new double[pricesWithGST.length];
        double[] gstAmounts = new double[pricesWithGST.length];
        for (int i = 0; i < pricesWithGST.length; i++) {
            actualPrices[i] = calculateActualPrice(pricesWithGST[i], gstPercentages[i]);
            System.out.println("actualPrices:"+i+":"+actualPrices[i]);

            gstAmounts[i] = pricesWithGST[i] - actualPrices[i];
            System.out.println("gstAmounts:"+i+":"+gstAmounts[i]);

        }

        // Distribute shipping charges and calculate shipping charges with GST for each product
        double[] shippingChargesWithGST = new double[pricesWithGST.length];
        for (int i = 0; i < pricesWithGST.length; i++) {
            double shareOfShippingCharges = ((actualPrices[i] * quantities[i] )/ orderTotal) * shippingCharges;
            System.out.println("shareOfShippingCharges:"+shareOfShippingCharges);

            shippingChargesWithGST[i] = shareOfShippingCharges * (gstAmounts[i] /100.0);
            System.out.println("shippingChargesWithGST:"+i+":"+shippingChargesWithGST[i]);

        }

        // Calculate total price
        double totalPrice = orderTotal;
        for (double charge : shippingChargesWithGST) {
            totalPrice += charge;
            System.out.println("totalPrice:"+totalPrice);
        }

        return totalPrice;
    }

    public static void main(String[] args) {
        // Example data
        double[] pricesWithGST = {600, 900}; // Prices of products including GST
        double[] gstPercentages = {18, 5}; // GST percentages for each product
        int[] quantities = {2, 1}; // Quantities of each product
        double shippingCharges = 100;

        // Calculate total price
        double totalPrice = calculateTotalPrice(pricesWithGST, gstPercentages, quantities, shippingCharges);
        System.out.println("Grand Total: $" + totalPrice);
    }
}
