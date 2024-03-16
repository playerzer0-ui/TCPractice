package server;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class test {
    public static void main(String[] args) {
        // Create a HashMap
        Map<String, String> quotes = new HashMap<>();
//        quotes.put("key1", "Quote 1");
//        quotes.put("key2", "Quote 2");
//        quotes.put("key3", "Quote 3");
//        quotes.put("key4", "Quote 4");
//        quotes.put("key5", "Quote 5");

        // Get a random key
        String randomKey = getRandomKey(quotes);
        System.out.println("Random key: " + randomKey);
    }

    public static <K, V> K getRandomKey(Map<K, V> map) {
        // Get a set of all keys
        Object[] keys = map.keySet().toArray();

        // Generate a random index
        Random random = new Random();
        int randomIndex = random.nextInt(keys.length);

        // Retrieve and return the key at the random index
        @SuppressWarnings("unchecked")
        K randomKey = (K) keys[randomIndex];
        return randomKey;
    }
}
