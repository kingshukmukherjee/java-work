package com.rabobank.statement.model;

/**
 * POJO class for csv input statement.
 *
 *
 * @author  Kingshuk Mukherjee - 233303
 * @version 1.0
 * @since   20/09/2019
 */

public class CustomerStatementModel {

    private String reference;
    private String accountNumber;
    private String description;
    private String startBalance;
    private String mutation;
    private String endBalance;

    public CustomerStatementModel() {
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartBalance() {
        return startBalance;
    }

    public void setStartBalance(String startBalance) {
        this.startBalance = startBalance;
    }

    public String getMutation() {
        return mutation;
    }

    public void setMutation(String mutation) {
        this.mutation = mutation;
    }

    public String getEndBalance() {
        return endBalance;
    }

    public void setEndBalance(String endBalance) {
        this.endBalance = endBalance;
    }
}
