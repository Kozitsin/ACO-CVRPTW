package com.hse.aco.io;

import com.hse.aco.algo.Context;
import com.hse.aco.entity.Customer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.net.URLDecoder.decode;

public class Reader {

    private static void skip(BufferedReader reader, int n) throws IOException {
        for (int i = 0; i < n; i++)
            reader.readLine();
    }

    public static Context read(String fileName) {
        Context context;

        try (BufferedReader reader = new BufferedReader(new FileReader(new File(decode(fileName, "UTF-8"))))) {
            String instanceName = reader.readLine();
            skip(reader, 3);
            List<String> line = Stream.of(reader.readLine().trim().split(" "))
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());

            int maxTruckNumber = Integer.valueOf(line.get(0));
            int maxTruckCapacity = Integer.valueOf(line.get(1));

            skip(reader, 4);

            List<Customer> customers = new ArrayList<>();

            String current;
            while((current = reader.readLine()) != null) {
                line = Stream.of(current.trim().split(" "))
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.toList());

                int customerId = Integer.valueOf(line.get(0));
                double x = Double.valueOf(line.get(1));
                double y = Double.valueOf(line.get(2));
                int demand = Integer.valueOf(line.get(3));
                double readyTime = Double.valueOf(line.get(4));
                double dueDate = Double.valueOf(line.get(5));
                double serviceTime = Double.valueOf(line.get(6));
                customers.add(new Customer(customerId, x, y, demand, readyTime, dueDate, serviceTime));
            }
            context = new Context(instanceName, customers, maxTruckCapacity, maxTruckNumber);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return context;
    }
}


