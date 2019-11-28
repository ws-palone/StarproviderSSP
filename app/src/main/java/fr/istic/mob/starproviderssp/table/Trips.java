package fr.istic.mob.starproviderssp.table;

import fr.istic.mob.starproviderssp.StarContract;

public class Trips implements StarContract {

    private int routeId;
    private int serviceId;
    private String headsign;
    private int directionId;
    private String blockId;
    private int wheelchairAccessible;


    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getHeadsign() {
        return headsign;
    }

    public void setHeadsign(String headsign) {
        this.headsign = headsign;
    }

    public int getDirectionId() {
        return directionId;
    }

    public void setDirectionId(int directionId) {
        this.directionId = directionId;
    }

    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public int getWheelchairAccessible() {
        return wheelchairAccessible;
    }

    public void setWheelchairAccessible(int wheelchairAccessible) {
        this.wheelchairAccessible = wheelchairAccessible;
    }
}
