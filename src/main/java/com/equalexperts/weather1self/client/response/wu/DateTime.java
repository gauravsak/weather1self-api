package com.equalexperts.weather1self.client.response.wu;

public class DateTime {
    private String year;
    private String month;
    private String dayOfMonth;
    private String hour;
    private String minute;

    public DateTime(String year, String month, String dayOfMonth, String hour, String minute) {
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        this.hour = hour;
        this.minute = minute;
    }

    public String toISOString() {
        return year + "-" + month + "-"
                + dayOfMonth + "T" + hour + ":" + minute + ":00.000Z";    // “2014-11-11T22:30:00.000Z”
    }
}
