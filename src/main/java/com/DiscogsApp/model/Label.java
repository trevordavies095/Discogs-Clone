package com.DiscogsApp.model;

public class Label implements SearchObject {

    private static final SearchEnum type = SearchEnum.LABEL;

    private final String name;

    private final int formed;

    private final double netWorth;

    public Label(String name, int formed, double netWorth){
        this.formed = formed;
        this.name = name;
        this.netWorth = netWorth;
    }

    public static SearchEnum getType() {
        return type;
    }

    public int getFormed() {
        return formed;
    }

    public String getName() {
        return name;
    }

    public double getNetWorth() {
        return netWorth;
    }
}
