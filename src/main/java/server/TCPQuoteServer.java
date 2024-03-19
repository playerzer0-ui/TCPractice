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
                handleSession(socket, quotes);
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

    public static void handleSession(Socket socket, Map<String, String> quotes){
        try(Scanner input = new Scanner(socket.getInputStream()); PrintWriter output = new PrintWriter(socket.getOutputStream())){
            boolean validSession = true;
            while(validSession){
                String request = input.nextLine();
                String[] components = request.split(TCProtocol.DELIMITER);
                String response = null;
                System.out.println(request);

                switch (components[0]){
                    case TCProtocol.ADD:
                        response = addCommand(components, quotes);
                        break;

                    case TCProtocol.REMOVE:
                        response = removeCommand(components, quotes);
                        break;

                    case TCProtocol.GET_QUOTE:
                        response = getQuoteCommand(components, quotes);
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
        } catch (IOException e) {
            System.out.println("IO exception: " + e.getMessage());
        }
    }

    public static String addCommand(String[] components, Map<String, String> quotes){
        if(components.length == 3){
            quotes.put(components[2], components[1]);
            return TCProtocol.ADDED;
        }
        else{
            return TCProtocol.ERROR;
        }
    }

    public static String removeCommand(String[] components, Map<String, String> quotes){
        if(components.length == 3){
            if(quotes.containsKey(components[2])){
                if(quotes.get(components[2]).equals(components[1])){
                    quotes.remove(components[2]);
                    return TCProtocol.REMOVED;
                }
                else{
                    return TCProtocol.NOT_EXIST;
                }
            }
            else{
                return TCProtocol.NOT_EXIST;
            }
        }
        else{
            return TCProtocol.ERROR;
        }
    }

    public static String getQuoteCommand(String[] components, Map<String, String> quotes){
        if(quotes.isEmpty()){
            return TCProtocol.EMPTY;
        }
        else{
            String randomPerson = getRandomKey(quotes);
            return quotes.get(randomPerson) + TCProtocol.DELIMITER + randomPerson;
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
