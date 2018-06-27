package com.example.tanya.internityhackathon;

import com.example.tanya.internityhackathon.weather.Weather;

import java.util.ArrayList;

public class Data
{
    private Coord coord;
    private ArrayList<Weather> weather;
    private Main main;
    private Wind wind;
    private String name;

    public Data(Coord coord, ArrayList<Weather> weather, Main main, Wind wind, String name) {
        this.coord = coord;
        this.weather = weather;
        this.main = main;
        this.wind = wind;
        this.name = name;
    }

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public ArrayList<Weather> getWeather() {
        return weather;
    }

    public void setWeather(ArrayList<Weather> weather) {
        this.weather = weather;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "Data{" +
                "coord=" + coord +
                ", weather=" + weather +
                ", main=" + main +
                ", wind=" + wind +
                ", name='" + name + '\'' +
                '}';
    }
}
