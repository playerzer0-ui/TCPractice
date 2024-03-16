package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TCPQuoteServer {
    public static void main(String[] args) {
        try(ServerSocket serverSocket = new ServerSocket(TCProtocol.PORT)){
            Map<String, String> quotes = new HashMap<>();

            while(true){
                Socket socket = serverSocket.accept();
                boolean validSession = true;

                try(Scanner input = new Scanner(socket.getInputStream()); PrintWriter output = new PrintWriter(socket.getOutputStream())){

                    while(validSession){
                        String request = input.nextLine();
                        String[] components = request.split(TCProtocol.DELIMITER);
                        String response = null;

                        switch (components[0]){
                            case "TEST":
                                response = "asdsad";
                                break;

                            case TCProtocol.EXIT:
                                validSession = false;
                                response = TCProtocol.EXIT;
                                break;

                            default:
                                response = TCProtocol.INVALID;
                                break;
                        }

                        output.println(response);
                        output.flush();
                    }
                }
            }
        }
        catch (BindException e) {
            System.out.println("BindException occurred when attempting to bind to port " + TCProtocol.PORT);
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException occurred on server socket");
            System.out.println(e.getMessage());
        }
    }
}
