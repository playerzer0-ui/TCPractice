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
        // SET UP HOST AND PORT INFO
        // Done in MathsService utility class
        // Make a listening socket
        try (ServerSocket listeningSocket = new ServerSocket(TCProtocol.PORT)) {
            int largest = Integer.MIN_VALUE;
            // REPEATEDLY:
            while (true) {
                // Accept an incoming connection request
                Socket dataSocket = listeningSocket.accept();
                // Set up our lines of communication - input and output
                try (Scanner input = new Scanner(dataSocket.getInputStream());
                     PrintWriter output = new PrintWriter(dataSocket.getOutputStream());) {
                    boolean validSession = true;
                    int clientLargest = Integer.MIN_VALUE;
                    // REPEATEDLY
                    while(validSession){
                        // Take in a request
                        String message = input.nextLine();
                        // Parse the request
                        String [] components = message.split(TCProtocol.DELIMITER);
                        // Do the requested action and generate an appropriate response
                        String response = "";
                        System.out.println(message);
                        switch(components[0]){
                            case(TCProtocol.CUBE):
                                if(components.length == 2){
                                    if(isNumber(components[1])){
                                        int result = Integer.parseInt(components[1]);
                                        result = result * result * result;
                                        response = String.valueOf(result);

                                        if(result > clientLargest){
                                            clientLargest = result;
                                        }
                                        if(result > largest){
                                            largest = result;
                                        }
                                    }
                                }else{
                                    response = TCProtocol.INVALID;
                                }
                                break;
                            case(TCProtocol.SQUARE):
                                if(components.length == 2){
                                    if(isNumber(components[1])){
                                        int result = Integer.parseInt(components[1]);
                                        result *= 2;
                                        response = String.valueOf(result);

                                        if(result > clientLargest){
                                            clientLargest = result;
                                        }
                                        if(result > largest){
                                            largest = result;
                                        }
                                    }
                                }else{
                                    response = TCProtocol.INVALID;
                                }
                                break;
                            case(TCProtocol.MYLARGEST):
                                if(components.length == 1) {
                                    response = "" + clientLargest;
                                }else{
                                    response = TCProtocol.INVALID;
                                }
                                break;
                            case(TCProtocol.LARGEST):
                                if(components.length == 1) {
                                    response = "" + largest;
                                }else{
                                    response = TCProtocol.INVALID;
                                }
                                break;
                            case(TCProtocol.EXIT):
                                if(components.length == 1) {
                                    validSession = false;
                                    response = TCProtocol.EXIT;
                                }else{
                                    response = TCProtocol.INVALID;
                                }
                                break;
                            default:
                                response = TCProtocol.INVALID;
                        }
                        // Send the response
                        output.println(response);
                        output.flush();
                    }
                }
            }
        } catch (BindException e) {
            System.out.println("BindException occurred when attempting to bind to port " + TCProtocol.PORT);
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException occurred on server socket");
            System.out.println(e.getMessage());
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
