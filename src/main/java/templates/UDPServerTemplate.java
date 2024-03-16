package templates;

import java.io.IOException;
import java.net.*;

public class UDPServerTemplate {
    private static final int myPort = 9999;
    private static DatagramSocket mySocket;
    private static InetAddress destinationIP;

    public static void main(String[] args) {

        int count = 0;
        String message = "";

        activate(myPort);
        while(true){

            try {
                byte [] receivedMessage = new byte[50];
                DatagramPacket incomingMessage = new DatagramPacket(receivedMessage, receivedMessage.length);
                mySocket.receive(incomingMessage);
                int l = incomingMessage.getLength();
//                String ipAddress = String.valueOf(incomingMessage.getAddress());
                String incoMessage = new String(receivedMessage, 0, l);

                String[] components = incoMessage.split("%%");
                int destPort = incomingMessage.getPort();

                switch (components[0]){

                }

                //send the message
                count++;
                byte[] payload = message.getBytes();
                DatagramPacket packet = new DatagramPacket(payload, payload.length, destinationIP, destPort);
                mySocket.send(packet);

                System.out.println("packet received from port: " + destPort);
                System.out.println("packets received: " + count);


            } catch (BindException e){
                System.out.println("socket already exists");
            } catch (SocketException e) {
                System.out.println("Problem occurred on the socket");
                System.out.println(e.getMessage());
            } catch (UnknownHostException e) {
                System.out.println("IP address is not recognised");
                System.out.println(e.getMessage());
            }
            catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

    }

    public static void activate(int myPort){
        try {
            mySocket = new DatagramSocket(myPort);
            destinationIP = InetAddress.getByName("localhost");
            mySocket.setSoTimeout(10000);
            System.out.println("listening server...");
        } catch (SocketException e) {
            System.out.println("socket error");
            System.out.println(e.getMessage());
        } catch (UnknownHostException e) {
            System.out.println("IP error");
            System.out.println(e.getMessage());
        }
    }
}
