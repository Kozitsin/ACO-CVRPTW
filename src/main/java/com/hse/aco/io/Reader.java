package com.hse.aco.io;

import com.hse.aco.algo.Context;
import com.hse.aco.entity.Customer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static java.net.URLDecoder.decode;

public class Reader {
    public static Context read(String fileName) {
        Context context;

        try (BufferedReader reader = new BufferedReader(new FileReader(new File(decode(fileName, "UTF-8"))))) {
            String[] size = reader.readLine().split(" ");

            int cust_id;
            int XCOORD;
            int YCOORD;
            int DEMAND;
            int RTIME;
            int DDATE;
            int STIME;

            for (int i = 0; i < 4; i++) {
                reader.readLine();
            }
            int maxTruckNumber = Integer.valueOf(size[0]);
            int maxTruckCapacity = Integer.valueOf(size[1]);

            for (int i = 0; i < 4; i++) {
                reader.readLine();
            }

            List<Context> contexts = new ArrayList<>();

            while((reader.readLine() ) != null ) {
                cust_id = Integer.valueOf(size[0]);
                XCOORD = Integer.valueOf(size[1]);
                YCOORD = Integer.valueOf(size[2]);
                DEMAND = Integer.valueOf(size[3]);
                RTIME = Integer.valueOf(size[4]);
                DDATE = Integer.valueOf(size[5]);
                STIME = Integer.valueOf(size[6]);

                contexts.add(cust_id);

            }

            context = new Context(context, maxTruckCapacity, maxTruckNumber);

        }
        catch (IOException ex) {
                return null;
        }
        return context;
    }
}


