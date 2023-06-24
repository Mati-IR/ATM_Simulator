package com.Client;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;

public class ClientSocketHandler {
    private Socket clientSocket;
    private BufferedReader in;
    private PrintStream out;

    private String serverAddress = "";
    private int serverPort = 0;

    public ClientSocketHandler(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        try {
            this.clientSocket = new Socket(serverAddress, serverPort);
            clientSocket.setKeepAlive(true);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not create socket\nAddress: " + serverAddress + "\nPort: " + serverPort);
        }
    }

    public void connectToServer() {
        try {
            if (clientSocket == null){
                clientSocket = new Socket(serverAddress, serverPort);
                if (clientSocket == null) {
                    System.out.println("Could not connect to server...");
                    return;
                }
            }
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintStream(clientSocket.getOutputStream(), true);
            System.out.println("Connected to server...");

            //clean up
            in.close();
            out.close();
            clientSocket.close();
        } catch (ConnectException e) {
            System.out.println("Could not connect to server...");
        } catch (IOException e) {
            System.out.println("Could not write message to server...");
        } catch (NullPointerException e) {
            System.out.println("Null pointer - Could not connect to server...");
        }

    }

    public String sendRequestAndReceiveResponse(String request) {
        try {
            if (null == clientSocket || clientSocket.isClosed()) {
                //Open a new socket
                clientSocket = new Socket(serverAddress, serverPort);
                if (clientSocket == null) {
                    System.out.println("Could not connect to server...");
                    return null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // try with resources with printwriter and bufferedreader
        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            out.println(request);
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))){
                return in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            System.out.println("Could not write message to server...");
            e.printStackTrace();
        }
        return null;
    }


    public void disconnectFromServer() {
        try {
            in.close();
            out.close();
            clientSocket.close();
            System.out.println("Disconnected from server.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        if (null == clientSocket || clientSocket.isClosed()){
            return false;
        }
        return clientSocket.isConnected();
    }
}
