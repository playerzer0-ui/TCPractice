package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class TCPQuoteServer {
    public static void main(String[] args) {
        System.out.println("server quote online");
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
                            case TCProtocol.ADD:
                                if(components.length == 3){
                                    quotes.put(components[2], components[1]);
                                    response = TCProtocol.ADDED;
                                }
                                else{
                                    response = TCProtocol.ERROR;
                                }
                                break;

                            case TCProtocol.REMOVE:
                                if(components.length == 3){
                                    if(quotes.containsKey(components[2])){
                                        if(quotes.get(components[2]).equals(components[1])){
                                            quotes.remove(components[2]);
                                            response = TCProtocol.REMOVED;
                                        }
                                        else{
                                            response = TCProtocol.NOT_EXIST;
                                        }
                                    }
                                    else{
                                        response = TCProtocol.NOT_EXIST;
                                    }
                                }
                                else{
                                    response = TCProtocol.ERROR;
                                }
                                break;

                            case TCProtocol.GET_QUOTE:
                                if(quotes.isEmpty()){
                                    response = TCProtocol.EMPTY;
                                }
                                else{
                                    String randomPerson = getRandomKey(quotes);
                                    response = quotes.get(randomPerson) + TCProtocol.DELIMITER + randomPerson;
                                }
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

    public static <K, V> K getRandomKey(Map<K, V> map) {
        // Get a set of all keys
        Object[] keys = map.keySet().toArray();

        // Generate a random index
        Random random = new Random();
        int randomIndex = random.nextInt(keys.length);

        // Retrieve and return the key at the random index
        return (K) keys[randomIndex];
    }
}
