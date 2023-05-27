package com.Run;
import com.Server.ATMServer;
import com.client.ATMClient;

public class Main {

    public static void main(String[] args) {
	    // if argument is equal to "c" build client, if it is "s", build server
        if(args[0].equals("c")){
            System.out.println("Building client...");
            ATMClient atmClient = new ATMClient(1, "localhost", 1234);
        } else if(args[0].equals("s")){
            System.out.println("Building server...");
            ATMServer atmServer = new ATMServer();
        } else {
            System.out.println("Wrong argument. Use \"c\" for client or \"s\" for server.");
        }

    }
}
