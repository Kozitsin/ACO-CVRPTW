package com.hse.aco.entity;

import java.util.ArrayList;
import java.util.List;

public class Solution {
    public List<Route> routes = new ArrayList<>();
    public double total = 0.0;

    public Solution() {
    }

    public Solution(double total) {
        this.total = total;
    }

    public void copyTo(Solution x) {
        x.routes = new ArrayList<>(this.routes.size());
        for (Route r : this.routes) {
            x.routes.add(r.copy());
        }
        x.total = this.total;
    }
}
