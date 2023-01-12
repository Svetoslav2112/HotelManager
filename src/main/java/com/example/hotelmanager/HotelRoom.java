package com.example.hotelmanager;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class HotelRoom implements Serializable {
    private  ArrayList<Features> features;
    private int personCount;
    private int roomNumber;
    private double pricePerNight;
    private boolean petsAllowed;
    private LocalDate reserved_from;
    private LocalDate reserved_to;
    private String[] imageURLs;
    private final int imageCount = 3;

    public HotelRoom(ArrayList<Features> features, int personCount, int roomNumber, double pricePerNight, boolean petsAllowed, LocalDate reserved_from, LocalDate reserved_to, String imagePath) throws IOException {
        this.features = features;
        this.personCount = personCount;
        this.roomNumber = roomNumber;
        this.pricePerNight = pricePerNight;
        this.petsAllowed = petsAllowed;
        this.imageURLs = new String[imageCount];
        this.reserved_from = reserved_from;
        this.reserved_to = reserved_to;

        for(int i = 1; i <= imageCount; i++) {
            String path = imagePath + "/" + "r" + roomNumber + "/img" + i + ".jpg";
            imageURLs[i-1] = path;
        }
    }

    public HotelRoom() {
        this.features = new ArrayList<>();
        this.personCount = 0;
        this.roomNumber = -1;
        this.pricePerNight = 0;
        this.petsAllowed = false;
        LocalDate dateFrom = LocalDate.of(0,0,0);
        LocalDate dateTo = LocalDate.of(0,0,0);
        this.reserved_from = dateFrom;
        this.reserved_to = dateTo;
        this.imageURLs = new String[imageCount];
    }

    public ArrayList<Features> getFeatures() {
        return features;
    }

    public int getPersonCount() {
        return personCount;
    }

    public void setReserved_from(LocalDate reserved_from) {
        this.reserved_from = reserved_from;
    }

    public void setReserved_to(LocalDate reserved_to) {
        this.reserved_to = reserved_to;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public boolean isPetsAllowed() {
        return petsAllowed;
    }

    public void setFeatures(ArrayList<Features> features) {
        this.features = features;
    }

    public void setPersonCount(int personCount) {
        this.personCount = personCount;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public void setPetsAllowed(boolean petsAllowed) {
        this.petsAllowed = petsAllowed;
    }

    public LocalDate getReservedFromDate() {
        return reserved_from;
    }

    public LocalDate getReservedToDate() {
        return reserved_to;
    }

    public int getFeatureIndByValue(String value) {
        for(int i = 0; i < features.size(); i++) {
            if(Objects.equals(features.get(i).toString(), value)) {
                return i;
            }
        }
        return 0;
    }
    public boolean hasFeature(String feature) {
        for (Features value : features) {
            if (Objects.equals(value.toString(), feature)) {
                return true;
            }
        }

        return false;
    }

    public String[] getImageURLs() {return this.imageURLs;}

    @Override
    public String toString() {
        return "HotelRoom{" +
                "features=" + features +
                ", personCount=" + personCount +
                ", roomNumber='" + roomNumber + '\'' +
                ", pricePerNight=" + pricePerNight +
                ", petsAllowed=" + petsAllowed +
                '}';
    }
}