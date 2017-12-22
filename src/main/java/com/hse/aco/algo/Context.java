package com.hse.aco.algo;

import com.hse.aco.entity.Customer;
import com.hse.aco.entity.Solution;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.Math.*;
import static java.lang.Math.pow;

@SuppressWarnings("WeakerAccess")
public class Context {
    public Map<Integer, Customer> customers = new HashMap<>();
    public double[][] distance;
    public double[][] savings;
    public double[][] inverse;
    public double[][] pheromones;

    public final int MAX_TRUCK_CAPACITY;
    public final int MAX_TRUCK_NUMBER;

    Solution best = new Solution();

    public Context(List<Customer> customers, int maxTruckCapacity, int maxTruckNumber) {
        this.customers = customers.stream().collect(Collectors.toMap(c -> c.customerId, c -> c ));
        MAX_TRUCK_CAPACITY = maxTruckCapacity;
        MAX_TRUCK_NUMBER = maxTruckNumber;
        calculateDistances(customers);
        calculateSavings();
        initializePheromones();
    }

    public Customer getDepot() {
        return customers.get(0);
    }

    public double calculateCost(int last, int next) {
        return Math.pow(pheromones[last][next], MetaParams.alpha) +
                Math.pow(inverse[last][next], MetaParams.beta) +
                Math.pow(savings[last][next], MetaParams.lambda);
    }

    public void updatePheromones(int last, int next) {
        pheromones[last][next] = (1 - MetaParams.p) * pheromones[last][next] + MetaParams.p * MetaParams.theta0;
    }

    private static double dist(double x1, double x2, double y1, double y2) {
        return sqrt(pow((x1 - x2), 2) + pow((y1 - y2), 2));
    }

    private void calculateDistances(List<Customer> customers) {
        distance = new double[customers.size()][customers.size()];
        inverse = new double[customers.size()][customers.size()];
        for (int i = 0; i < customers.size(); i++) {
            Customer c1 = customers.get(i);
            for (int j = i; j < customers.size(); j++) {
                if (i == j) {
                    distance[i][j] = 0.0;
                    inverse[i][j] = Double.MAX_VALUE;
                } else {
                    Customer c2 = customers.get(j);
                    distance[i][j] = distance[j][i] = dist(c1.x, c2.x, c1.y, c2.y);
                    inverse[i][j] =  inverse[j][i] = 1 / distance[i][j];
                }
            }
        }
    }

    private static double saving(double di0, double d0j, double dij) {
        return di0 + d0j - MetaParams.g * dij + MetaParams.f * Math.abs(di0 - d0j);
    }

    private void calculateSavings() {
        savings = new double[customers.size()][customers.size()];
        for (int i = 0 ; i < savings.length; i++) {
            for (int j = 0; j < savings.length; j++) {
                savings[i][j] = saving(distance[i][0], distance[0][j], distance[i][j]);
            }
        }
    }

    private void initializePheromones() {
        pheromones = new double[customers.size()][customers.size()];
        for (int i = 0; i < pheromones.length; i++)
            for (int j = 0; j < pheromones.length; j++)
                pheromones[i][j] = MetaParams.theta0;
    }
}
