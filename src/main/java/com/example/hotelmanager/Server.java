package com.example.hotelmanager;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Server extends UnicastRemoteObject implements HotelFacade {
    private ArrayList<HotelRoom> rooms;

    public Server() throws IOException {
        super();
        this.rooms = createRooms();
    }

    @Override
    public ArrayList<HotelRoom> getRooms() throws RemoteException {
        return rooms;
    }

    @Override
    public String getHelp(String request) throws RemoteException {
        String header = "[EasyHotel]:";
        switch(request) {
            case "reservation" -> {
                return header + " In order to make a reservation, please select an available room and click on the 'Reserve' button." + "\n" + "\n";
            }
            case "filter" -> {
                return header + " You can filter the room by features, date or count of people. In order to filter the rooms, use the checkboxes, date picker and person slider." + "\n" + "\n";
            }
            case "preview" -> {
                return header + " You can preview a room by selecting the given room's button. Then you can preview the images of the room by using the radio buttons on the bottom" + "\n" + "\n";
            }
            case "" -> {
                return header + " Welcome to EasyHotel. Please enter one of the following commands in order to get help: reservation, filter, preview" + "\n" + "\n";
            }
            default -> {
                return header + " Please enter valid command" + "\n" + "\n";
            }
        }
    }

    @Override
    public HotelRoom getRoom(String id) throws RemoteException {
        int ind = Integer.parseInt(id);
        return this.rooms.get(ind-1);
    }

    @Override
    public String getCurrentWeatherAtHotelLocation() throws IOException {
        Socket socket = new Socket("localhost", 11000);
        InputStream is = socket.getInputStream();

        // Length of received string
        byte[] resLength = new byte[4];
        is.read(resLength, 0, 4);
        int len = (((resLength[3] & 0xff) << 24) | ((resLength[2] & 0xff) << 16) |
                ((resLength[1] & 0xff) << 8) | (resLength[0] & 0xff));

        // Received string
        byte[] res = new byte[len];
        is.read(res, 0, len);
        String received = new String(res, 0, len);

        socket.close();
        return received;
    }

    @Override
    public void changeOccupied(String roomId, LocalDate from, LocalDate to) throws RemoteException {
        int roomIdNum = Integer.parseInt(roomId);
        for(HotelRoom hr : this.rooms) {

            if(hr.getRoomNumber() == roomIdNum) {
                hr.setReserved_from(from);
                hr.setReserved_to(to);
            }
        }
    }

    @Override
    public boolean isRoomAvailable(ArrayList<String> selectedCriterias, double personCount, LocalDate from, LocalDate to, HotelRoom hr) throws RemoteException {
        boolean visible = true;
        for (String selectedCriteria : selectedCriterias) {
            if (!hr.hasFeature(selectedCriteria)) {
                visible = false;
            }
        }

        if(hr.getPersonCount() < personCount) visible = false;

        LocalDate reservedFrom = hr.getReservedFromDate();
        LocalDate reservedTo = hr.getReservedToDate();

        if(reservedFrom != null && reservedTo != null) {
            if(!((reservedTo.isBefore(from)  || (reservedFrom.isAfter(to)  )))) visible = false;
        }

        return visible;
    }

    private static ArrayList<HotelRoom> createRooms() throws IOException {
        ArrayList<HotelRoom> list = new ArrayList<HotelRoom>();
        String imageFolderURL = "images";
        list.add(new HotelRoom(new ArrayList<Features>(List.of(Features.WIFI, Features.MINI_FRIDGE, Features.BALCONY)), 3, 1, 100, false, LocalDate.of(2023, 2, 1), LocalDate.of(2023, 2, 10), imageFolderURL));
        list.add(new HotelRoom(new ArrayList<Features>(List.of(Features.BATHTUB, Features.MINI_FRIDGE)), 2, 2, 100, true, LocalDate.of(2023, 3, 1), LocalDate.of(2023, 3, 10), imageFolderURL));
        list.add(new HotelRoom(new ArrayList<Features>(List.of(Features.WIFI, Features.MINI_FRIDGE, Features.KITCHEN, Features.BATHTUB, Features.BALCONY, Features.TV, Features.AC, Features.ROOM_SERVICE)), 2, 3, 500, true, null, null, imageFolderURL));       // list.add(new HotelRoom( new ArrayList<Features>(List.of(Features.WIFI, Features.MINI_FRIDGE, Features.TV)), 4, 5, 150, false));
        list.add(new HotelRoom(new ArrayList<Features>(List.of(Features.AC, Features.BALCONY)), 5, 4, 120, true, LocalDate.of(2023, 4, 12), LocalDate.of(2023, 4, 15), imageFolderURL));
        list.add(new HotelRoom(new ArrayList<Features>(List.of(Features.KITCHEN, Features.MINI_FRIDGE, Features.TV)), 5, 5, 200, true, LocalDate.of(2023, 5, 20), LocalDate.of(2023, 5, 22), imageFolderURL));
        list.add(new HotelRoom(new ArrayList<Features>(List.of(Features.WIFI, Features.MINI_FRIDGE, Features.KITCHEN,  Features.BALCONY)), 3, 6, 160, false, null, null, imageFolderURL));
        list.add(new HotelRoom(new ArrayList<Features>(List.of(Features.WIFI, Features.MINI_FRIDGE, Features.KITCHEN)), 2, 7, 90, false, null, null, imageFolderURL));
        list.add(new HotelRoom(new ArrayList<Features>(List.of( Features.KITCHEN, Features.BATHTUB, Features.BALCONY)), 3, 8, 110, true, null, null, imageFolderURL));
        list.add(new HotelRoom(new ArrayList<Features>(List.of(Features.ROOM_SERVICE, Features.MINI_FRIDGE, Features.KITCHEN, Features.AC, Features.BALCONY)), 3, 9, 200, false, null, null, imageFolderURL));
        list.add(new HotelRoom(new ArrayList<Features>(List.of(Features.AC, Features.WIFI, Features.TV)), 6, 10, 190, true, LocalDate.of(2023, 2, 2), LocalDate.of(2023, 2, 10), imageFolderURL));
        list.add(new HotelRoom(new ArrayList<Features>(List.of(Features.KITCHEN, Features.MINI_FRIDGE, Features.TV)), 2, 11, 120, true, LocalDate.of(2023, 2, 20), LocalDate.of(2023, 2, 22), imageFolderURL));
        list.add(new HotelRoom(new ArrayList<Features>(List.of(Features.TV)), 2, 12, 100, false, LocalDate.of(2023, 3, 3), LocalDate.of(2023, 3, 7), imageFolderURL));
        list.add(new HotelRoom(new ArrayList<Features>(List.of()), 2, 13, 50, false, null, null, imageFolderURL));
        list.add(new HotelRoom(new ArrayList<Features>(List.of(Features.KITCHEN, Features.MINI_FRIDGE, Features.TV)), 5, 14, 200, true, LocalDate.of(2023, 4, 20), LocalDate.of(2023, 4, 22), imageFolderURL));
        list.add(new HotelRoom(new ArrayList<Features>(List.of( Features.KITCHEN, Features.BATHTUB, Features.BALCONY)), 3, 15, 110, true, null, null, imageFolderURL));
        list.add(new HotelRoom(new ArrayList<Features>(List.of( Features.AC, Features.WIFI, Features.BALCONY)), 4, 16, 150, false, null, null, imageFolderURL));
        list.add(new HotelRoom(new ArrayList<Features>(List.of( Features.BALCONY)),  3, 17, 80, true, null, null, imageFolderURL));
        list.add(new HotelRoom(new ArrayList<Features>(List.of( Features.KITCHEN, Features.BATHTUB, Features.BALCONY, Features.AC, Features.ROOM_SERVICE)), 3, 18, 250, true, null, null, imageFolderURL));
        list.add(new HotelRoom(new ArrayList<Features>(List.of( Features.KITCHEN, Features.BALCONY, Features.MINI_FRIDGE)), 2, 19, 140, false, null, null, imageFolderURL));
        list.add(new HotelRoom(new ArrayList<Features>(List.of( Features.KITCHEN, Features.BALCONY, Features.MINI_FRIDGE)), 2, 20, 140, false, null, null, imageFolderURL));


        return list;
    }

//    public static void acceptConnection() throws IOException {
//        ArrayList<ServerThread> threadList = new ArrayList<>();
//        try (ServerSocket serversocket = new ServerSocket(5000)) {
//            while (true) {
//                System.out.println("wait for client");
//                Socket socket = serversocket.accept();
//                System.out.println("server accepts connection");
//                ServerThread serverThread = new ServerThread(socket, threadList);
//                threadList.add(serverThread);
//                serverThread.start();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    public static void main(String[] args) {
        try {
            // RMI
            Registry reg = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            Server server = new Server();
            reg.bind("rmi://localhost/server", server);
            System.out.println("The server is currently running...");
        } catch (AlreadyBoundException | IOException e) {
            e.printStackTrace();
        }
    }

}
