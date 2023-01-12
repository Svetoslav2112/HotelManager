package com.example.hotelmanager;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;

public interface HotelFacade extends Remote {
    ArrayList<HotelRoom> getRooms() throws RemoteException;

    String getHelp(String request) throws RemoteException;

    HotelRoom getRoom(String id) throws RemoteException;

    String getCurrentWeatherAtHotelLocation() throws IOException, RemoteException;

    void changeOccupied(String roomId, LocalDate from, LocalDate to) throws RemoteException;

    boolean isRoomAvailable(ArrayList<String> selectedCriterias, double personCount, LocalDate from, LocalDate to, HotelRoom hr) throws RemoteException;
}
