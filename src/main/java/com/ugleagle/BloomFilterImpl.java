package com.ugleagle;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class BloomFilterImpl {

    public static void main(String[] args) throws Exception {
        BloomFilter<String> ipFilter = BloomFilter.create(Funnels.stringFunnel(StandardCharsets.UTF_8), 10000, 0.01);

        try (InputStream inputStream = BloomFilterImpl.class.getClassLoader().getResourceAsStream("blacklist.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            if (inputStream == null) {
                System.err.println("blacklist not found");
                return;
            }

            String line;
            while ((line = reader.readLine()) != null) {
                ipFilter.put(line.trim());
            }

            System.out.println("Loaded all blacklisted IPs into Bloom Filter.");
        }

        try (BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Enter an IP address to check (or type 'exit' to quit):");
            String input;
            while (!(input = consoleReader.readLine().trim()).equalsIgnoreCase("exit")) {
        if (ipFilter.mightContain(input)) {
                System.out.println("The IP address " + input + " is blacklisted.");
            } else {
                System.out.println("The IP address " + input + " is not blacklisted.");
            }
            System.out.println("Enter another IP address to check (or type 'exit' to quit):");
            }
        } catch (IOException e) {
            System.err.println("Error reading input: " + e.getMessage());
        }


    }

    // public static void checkAllIp(BloomFilter<String> ipFilter) throws IOException {
    //     try (
    //             InputStream inputStream = BloomFilterImpl.class.getClassLoader().getResourceAsStream("blacklist.txt"); 
    //             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
    //         if (inputStream == null) {
    //             System.err.println("blacklist not found");
    //             return;
    //         }

    //         int count = 0;
    //         String line;
    //         while ((line = reader.readLine()) != null) {
    //             String ip = line.trim();
    //             if (ipFilter.mightContain(ip)) {
    //                 count++;
    //             }
    //         }
    //         System.out.println("Total number of blacklisted IPs: " + count);
    //     }
    // }

}
