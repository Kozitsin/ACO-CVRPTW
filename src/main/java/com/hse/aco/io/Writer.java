package com.hse.aco.io;

import com.hse.aco.algo.Context;
import com.hse.aco.entity.Customer;
import com.hse.aco.entity.Route;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.List;

public class Writer {
    private static String buildContent(Context context, Route r) {
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
        return sb.toString();
    }

    private static double getCurrentTime(Context context, int last, int current, double currentTime) {
        currentTime += context.distance[last][current];
        Customer c = context.customers.get(current);
        if (c.readyTime > currentTime) {
            currentTime += c.readyTime - currentTime;
        }
        return currentTime;
    }
    public static void write(Context context) {
        try (FileOutputStream outputStream = new FileOutputStream(context.instanceName + ".sol");
             OutputStreamWriter streamWriter = new OutputStreamWriter(outputStream, Charset.defaultCharset());
             BufferedWriter writer = new BufferedWriter(streamWriter)) {
            List<Route> routes = context.best.routes;
            for (Route r : routes) {
                writer.write(buildContent(context, r));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
