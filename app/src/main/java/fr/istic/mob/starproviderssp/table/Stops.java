package fr.istic.mob.starproviderssp.table;

import fr.istic.mob.starproviderssp.StarContract;

public class Stops implements StarContract {

    private int id;
    private String name = Stops.StopColumns.NAME;
    private String description = Stops.StopColumns.DESCRIPTION;
    private String latitude = Stops.StopColumns.LATITUDE;
    private String Longitude = Stops.StopColumns.LONGITUDE;
    private  String wheelchairBoarding = Stops.StopColumns.WHEELCHAIR_BOARDING;

    public int getId(){return id;}

    public void setId(int id){this.id=id;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getWheelchairBoarding() {
        return wheelchairBoarding;
    }

    public void setWheelchairBoarding(String wheelchairBoarding) {
        this.wheelchairBoarding = wheelchairBoarding;
    }
}
