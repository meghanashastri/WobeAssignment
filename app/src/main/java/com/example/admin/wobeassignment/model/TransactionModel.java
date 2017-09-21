package com.example.admin.wobeassignment.model;

import java.math.BigInteger;

/**
 * Created by Admin on 21-09-2017.
 */

public class TransactionModel {
    private BigInteger transactionId;
    private BigInteger fromCustomerID;
    private String fromFirstName;
    private String fromLastName;
    private BigInteger toCustomerID;
    private String toFirstName;
    private String toLastName;
    private BigInteger credits;
    private String transactionDate;
    private String description;
    private String noteToSelf;

    public BigInteger getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(BigInteger transactionId) {
        this.transactionId = transactionId;
    }

    public BigInteger getFromCustomerID() {
        return fromCustomerID;
    }

    public void setFromCustomerID(BigInteger fromCustomerID) {
        this.fromCustomerID = fromCustomerID;
    }

    public String getFromFirstName() {
        return fromFirstName;
    }

    public void setFromFirstName(String fromFirstName) {
        this.fromFirstName = fromFirstName;
    }

    public String getFromLastName() {
        return fromLastName;
    }

    public void setFromLastName(String fromLastName) {
        this.fromLastName = fromLastName;
    }

    public BigInteger getToCustomerID() {
        return toCustomerID;
    }

    public void setToCustomerID(BigInteger toCustomerID) {
        this.toCustomerID = toCustomerID;
    }

    public String getToFirstName() {
        return toFirstName;
    }

    public void setToFirstName(String toFirstName) {
        this.toFirstName = toFirstName;
    }

    public String getToLastName() {
        return toLastName;
    }

    public void setToLastName(String toLastName) {
        this.toLastName = toLastName;
    }

    public BigInteger getCredits() {
        return credits;
    }

    public void setCredits(BigInteger credits) {
        this.credits = credits;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNoteToSelf() {
        return noteToSelf;
    }

    public void setNoteToSelf(String noteToSelf) {
        this.noteToSelf = noteToSelf;
    }
}
