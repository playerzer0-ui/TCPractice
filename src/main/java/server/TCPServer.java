package server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.TimeZone;

public class TCPServer {
    public static void main(String[] args) {
        String response = null;
        int count = 0;

        //get connection, SERVER
        try(ServerSocket serverSocket = new ServerSocket(TCProtocol.PORT)){

            //loop the thing, because it's a server
            while(true){
                //take next connection request
                try(Socket socket = serverSocket.accept()){
                    //setup scanner and printWriter
                    try(Scanner input = new Scanner(socket.getInputStream()); PrintWriter output = new PrintWriter(socket.getOutputStream())){
                        String request = input.nextLine();
                        System.out.println(request);
                        String[] components = request.split(TCProtocol.DELIMITER);


                        //LOGIC SWITCH
                        switch(components[0]){
                            case TCProtocol.EXIT:
                                response = TCProtocol.EXIT;
                                break;

                            //exercise 1
                            case TCProtocol.ECHO:
                                if(components.length == 2){
                                    response = components[1];
                                }
                                else{
                                    response = TCProtocol.INVALID;
                                }
                                break;

                            case TCProtocol.DAYTIME:
                                response = String.valueOf(LocalDateTime.now());
                                break;

                            case TCProtocol.STATS:
                                response = "stats: " + count;
                                break;

                            //exercise 2
                            case TCProtocol.EXISTS:
                                if(components.length == 2){
                                    File file = new File("src/main/resources/" + components[1]);
                                    if(file.exists()){
                                        response = TCProtocol.FOUND;
                                    }
                                    else{
                                        response = TCProtocol.FILE_NOT_FOUND;
                                    }
                                }
                                else{
                                    response = TCProtocol.INVALID;
                                }
                                break;

                            case TCProtocol.LAST_ACCESSED:
                                if(components.length == 2){
                                    File file = new File("src/main/resources/" + components[1]);
                                    if(file.exists()){
                                        response = longToDateTime(file.lastModified());
                                    }
                                    else{
                                        response = TCProtocol.FILE_NOT_FOUND;
                                    }
                                }
                                else{
                                    response = TCProtocol.INVALID;
                                }
                                break;

                            case TCProtocol.LENGTH:
                                if(components.length == 2){
                                    File file = new File("src/main/resources/" + components[1]);
                                    if(file.exists()){
                                        response = String.valueOf(file.length());
                                    }
                                    else{
                                        response = TCProtocol.FILE_NOT_FOUND;
                                    }
                                }
                                else{
                                    response = TCProtocol.INVALID;
                                }
                                break;

                            case TCProtocol.LARGEST:
                                File directory = new File("src/main/resources");
                                File[] files = directory.listFiles();

                                if (files == null || files.length == 0){
                                    response = TCProtocol.FILE_NOT_FOUND;
                                }
                                else {
                                    File largestFile = files[0];
                                    long largestSize = largestFile.length();

                                    for (File f: files) {
                                        if(f.length() > largestSize){
                                            largestFile = f;
                                            largestSize = f.length();
                                        }
                                    }

                                    response = largestFile + TCProtocol.DELIMITER + largestSize;
                                }
                                break;

                            case TCProtocol.LINE:
                                if(components.length == 3){
                                    File file = new File("src/main/resources/" + components[1]);

                                    if(!isNumber(components[2])){
                                        response = TCProtocol.INVALID;
                                    }
                                    else if(file.exists()){
                                        response = readFile(file, Integer.parseInt(components[2]));
                                    }
                                    else{
                                        response = TCProtocol.FILE_NOT_FOUND;
                                    }
                                }
                                else{
                                    response = TCProtocol.INVALID;
                                }
                                break;

                            default:
                                response = TCProtocol.INVALID;
                                break;
                        }

                        count++;
                        output.println(response);
                        output.flush();
                    }
                }
            }

        }
        catch (BindException e) {
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

    public static String readFile(File file, int lineNum){
        try {
            Scanner in = new Scanner(file);
            int count = 0;
            String line = null;

            while(in.hasNext() && count != lineNum){
                line = in.nextLine();
                count++;
            }

            if(count == lineNum){
                return line;
            }
            else{
                return TCProtocol.BOUNDS;
            }
        } catch (FileNotFoundException e) {
            return TCProtocol.FILE_NOT_FOUND;
        }
    }

    public static String longToDateTime(long lastModified){
        LocalDateTime accessDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(lastModified), TimeZone.getDefault().toZoneId());

        return accessDate.getDayOfWeek() + "" + accessDate.atZone(TimeZone.getDefault().toZoneId());
    }
}
