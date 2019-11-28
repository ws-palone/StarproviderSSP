package fr.istic.mob.starproviderssp.table;

import fr.istic.mob.starproviderssp.StarContract;

public class TripF implements StarContract {

    private String routeId = Trips.TripColumns.ROUTE_ID;
    private String serviceId = Trips.TripColumns.SERVICE_ID;
    private String headsign = Trips.TripColumns.HEADSIGN;
    private String directionId = Trips.TripColumns.DIRECTION_ID;
    private String blockId = Trips.TripColumns.BLOCK_ID;
    private String wheelchairAccessible = Trips.TripColumns.WHEELCHAIR_ACCESSIBLE;

    public TripF(String routeId, String serviceId, String headsign, String directionId, String blockId, String wheelchairAccessible) {
        this.routeId = routeId;
        this.serviceId = serviceId;
        this.headsign = headsign;
        this.directionId = directionId;
        this.blockId = blockId;
        this.wheelchairAccessible = wheelchairAccessible;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getHeadsign() {
        return headsign;
    }

    public void setHeadsign(String headsign) {
        this.headsign = headsign;
    }

    public String getDirectionId() {
        return directionId;
    }

    public void setDirectionId(String directionId) {
        this.directionId = directionId;
    }

    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public String getWheelchairAccessible() {
        return wheelchairAccessible;
    }

    public void setWheelchairAccessible(String wheelchairAccessible) {
        this.wheelchairAccessible = wheelchairAccessible;
    }
}
