package com.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ATMServer {
    String serverPort;
    String databaseAddress;
    String databasePort;
    String databaseName;
    String databaseUser;
    String databasePassword;

    public com.Server.DatabaseHandler databaseHandler;

    public void main(String[] args) {
        // split arguments into separate variables
        this.serverPort = args[1];
        this.databaseAddress = args[2];
        this.databasePort = args[3];
        this.databaseName = args[4];
        this.databaseUser = args[5];
        this.databasePassword = args[6];

        try {
            ServerSocket serverSocket = new ServerSocket(1234);
            System.out.println("Server started and listening on port 1234...");

            // Connect to database
            databaseHandler = new com.Server.DatabaseHandler(databaseAddress, databasePort, databaseName, databaseUser, databasePassword);
            databaseHandler.connectToDatabase();

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket);

                // Handle client request in a separate thread
                Thread clientThread = new Thread(new ClientHandler(clientSocket, databaseHandler));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
