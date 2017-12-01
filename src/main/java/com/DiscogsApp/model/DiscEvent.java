package com.DiscogsApp.model;

public class DiscEvent
{
    // Class constants

    // Class variables
    private String eventName;
    private String eventTime;
    private String eventLocation;
    private String eventArtist;
    private int eventID;
    private int attendees;

    public DiscEvent(String eventName, String eventTime, String eventArtist,
                     String eventLocation, int ID, int attendees)
    {
        // Local constants

        // Local variables

        /****** start DiscEvent() ******/

        this.eventArtist = eventArtist;
        this.eventLocation = eventLocation;
        this.eventName = eventName;
        this.eventTime = eventTime;
        this.eventID = ID;
        this.attendees = attendees;
    }

    public static String parseTimestamp(String eTime)
    {
        // Local constants

        // Local variables
        String [] inter = eTime.split(" ");
        String [] date = inter[0].split("-");
        String [] time = inter[1].split(":");
        int ogHours = Integer.parseInt(time[0]) - 12;
        String ampm = "AM";
        String hours;

        /****** start parseTimestamp() ******/

        if(ogHours >= 0)
            ampm = "PM";
        if(ogHours == 0)
            ogHours = 12;

        hours = Integer.toString(Math.abs(ogHours));

        return date[2] + "/" + date[1] + "/" + date[0] + " at " + hours + ":" + time[1] + ampm;
    }

    public String getEventTime(){
        return parseTimestamp(this.eventTime);
    }
    public String getRawTime(){
        return this.eventTime;
    }
    public String getEventLocation() {
        return eventLocation;
    }
    public String getEventArtist() {
        return eventArtist;
    }
    public String getEventName() {
        return eventName;
    }
    public int getAttendees() {
        return attendees;
    }
    public int getEventID() {
        return eventID;
    }
}
