package com.Run;
import com.Server.ATMServer;
import com.Client.AtmApplication;

public class Main {

    /* server arguments shall be inputed in following order:
    * 1. "s" - to build server
    * 2. port number
    * 3. database address
    * 4. database port
    * 5. database name
    * 6. database user
    * 7. database password
    *
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
