package com.hse.aco;

import com.hse.aco.algo.ACO;
import com.hse.aco.algo.Context;
import com.hse.aco.entity.Solution;
import com.hse.aco.io.Reader;

public class App {
    public static void main(String[] args) {
        String fileName = args[0];
        Context context = Reader.read(fileName);
        ACO search = new ACO(context);
        Solution best = search.run();
        System.out.println(best.total);
    }
}
