package com.hse.aco.algo;

import com.hse.aco.entity.Customer;
import com.hse.aco.entity.Solution;
import com.hse.aco.entity.Truck;
import com.hse.aco.entity.Tuple;

import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

import static com.hse.aco.algo.Utils.*;

public class ACO {
    private Context context;

    public ACO(Context context) {
        this.context = context;
    }

    public void run() {
        Solution s = new Solution();
        Truck t = new Truck(0, 0.0, context.getDepot());
        Map<Integer, Customer> available = findEligibleCustomers(t);

        while (!available.isEmpty()) {
            Customer next = getNext(t, available);
            localUpdate(t, next);
        }
        returnToDepot(t);
        twoOpt(); // we do not apply it

        if (s.total < context.best.total) {
            context.best = s; // copy
        }
        updatePheromonesGlobal();
    }

    private Map<Integer, Customer> findEligibleCustomers(Truck truck) {
        return context.customers.entrySet().stream()
                .filter(NOT_VISITED(truck))
                .filter(WEIGHT_CONSTRAINT(truck, context))
                .filter(BE_IN_TIME_TO_DOCK(truck, context))
                .filter(TIME_CONSTRAINT(truck, context))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Customer getNext(Truck t, Map<Integer, Customer> available) {
        final int lastVisited = t.lastVisited();

        if (MetaParams.q0 <= Math.random()) {
            return available.values().stream()
                    .max(Comparator.comparing(NEXT_CUSTOMER_RULE(lastVisited, context))).get();
        } else {
            double rand = Math.random();
            double denominator = available.values().stream()
                    .mapToDouble(c -> context.calculateCost(lastVisited, c.customerId)).sum();
            return available.values().stream()
                    .map(c -> new Tuple(c.customerId, context.calculateCost(lastVisited, c.customerId) / denominator))
                    .filter(x -> rand < x.value)
                    .sorted(Comparator.comparingDouble(x -> x.value))
                    .findFirst()
                    .map(x -> available.get(x.id)).get();
        }
    }

    private void returnToDepot(Truck t) {
        localUpdate(t, context.getDepot());
    }

    private void updateTruckInfo(Truck t, Customer c, int lastCustomerId) {
        t.visited.add(c.customerId);
        t.usedCapacity += c.demand;
        t.distanceTravelled += context.distance[lastCustomerId][c.customerId];
        t.currentTime += context.distance[lastCustomerId][c.customerId] + (c.readyTime - t.currentTime) + c.serviceTime;
    }

    private void localUpdate(Truck t, Customer c) {
        int lastCustomerId = t.lastVisited();
        updateTruckInfo(t, c, lastCustomerId);
        updatePheromonesLocal(c, lastCustomerId);
    }

    private void updatePheromonesLocal(Customer c, int lastCustomerId) {
        context.updatePheromones(lastCustomerId, c.customerId);
    }

    private void twoOpt() {

    }

    private void updatePheromonesGlobal() {

    }
}
