package com.hse.aco;

import com.hse.aco.algo.ACO;
import com.hse.aco.algo.TabuSearchContext;
import com.hse.aco.io.Reader;

public class App {

    public static void main(String[] args) {

        ACO search = new ACO();
        TabuSearchContext context = Reader.read();
        search.run(context);
    }
}
