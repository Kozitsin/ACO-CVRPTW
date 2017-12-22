package com.hse.aco.entity;

public class Customer {
    public int customerId;
    public double x;
    public double y;
    public int demand;

    public double readyTime;
    public double dueDate;
    public double serviceTime;

    public Customer(int customerId, double x, double y, int demand, double readyTime, double dueDate, double serviceTime) {
        this.customerId = customerId;
        this.x = x;
        this.y = y;
        this.demand = demand;
        this.readyTime = readyTime;
        this.dueDate = dueDate;
        this.serviceTime = serviceTime;
    }

    public Customer(Customer customer) {
        this.customerId = customer.customerId;
        this.x = customer.x;
        this.y = customer.y;
        this.demand = customer.demand;
        this.readyTime = customer.readyTime;
        this.dueDate = customer.dueDate;
        this.serviceTime = customer.serviceTime;
    }
}
