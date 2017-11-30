package com.DiscogsApp.model;

public class SearchField
{
    // Class constants
    private final SearchEnum type;
    private final String value;

    // Class variables

    public SearchField(SearchEnum type, String value)
    {
        // Local constants

        // Local variables

        /****** start SearchField() ******/

        this.type = type;
        this.value = value;
    }

    public SearchEnum getType() {
        return this.type;
    }
    public String getTable() {
        return type.getTable();
    }
    public String getfrName() {
        return type.getfrName();
    }
    public String getFkey(){
        return type.getFkey();
    }
    public String getValue(){
        return this.value;
    }
    public String getPkey(){
        return type.getPkey();
    }
}
