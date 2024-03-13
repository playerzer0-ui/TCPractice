package client;

import server.TCProtocol;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class TCPClient {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean flag = false;
        //request connection
        while (!flag){
            try(Socket socket = new Socket("localhost", TCProtocol.PORT)){

                //setup scanner and printWriter
                try(Scanner input = new Scanner(socket.getInputStream()); PrintWriter output = new PrintWriter(socket.getOutputStream())){
                    //get user to type something
                    System.out.println("Please enter a message to be sent:");
                    String msg = sc.nextLine();

                    //throw it to the server
                    output.println(msg);
                    output.flush();

                    //get the message from server
                    String response = input.nextLine();
                    System.out.println(response);

                    if(response.equals(TCProtocol.EXIT)){
                        flag = true;
                    }
                }
            }
            catch (UnknownHostException e) {
                System.out.println("Host cannot be found at this moment. Try again later");
            }
            catch (IOException e) {
                System.out.println("An IO Exception occurred: " + e.getMessage());
            }
        }
    }
}
