package com.example.hotelmanager;

import java.io.IOException;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
    private Registry reg;

    public Client() {
        try {
            reg = LocateRegistry.getRegistry("localhost", 1099);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

//    public void startChat() {
//        try (Socket socket = new Socket("localhost", 5000)) {
//            // I/O
//           // Thread clientThread = new ClientThread(socket, txtMessageInput);
//          //  clientThread.setDaemon(false);
//           // clientThread.start();
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public HotelFacade getStub() throws NotBoundException, RemoteException {
        return (HotelFacade) reg.lookup("rmi://localhost/server");
    }

    public static void main(String[] args) {
        System.out.println("Begin client, now=" + System.currentTimeMillis());
        Client client = new Client();
    }
}

