package com.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ClientSocketHandler {
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;

    public ClientSocketHandler(String serverAddress, int serverPort) {
        try {
            this.clientSocket = new Socket(serverAddress, serverPort);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not create socket\nAddress: " + serverAddress + "\nPort: " + serverPort);
        }
    }

    public void connectToServer() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            System.out.println("Connected to server...");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not connect to server...");
        }
    }

    public void sendRequest(String request) {
        out.println(request);
    }

    public String receiveResponse() {
        try {
            return in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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
}
