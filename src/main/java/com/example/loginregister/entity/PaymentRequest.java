package com.example.loginregister.entity;

/**
 * This class represents the payment request payload used to create a payment order in Razorpay.
 */
public class PaymentRequest {

    private double amount;  
    private String currency;
    private String receipt; 

    public PaymentRequest(double amount, String currency, String receipt) {
        this.amount = amount;
        this.currency = currency;
        this.receipt = receipt;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        // Validate the currency, here we are assuming "INR" is the only valid currency
        if (!"INR".equals(currency)) {
            throw new IllegalArgumentException("Invalid currency. Only INR is supported.");
        }
        this.currency = currency;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        if (receipt == null || receipt.trim().isEmpty()) {
            throw new IllegalArgumentException("Receipt ID cannot be null or empty.");
        }
        this.receipt = receipt;
    }

    public void convertAmountToPaise() {
        if (currency.equals("INR")) {
            this.amount = this.amount * 100; 
        }
    }

    @Override
    public String toString() {
        return "PaymentRequest [amount=" + amount + ", currency=" + currency + ", receipt=" + receipt + "]";
    }
}
