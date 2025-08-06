package com.mycompany;

public class App {
    public static void main(String[] args) {
        String username = "admin";
        String password = "12345"; // hardcoded password - vulnerability
        System.out.println("Logged in as " + username);
    }
}
