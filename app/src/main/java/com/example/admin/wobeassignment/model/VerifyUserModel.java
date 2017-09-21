package com.example.admin.wobeassignment.model;

import java.math.BigInteger;

/**
 * Created by Admin on 21-09-2017.
 */

public class VerifyUserModel {

    private String statusMessage;
    private String returnStatus;
    private BigInteger CUSTOMER_ID;

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getReturnStatus() {
        return returnStatus;
    }

    public void setReturnStatus(String returnStatus) {
        this.returnStatus = returnStatus;
    }

    public BigInteger getCUSTOMER_ID() {
        return CUSTOMER_ID;
    }

    public void setCUSTOMER_ID(BigInteger CUSTOMER_ID) {
        this.CUSTOMER_ID = CUSTOMER_ID;
    }
}
