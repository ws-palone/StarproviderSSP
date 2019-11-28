package fr.istic.mob.starproviderssp.table;

import fr.istic.mob.starproviderssp.StarContract;

public class BusRoutesF implements StarContract {
    
    private String short_name = BusRoutes.BusRouteColumns.SHORT_NAME;
    private String long_name = BusRoutes.BusRouteColumns.LONG_NAME;
    private String descritpion = BusRoutes.BusRouteColumns.DESCRIPTION;
    private String type = BusRoutes.BusRouteColumns.TYPE;
    private String color = BusRoutes.BusRouteColumns.COLOR;
    private String text_color = BusRoutes.BusRouteColumns.TEXT_COLOR;

    public BusRoutesF(String short_name, String long_name, String description, String type, String color, String text_color){
        this.short_name = short_name;
        this.long_name = long_name;
        this.descritpion = description;
        this.type = type;
        this.color = color;
        this.text_color = text_color;
    }

    public String getShort_name(){
        return this.short_name;
    }

    public void setShort_name(String short_name){
        this.short_name = short_name;
    }

    public String getLong_name(){
        return this.long_name;
    }

    public void setLong_name(String long_name){
        this.long_name = long_name;
    }

    public String getDescritpion(){
        return this.descritpion;
    }

    public void setDescritpion(String description){
        this.descritpion = descritpion;
    }

    public String getType(){
        return this.type;
    }

    public void setType(String type){
        this.type = type;
    }

    public String getColor(){
        return this.color;
    }

    public void setColor(String color){
        this.color = color;
    }

    public String getText_color(){
        return this.text_color;
    }

    public void setText_color(String text_color){
        this.text_color = text_color;
    }
}
