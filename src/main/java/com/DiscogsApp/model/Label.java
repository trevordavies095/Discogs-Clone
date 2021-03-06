package com.DiscogsApp.model;

public class Label implements SearchObject
{
    // Class constants
    private static final SearchEnum type = SearchEnum.LABEL;
    private final String name;
    private final int formed;
    private final double netWorth;

    // Class variables

    public Label(String name, int formed, double netWorth)
    {
        // Local constants

        // Local variables

        /****** start Label() ******/

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
    public String toString(){
        return name;
    }

    @Override
    public boolean equals(Object o)
    {
        // Local constants

        // Local variables
        Label label;

        /****** start equals() ******/

        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        label = (Label) o;

        if (formed != label.formed)
            return false;
        if (Double.compare(label.netWorth, netWorth) != 0)
            return false;

        return name != null ? name.equals(label.name) : label.name == null;
    }

}
