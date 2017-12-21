package com.hse.aco.entity;

import java.util.List;
import java.util.stream.Collectors;

public class Truck {
    public int usedCapacity;
    public double distanceTravelled;
    public double currentTime = 0.0;
    public List<Integer> visited;


    public Truck(int usedCapacity, double distanceTravelled, Customer customer) {
        this.usedCapacity = usedCapacity;
        this.distanceTravelled = distanceTravelled;

        if (customer != null) {
            this.visited.add(customer.customerId);
        }
    }

    public void cloneTo(Truck truck) {
        truck.usedCapacity = this.usedCapacity;
        truck.distanceTravelled = this.distanceTravelled;
        truck.visited = this.visited.stream().map(Integer::new).collect(Collectors.toList());
    }

    public int lastVisited() {
        return visited.get(visited.size() - 1);
    }
}
