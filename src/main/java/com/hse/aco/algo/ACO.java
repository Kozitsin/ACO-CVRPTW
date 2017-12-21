package com.hse.aco.algo;

import com.hse.aco.entity.Customer;
import com.hse.aco.entity.Solution;
import com.hse.aco.entity.Truck;

import java.util.Map;
import java.util.stream.Collectors;

import static com.hse.aco.algo.Utils.*;

// TODO: do not forget to update current time with waiting time!!!
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
            Customer next = getNext(available);
            updateTruckInfo(t, next);
            updatePheromonesLocal();
        }
        returnToDepot();
        twoOpt();

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

    private Customer getNext(Map<Integer, Customer> available) {
        return null;
    }

    private void returnToDepot() {

    }

    private void updateTruckInfo(Truck t, Customer c) {
        // increase used capacity
        // add distance
        // add current time
    }

    private void updatePheromonesLocal() {

    }

    private void twoOpt() {

    }

    private void updatePheromonesGlobal() {

    }
}
