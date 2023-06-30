package com.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The ATMServer class represents the server component of the ATM application.
 * It listens for client connections, handles client requests, and interacts with the database.
 */
public class ATMServer {
    String serverPort;
    String databaseAddress;
    String databasePort;
    String databaseName;
    String databaseUser;
    String databasePassword;

    public com.Server.DatabaseHandler databaseHandler;

    /**
     * The main entry point of the server application.
     *
     * @param args The command-line arguments.
     *             - args[0] is not used in this application.
     *             - args[1] should be the server port.
     *             - args[2] should be the database address.
     *             - args[3] should be the database port.
     *             - args[4] should be the database name.
     *             - args[5] should be the database user.
     *             - args[6] should be the database password.
     */
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
