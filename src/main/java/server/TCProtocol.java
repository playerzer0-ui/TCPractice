package server;

public class TCProtocol {
    public final static int PORT = 2343;
    public final static String HOST = "localhost";
    public final static String DELIMITER = "%%";
    public final static String INVALID = "INVALID_COMMAND";
    public final static String EXIT = "EXIT";

    //exercise 1
    public final static String ECHO = "ECHO";
    public final static String DAYTIME = "DAYTIME";
    public final static String STATS = "STATS";

    //exercise 2
    public final static String EXISTS = "EXISTS";
    public final static String LAST_ACCESSED = "LAST_ACCESSED";
    public final static String LENGTH = "LENGTH";
    public final static String LARGEST = "LARGEST";
    public final static String LINE = "LINE";
    public final static String FILE_NOT_FOUND = "FILE_NOT_FOUND";
    public final static String FOUND = "FOUND";
    public final static String BOUNDS = "OUT_OF_BOUNDS";

    //Math exercise
    public final static String SQUARE = "SQUARE";
    public final static String PLEASE_SUPPLY_NUMBER = "PLEASE_SUPPLY_NUMBER";
    public final static String CUBE = "CUBE";
    public final static String MYLARGEST = "MYLARGEST";

    //Quote Exercise
    public final static String ADD = "ADD";
    public final static String EMPTY = "EMPTY";
    public final static String REMOVE = "REMOVE";
    public final static String ADDED = "ADDED";
    public final static String REMOVED = "REMOVED";
    public final static String GET_QUOTE = "GET_QUOTE";
    public final static String NOT_EXIST = "QUOTE_DOES_NOT_EXIST";
    public final static String ERROR = "ERROR";

}
