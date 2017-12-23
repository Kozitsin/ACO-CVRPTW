package com.hse.aco.algo;

import com.hse.aco.entity.Customer;
import com.hse.aco.entity.Truck;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

@SuppressWarnings("WeakerAccess")
public class Utils {
    public static Predicate<Map.Entry<Integer, Customer>> NOT_VISITED(final Set<Integer> used) {
        return e -> !used.contains(e.getKey());
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

    public static Function<Customer, Double> NEXT_CUSTOMER_RULE(final int i, final Context context) {
        return c -> context.calculateCost(i, c.customerId);
    }
}
