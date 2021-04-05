package com.acme.simulation.saga.util;

/**
 * Utility class
 * Simulates a service call.
 *
 * 1. Delays by a random duration between 0 - 10 seconds
 * 2. Randomly decides whether call is successful or failure - returns true in case of success
 *
 * @successPercent = 100   Means 100% chance of success
 *                   50    Means 50% chance of success
 *                   0     Means 100% chance of failure
 */
public class ServiceSimulator {

    /**
     * Randomly generate true | false indicating success|failure of service
     */
    public static boolean simulate(int successPercent){
        // 1. Simulate processing delay
        long sleepTime = (long)(Math.random()*10000);
        System.out.println("Sleeping for (ms) : " + sleepTime);
        try { Thread.sleep(sleepTime);}catch(Exception e){};

        // Randomly set the flag = false
        int rand = (int)(Math.random()*100);
        System.out.println(rand);
        if(rand < successPercent){
            return true;
        }

        return false;
    }

    /** Unit testing code **/
    public static void main(String[] args){
        int successPercent = 65;
        System.out.println("Success="+simulate(successPercent));
    }
}
