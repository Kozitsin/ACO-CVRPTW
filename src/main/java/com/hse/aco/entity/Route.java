package com.hse.aco.entity;

import java.util.ArrayList;
import java.util.List;

public class Route {
    public List<Integer> customers;
    public double cost;

    public Route(List<Integer> customers, double cost) {
        this.customers = customers;
        this.cost = cost;
    }

    public Route copy() {
        return new Route(new ArrayList<>(customers), cost);
    }
}
