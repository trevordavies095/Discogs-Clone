package com.DiscogsApp.appl;

public class SearchField {

    private final SearchEnum type;

    private final String value;

    public SearchField(SearchEnum type, String value){
        this.type = type;
        this.value = value;
    }

    public SearchEnum getType() {
        return type;
    }

    public String getValue(){
        return this.value;
    }
}
