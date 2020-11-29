package com.abdo.crona;

public class Hospital {

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLongt() {
        return longt;
    }

    public void setLongt(Double longt) {
        this.longt = longt;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    Double lat;
    Double longt;
    String Name;

    public Hospital(Double lat,Double longt, String Name){

        this.lat = lat;
        this.longt = longt;
        this.Name = Name;

    }



}
