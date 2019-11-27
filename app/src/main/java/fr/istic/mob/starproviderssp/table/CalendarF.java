package fr.istic.mob.starproviderssp;

public class CalendarF implements  StarContract{

    private String monday = Calendar.CalendarColumns.MONDAY;
    private String tuesday = Calendar.CalendarColumns.TUESDAY;
    private String wednesday = Calendar.CalendarColumns.WEDNESDAY;
    private String thursday = Calendar.CalendarColumns.THURSDAY;
    private String friday = Calendar.CalendarColumns.FRIDAY;
    private String saturday = Calendar.CalendarColumns.SATURDAY;
    private String Sunday = Calendar.CalendarColumns.SUNDAY;
    private String startdate = Calendar.CalendarColumns.START_DATE;
    private String enddate = Calendar.CalendarColumns.END_DATE;

    public CalendarF(String monday, String tuesday, String wednesday, String thursday, String friday,
                     String saturday, String sunday, String startdate, String enddate) {
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        Sunday = sunday;
        this.startdate = startdate;
        this.enddate = enddate;
    }

    public void CreateCalendar(){

    }

    public String getMonday() {
        return monday;
    }

    public void setMonday(String monday) {
        this.monday = monday;
    }

    public String getTuesday() {
        return tuesday;
    }

    public void setTuesday(String tuesday) {
        this.tuesday = tuesday;
    }

    public String getWednesday() {
        return wednesday;
    }

    public void setWednesday(String wednesday) {
        this.wednesday = wednesday;
    }

    public String getThursday() {
        return thursday;
    }

    public void setThursday(String thursday) {
        this.thursday = thursday;
    }

    public String getFriday() {
        return friday;
    }

    public void setFriday(String friday) {
        this.friday = friday;
    }

    public String getSaturday() {
        return saturday;
    }

    public void setSaturday(String saturday) {
        this.saturday = saturday;
    }

    public String getSunday() {
        return Sunday;
    }

    public void setSunday(String sunday) {
        Sunday = sunday;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }
}
