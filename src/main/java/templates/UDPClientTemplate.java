package templates;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class UDPClientTemplate {
    public static void main(String[] args) {

        System.out.println("client listening");
        Scanner sc = new Scanner(System.in);
        int myPort = 1122;

        while(true){
            try {
                System.out.println("insert message: ");
                String message = sc.nextLine();

                // Create a socket on which to listen for messages to that port
                DatagramSocket mySocket = new DatagramSocket(myPort);
                // Set timeout on the socket, so it doesn't wait for a message for longer than 5000 milliseconds
                mySocket.setSoTimeout(5000);

                // Destination address information - IP and port
                InetAddress destinationIP = InetAddress.getByName("localhost");
                int destinationPort = 9999;


                // TRANSMISSION STAGE:
                // Condition the message for transmission
                byte[] payload = message.getBytes();
                // Build the packet to be sent
                DatagramPacket packet = new DatagramPacket(payload, payload.length, destinationIP, destinationPort);
                // Send message to server
                mySocket.send(packet);

                // Create a byte array to hold the payload data
                byte [] receivedMessage = new byte[50];
                // Create a packet to hold the received message
                DatagramPacket incomingMessage = new DatagramPacket(receivedMessage, receivedMessage.length);
                // Receive the message from the network
                // This will BLOCK until it receives a message, i.e. the code will not progress beyond this line!
                mySocket.receive(incomingMessage);
                // Get the data out of the packet
                receivedMessage = incomingMessage.getData();
                int len = incomingMessage.getLength();
                String incoMessage = new String(receivedMessage, 0, len);

                // LOGIC STAGE
                // Display the data from the packet
                System.out.println("Response received: " + incoMessage);

                // Close socket!
                mySocket.close();

            } catch (UnknownHostException e) {
                System.out.println("IP address is not recognised");
                System.out.println(e.getMessage());
            } catch (SocketException e) {
                System.out.println("Problem occurred on the socket");
                System.out.println(e.getMessage());
            } catch (IOException e) {
                System.out.println("Problem occurred when working with the socket");
                System.out.println(e.getMessage());
            }
        }
    }

}
