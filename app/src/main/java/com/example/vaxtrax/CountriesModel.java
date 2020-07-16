package com.example.vaxtrax;

public class CountriesModel {
    String country;
    StatsModel statsModel;

    public CountriesModel(String country, StatsModel statsModel) {
        this.country = country;
        this.statsModel = statsModel;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String name) {
        this.country = name;
    }

    public StatsModel getStatsModel() {
        return statsModel;
    }

    public void setStatsModel(StatsModel statsModel) {
        this.statsModel = statsModel;
    }
}
