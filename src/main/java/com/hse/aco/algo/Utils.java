package com.hse.aco.algo;

import com.hse.aco.entity.Customer;
import com.hse.aco.entity.Truck;

import java.util.Map;
import java.util.function.Predicate;

public class Utils {
    public static Predicate<Map.Entry<Integer, Customer>> NOT_VISITED(final Truck t) {
        return e -> !t.visited.contains(e.getKey());
    }

    public static Predicate<Map.Entry<Integer, Customer>> WEIGHT_CONSTRAINT(final Truck t, final Context context) {
        return e -> t.usedCapacity + e.getValue().demand < context.MAX_TRUCK_CAPACITY;
    }

    public static Predicate<Map.Entry<Integer, Customer>> BE_IN_TIME_TO_DOCK(final Truck t, final Context context) {
        return e -> t.currentTime + context.distance[t.lastVisited()][e.getKey()]
                + e.getValue().serviceTime + context.distance[e.getKey()][0] < context.getDepot().dueDate;
    }

    public static Predicate<Map.Entry<Integer, Customer>> TIME_CONSTRAINT(final Truck t, final Context context) {
        return e -> t.currentTime + context.distance[t.lastVisited()][e.getKey()] < e.getValue().dueDate;
    }
}
