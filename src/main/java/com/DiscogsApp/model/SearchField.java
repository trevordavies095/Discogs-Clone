package com.DiscogsApp.model;

public class SearchField {

    private final SearchEnum type;

    private final String value;


    public SearchField(SearchEnum type, String value){
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
