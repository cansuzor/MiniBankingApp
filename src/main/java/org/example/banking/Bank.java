package org.example.banking;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Bank {
    public static void main(String[] args) {
    BufferedReader sc = new BufferedReader(new InputStreamReader(System.in));

    String name;
    int passCode;
    int choice;

    while(true) {
        //Main menu
        System.out.println("\n===============================");
        System.out.println(" Welcome to InBank ");
        System.out.println("===============================");
        System.out.println("1) Create Account");
        System.out.println("2) Login Account");
        System.out.println("3) Exit");

        try {
            System.out.println("Enter a choice");
            choice = Integer.parseInt(sc.readLine());

            switch (choice) {
                case 1 -> {
                    //Create account
                    System.out.println("Enter a unique Name");
                    name = sc.readLine();
                    System.out.println("Enter a safe passcode");
                    passCode = Integer.parseInt(sc.readLine());

                    if (BankManagement.createAccount(name, passCode)) {
                        System.out.println("You can now login from the main menu");
                    }
                }
                case 2 -> {
                    System.out.println("Enter your login name");
                    name = sc.readLine();
                    System.out.println("Enter your account's passcode");
                    passCode = Integer.parseInt(sc.readLine());

                    if (BankManagement.loginAccount(name, passCode)) {
                        System.out.println("You have successfully logged in");
                    }
                }

                case 3 -> {
                    System.out.println("Thank you for using our mini bank");
                    System.exit(0);
                }

                default -> {
                    System.out.println("Invalid input! Please try again.");
                }
            }
        } catch(Exception e) {
            System.out.println("Please enter a valid input!");
        }
    }
    }
}



