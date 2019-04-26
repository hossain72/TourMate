package com.remon.tourmate;

public class TourExpense {

    String id;
    String expenseName, expense;

    public TourExpense() {
    }

    public TourExpense(String expenseName, String expense) {
        this.expenseName = expenseName;
        this.expense = expense;
    }

    public TourExpense(String id, String expenseName, String expense) {
        this.id = id;
        this.expenseName = expenseName;
        this.expense = expense;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public String getExpense() {
        return expense;
    }
}
