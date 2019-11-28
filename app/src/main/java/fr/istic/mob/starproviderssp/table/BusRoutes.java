package fr.istic.mob.starproviderssp.table;

import fr.istic.mob.starproviderssp.StarContract;

public class BusRoutes implements StarContract {

    private int id;
    private String short_name;
    private String long_name;
    private String descritpion;
    private String type;
    private String color;
    private String text_color;

    public int getId(){return id;}

    public void setId(int id){this.id = id;}

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
