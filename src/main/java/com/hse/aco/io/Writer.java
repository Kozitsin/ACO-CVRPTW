package com.hse.aco.io;

import com.hse.aco.algo.Context;
import com.hse.aco.entity.Customer;
import com.hse.aco.entity.Route;

import java.util.List;

public class Writer {
    public static void write(Context context) {
        List<Route> routes = context.best.routes;
        for (Route r : routes) {
            StringBuilder sb = new StringBuilder();
            List<Integer> customers = r.customers;
            double currentTime = 0.0;

            sb.append(0).append(" ").append(0).append(" ");

            for (int i = 1; i < customers.size(); i++) {
                int last = customers.get(i-1);
                int current = customers.get(i);
                currentTime = getCurrentTime(context, last, current, currentTime);
                sb.append(current).append(" ").append(currentTime).append(" ");
                currentTime += context.customers.get(current).serviceTime;
            }

            System.out.println(sb.toString());
        }
    }

    private static double getCurrentTime(Context context, int last, int current, double currentTime) {
        currentTime += context.distance[last][current];
        Customer c = context.customers.get(current);
        if (c.readyTime > currentTime) {
            currentTime += c.readyTime - currentTime;
        }
        return currentTime;
    }
}
