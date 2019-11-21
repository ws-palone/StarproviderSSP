package fr.istic.mob.starproviderssp;

public class StopsF implements StarContract{

    private String name = Stops.StopColumns.NAME;
    private String description = Stops.StopColumns.DESCRIPTION;
    private String latitude = Stops.StopColumns.LATITUDE;
    private String Longitude = Stops.StopColumns.LONGITUDE;
    private  String wheelchairBoarding = Stops.StopColumns.WHEELCHAIR_BOARDING;

    public StopsF(String name, String description, String longitude, String latitude, String wheelchairBoarding) {
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.Longitude = longitude;
        this.wheelchairBoarding = wheelchairBoarding;
    }

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
