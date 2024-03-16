package client;

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
                    System.out.println("input message (type EXIT to leave): ");
                    String msg = sc.nextLine();

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
}
