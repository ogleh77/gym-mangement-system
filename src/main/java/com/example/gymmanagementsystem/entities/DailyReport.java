package com.example.gymmanagementsystem.entities;

public class DailyReport {
    private final String day;
    private final int registrations;
    private final int male;
    private final int female;
    private final int vipBox;

    public DailyReport(String day, int registrations, int male, int female, int vipBox) {
        this.day = day;
        this.registrations = registrations;
        this.male = male;
        this.female = female;
        this.vipBox = vipBox;
    }

    public String getDay() {
        return day;
    }

    public int getRegistrations() {
        return registrations;
    }

    public int getMale() {
        return male;
    }

    public int getFemale() {
        return female;
    }

    public int getVipBox() {
        return vipBox;
    }

    @Override
    public String toString() {
        return "DailyReport{" +
                "day='" + day + '\'' +
                ", registrations=" + registrations +
                ", male=" + male +
                ", female=" + female +
                ", vipBox=" + vipBox +
                '}';
    }
}
