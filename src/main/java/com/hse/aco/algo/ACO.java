package com.hse.aco.algo;

import com.hse.aco.entity.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

import static com.hse.aco.algo.Utils.*;

public class ACO {
    private static final Logger logger = LoggerFactory.getLogger(ACO.class);
    private Context context;

    public ACO(Context context) {
        this.context = context;
    }

    public Solution run() {
        for (int i = 0, iteration = 0; i < MetaParams.MAX_ITER_WO_IMPROVEMENT; i++, iteration++) {
            if (iteration % 100 == 0) {
                logger.info("Iteration {} started...", iteration);
            }
            Solution s = new Solution();
            Set<Integer> used = new HashSet<>();
            used.add(context.getDepot().customerId);

            while (s.routes.size() < context.MAX_TRUCK_NUMBER && used.size() != context.customers.size()) {
                Route r = buildSingleRoute(used);
                s.routes.add(r);
                s.total += r.cost;
            }

            updatePheromonesGlobal(s);

            if (s.total < context.best.total) {
                s.copyTo(context.best);
                logger.info("Found new best solution: {}", context.best.total);
                i = 0;
            }
        }
        logger.info("Found optimum: {}", context.best.total);
        return context.best;
    }

    private Route buildSingleRoute(Set<Integer> used) {
        Truck t = new Truck(0, 0.0, context.getDepot());
        Map<Integer, Customer> available;

        while (true) {
            available = findEligibleCustomers(t, used);
            if (MapUtils.isEmpty(available)) {
                break;
            }
            Customer next = getNext(t, available);
            used.add(next.customerId);
            localUpdate(t, next);
        }
        returnToDepot(t);
        return new Route(t.visited, t.distanceTravelled);
    }

    private Map<Integer, Customer> findEligibleCustomers(Truck truck, Set<Integer> used) {
        return context.customers.entrySet().stream()
                .filter(NOT_VISITED(used))
                .filter(WEIGHT_CONSTRAINT(truck, context))
                .filter(BE_IN_TIME_TO_DOCK(truck, context))
                .filter(TIME_CONSTRAINT(truck, context))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Customer getNext(Truck t, Map<Integer, Customer> available) {
        final int lastVisited = t.lastVisited();

        logger.debug("Last Visited Customer: {}", lastVisited);

        if (MetaParams.q0 <= Math.random()) {
            return available.values().stream()
                    .max(Comparator.comparing(NEXT_CUSTOMER_RULE(lastVisited, context))).get();
        } else {
            double rand = Math.random();
            double denominator = available.values().stream()
                    .mapToDouble(c -> context.calculateCost(lastVisited, c.customerId)).sum();
            List<Tuple> tmp = available.values().stream()
                    .map(c -> new Tuple(c.customerId, context.calculateCost(lastVisited, c.customerId) / denominator))
                    .sorted(Comparator.comparingDouble(x -> x.value))
                    .collect(Collectors.toList());

            Tuple result = tmp.get(tmp.size() - 1);
            tmp = tmp.stream().filter(x -> rand < x.value).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(tmp)) {
                result = tmp.get(0);
            }
            return available.get(result.id);
        }
    }

    private void returnToDepot(Truck t) {
        localUpdate(t, context.getDepot());
    }

    private void updateTruckInfo(Truck t, Customer c, int lastCustomerId) {
        logger.debug("Travelling to {}", c.customerId);
        t.visited.add(c.customerId);

        t.usedCapacity += c.demand;
        logger.debug("Used Capacity: {}", t.usedCapacity);

        t.distanceTravelled += context.distance[lastCustomerId][c.customerId];
        logger.debug("Distance Travelled: {}", t.distanceTravelled);

        t.currentTime += context.distance[lastCustomerId][c.customerId];
        if (c.readyTime > t.currentTime) {
            double waitingTime = c.readyTime - t.currentTime;
            logger.debug("Waiting Time: {}", waitingTime);
            t.currentTime += waitingTime;
        }
        t.currentTime += c.serviceTime;
    }

    private void localUpdate(Truck t, Customer c) {
        int lastCustomerId = t.lastVisited();
        updateTruckInfo(t, c, lastCustomerId);
        updatePheromonesLocal(c, lastCustomerId);
    }

    private void updatePheromonesLocal(Customer c, int lastCustomerId) {
        context.updatePheromones(lastCustomerId, c.customerId);
    }

    private void updatePheromonesGlobal(Solution s) {
        if (isGlobalUpdateAllowed(s)) {
            double delta = 1 / s.total;
            for (int i = 0; i < context.pheromones.length; i++) {
                for (int j = 0; j < context.pheromones.length; j++) {
                    context.updatePheromones(i, j, delta);
                }
            }
        }
        updateTemperature();
    }

    private boolean isGlobalUpdateAllowed(Solution current) {
        double delta = current.total - context.best.total;
        return delta < 0 || Math.exp(-delta / context.T) > Math.random();
    }

    private void updateTemperature() {
        context.T *= MetaParams.theta;
        logger.debug("Temperature: {}", context.T);
    }
}
