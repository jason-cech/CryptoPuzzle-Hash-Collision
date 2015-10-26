/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cryptopuzzle.hashcode.collision;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author there
 */
public class CryptoPuzzleHashCodeCollision {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Map<Integer, String> store = new HashMap<>();
        Map<Integer, List<String>> collisions = new HashMap<>();
        
        int stringMaxLength = Integer.parseInt(args[0]);
        String str = new String(new char[stringMaxLength]).replace("\0", "a");
        
        while (str.length() <= stringMaxLength) {
            int hash = str.hashCode();
            String prev = store.putIfAbsent(hash, str);
            if (null != prev) {
                if (!collisions.containsKey(hash)) {
                    collisions.put(hash, new ArrayList<>());
                    collisions.get(hash).add(prev);// need to record the first one
                }
                collisions.get(hash).add(str);// all others are recorded here
            }
            
            str = nextIncrement(str);
        }
        
        //collisions.forEach((k,v) -> System.out.println("Collision at " + k + " with a values of " + String.join(", ", v)));
        System.out.println("There were " + collisions.size() + " collisions with strings of length " + stringMaxLength);
        System.out.println("The first 5 are:");
        collisions.entrySet()
                .stream()
                .limit(5)
                .forEach((kvp) -> System.out.println(kvp.getKey() + " -> " + String.join(", ", kvp.getValue())));
    }
    
    private static String nextIncrement(String str) {
        char[] chars = str.toCharArray();
        
        List<Integer> codes = new ArrayList<>(chars.length);
        for (char aChar : chars) {
            codes.add((int)ALPHABET.indexOf(aChar));// the minus ten is to zero out the starting value of 'a'
        }
        
        Collections.reverse(codes);
        
        increment(codes, 0);
        
        Collections.reverse(codes);
        
        String newStr = rebuild(codes);
        
        return newStr;
    }
    
    private static String rebuild(List<Integer> codes) {
        char[] chars = new char[codes.size()];
        for (int i = 0; i < chars.length; i++) {
            char c = (char)(ALPHABET.charAt(codes.get(i)));
            chars[i] = c;
        }
        String s = new String(chars);
        
        return s;
    }
    
    private static void increment(List<Integer> codes, int place) {
        Integer v1 = 0;
        if (codes.size() == place) {
            codes.add(0);
        } else {
            Integer v = codes.get(place);
            v1 = v + 1;
        }
        Integer vMod = v1 % ALPHABET.length();
        boolean carry = v1 / ALPHABET.length() == 1;
        
        codes.set(place, vMod);
        
        if(carry)
            increment(codes, place + 1);
    }
    
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
}
