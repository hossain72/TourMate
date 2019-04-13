package com.remon.tourmate;

public class TourInformation {

    String tourId, tourName, tourDescription, startDate, endDate;

    public TourInformation() {
    }

    public TourInformation(String tourName, String tourDescription, String startDate, String endDate) {
        this.tourName = tourName;
        this.tourDescription = tourDescription;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public TourInformation(String userid, String tourName, String tourDescription, String startDate, String endDate) {
        this.tourId = userid;
        this.tourName = tourName;
        this.tourDescription = tourDescription;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getTourId() {
        return tourId;
    }

    public void setTourId(String tourId) {
        this.tourId = tourId;
    }

    public String getTourName() {
        return tourName;
    }

    public String getTourDescription() {
        return tourDescription;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }
}
