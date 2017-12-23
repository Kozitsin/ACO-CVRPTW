package com.hse.aco;

import com.hse.aco.algo.ACO;
import com.hse.aco.algo.Context;
import com.hse.aco.io.Reader;
import com.hse.aco.io.Writer;

public class App {
    public static void main(String[] args) {
        String path = args[0];
        Context context = Reader.read(path);
        new ACO(context).run();
        Writer.write(context);
    }
}
