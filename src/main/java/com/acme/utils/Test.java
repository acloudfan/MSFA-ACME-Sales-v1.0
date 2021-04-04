package com.acme.utils;

import java.util.HashMap;

public class Test {

    // test
    public static void main(String[] args){

    }

    public String restoreString(String s, int[] indices) {

        //1. Upfront constraints
        //1.1 Check for length
        if(s.length() != indices.length) return null;

        //1.2 Check for indices size
        if(indices.length == 0 || indices.length >= 100) return null;

        //1.3 Assumption: make the string lowercase
        s=s.toLowerCase();

        //1.4 Optiistic approach for indices check




        //2. Declare my data structure
        char[] str = s.toCharArray();
        HashMap<Integer, Character> map = new HashMap<Integer, Character>();

        //3. Carry out the logic & apply runtime constraints
        // Contrsint to be consideredd: Indicise have to be unique

        // 3.1 impt character array and indices array into hashmap
        for (int i = 0; i < indices.length; i++) {
            map.put(indices[i], str[i]);
        }

        // 3.2 sort the indices array in increasing order

        //3.3

        return "";
    }
}
