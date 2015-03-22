package com.equalexperts.weather1self.client.response.wu;

public class DateTime {
    private String yyyy;
    private String MM;
    private String dd;
    private String HH;
    private String mm;

    public DateTime(String yyyy, String MM, String dd, String HH, String mm) {
        this.yyyy = yyyy;
        this.MM = MM;
        this.dd = dd;
        this.HH = HH;
        this.mm = mm;
    }

    public String toISOString() {
        return yyyy + "-" + MM + "-" + dd + "T" + HH + ":" + mm + ":00.000Z";    // “2014-11-11T22:30:00.000Z”
    }
}
