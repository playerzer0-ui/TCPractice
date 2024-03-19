package client;

import server.TCPQuoteServer;
import server.TCProtocol;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class TCPQuoteClient {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("client quote service");
        //setup connection
        try(Socket socket = new Socket(TCProtocol.HOST, TCProtocol.PORT)){
            try(Scanner input = new Scanner(socket.getInputStream()); PrintWriter output = new PrintWriter(socket.getOutputStream())){
                boolean validSession = true;

                while(validSession){
                    String msg = actionMenu();

                    output.println(msg);
                    output.flush();

                    String response = input.nextLine();
                    System.out.println("message recieved: " + response);

                    if(response.equals(TCProtocol.EXIT)){
                        validSession = false;
                    }
                }
            }
        }
        catch(UnknownHostException e){
            System.out.println("Host cannot be found at this moment. Try again later");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void displayMenu(){
        System.out.println("1) add a quote");
        System.out.println("2) remove a quote");
        System.out.println("3) get a random quote");
        System.out.println("4) EXIT");
    }

    public static String actionMenu(){
        Scanner sc = new Scanner(System.in);
        System.out.println("input message (type the number): ");
        String request = null;

        String author;
        String quote;

        displayMenu();
        String msg = sc.nextLine();
        switch(msg){
            case "1":
                System.out.println("input author name: ");
                author = sc.nextLine();
                System.out.println("input quote: ");
                quote = sc.nextLine();

                request = TCProtocol.ADD + TCProtocol.DELIMITER + quote + TCProtocol.DELIMITER + author;
                break;

            case "2":
                System.out.println("input author name: ");
                author = sc.nextLine();

                request = TCProtocol.REMOVE + TCProtocol.DELIMITER + author;
                break;

            case "3":
                request = TCProtocol.GET_QUOTE;
                break;

            case "4":
                request = TCProtocol.EXIT;
                break;

            default:
                System.out.println("invalid number, try again");
                break;
        }
        return request;
    }
}
