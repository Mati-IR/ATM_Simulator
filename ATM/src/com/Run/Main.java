package com.Run;
import com.Server.ATMServer;
import com.Client.AtmApplication;

public class Main {

    /**
     * The main entry point of the application.
     *
     * @param args The command-line arguments.
     *             If the first argument is "c", the client application will be built.
     *             If the first argument is "s", the server application will be built.
     *             For the server application, the following arguments should be provided:
     *             - port number
     *             - database address
     *             - database port
     *             - database name
     *             - database user
     *             - database password
     *             For the client application, the following arguments should be provided:
     *             - client number
     *             - server address
     *             - server port
     */
    public static void main(String[] args) {
	    // if argument is equal to "c" build client, if it is "s", build server
        if(args[0].equals("c")){
            System.out.println("Building client...");
            AtmApplication atmApp = new AtmApplication();
            atmApp.main(args);
        } else if(args[0].equals("s")){
            System.out.println("Building server...");
            ATMServer atmServer = new ATMServer();
            atmServer.main(args);
        } else {
            System.out.println("Wrong argument. Use \"c\" for client or \"s\" for server.");
        }

    }
}
