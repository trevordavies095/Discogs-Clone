package com.DiscogsApp.model;

public class Artist implements SearchObject {

    private static final SearchEnum type = SearchEnum.ARTIST;

    private final int artistID;

    private final String name;

    private final String realName;

    private final int debutYear;

    private final Label label;

    public Artist(int artistID, int debutYear, String name, String realName,
                  String labelName, Label label){
        this.artistID = artistID;
        this.debutYear = debutYear;
        this.name = name;
        if(realName != null) {
            this.realName = realName;
        } else {
            this.realName = "unknown";
        }
        this.label = label;
    }

    public static SearchEnum getType() {
        return type;
    }

    public int getArtistID() {
        return artistID;
    }

    public String getName() {
        return name;
    }

    public String getRealName() {
        return realName;
    }

    public int getDebutYear() {
        return debutYear;
    }

    public Label getLabel(){ return this.label; }

    public String toString(){
        return name + ", signed with " + label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Artist artist = (Artist) o;

        if (artistID != artist.artistID) return false;
        if (debutYear != artist.debutYear) return false;
        if (name != null ? !name.equals(artist.name) : artist.name != null) return false;
        if (realName != null ? !realName.equals(artist.realName) : artist.realName != null) return false;
        return label != null ? label.equals(artist.label) : artist.label == null;
    }
}
