package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TCPMathServer {
    public static void main(String[] args) {
        String response = null;
        Map<Integer, Integer> map = new HashMap<>();

        try (ServerSocket serverSocket = new ServerSocket(TCProtocol.PORT)){
            while(true){
                try(Socket socket = serverSocket.accept()){
                    try(Scanner input = new Scanner(socket.getInputStream()); PrintWriter output = new PrintWriter(socket.getOutputStream())){
                        String request = input.nextLine();

                        System.out.println(request);
                        String[] components = request.split(TCProtocol.DELIMITER);

                        switch (components[0]){
                            case TCProtocol.SQUARE:
                                if(components.length == 2){
                                    if(isNumber(components[1])){
                                        int res = Integer.parseInt(components[1]);
                                        response = String.valueOf(res * res);
                                    }
                                    else {
                                        response = TCProtocol.PLEASE_SUPPLY_NUMBER;
                                    }
                                }
                                else{
                                    response = TCProtocol.PLEASE_SUPPLY_NUMBER;
                                }
                                break;

                            case TCProtocol.CUBE:
                                if(components.length == 2){
                                    if(isNumber(components[1])){
                                        int res = Integer.parseInt(components[1]);
                                        response = String.valueOf(res * res * res);
                                    }
                                    else {
                                        response = TCProtocol.PLEASE_SUPPLY_NUMBER;
                                    }
                                }
                                else{
                                    response = TCProtocol.PLEASE_SUPPLY_NUMBER;
                                }
                                break;

                            case TCProtocol.MYLARGEST:
                                break;


                        }

                        output.println(response);
                        output.flush();
                    }
                }
            }
        }catch (BindException e) {
            System.out.println("An exception occurred when binding to the server port " + TCProtocol.PORT);
        } catch (IOException e) {
            System.out.println("An IO exception occurred" + e.getMessage());
        }
    }
    public static boolean isNumber(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            // If the string is not a valid number, the parseDouble method will throw NumberFormatException
            return false;
        }
    }
}
