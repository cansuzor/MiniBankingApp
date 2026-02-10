package org.example.banking;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;

public class BankManagement {

    public static boolean createAccount(String name, int passCode){
        Connection con = DBConnection.getConnection();
        if (name.isEmpty() || passCode == 0) {
            System.out.println("All fields are required!");
            return false;
        }

        try {
            String sql = "INSERT INTO customer(cname, balance, pass_code) VALUES (?, 1000, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.setInt(2, passCode);

            int rows = ps.executeUpdate();
            if (rows == 1) {
                System.out.println("New user has been created");
                return true;
            }


        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Use has already been created, try another one");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean loginAccount (String name, int passcode) {
        Connection con = DBConnection.getConnection();
        if(name.isEmpty() || passcode == 0) {
            System.out.println("All fields are required");
            return false;
        }

        try {
            String sql = "SELECT * FROM customer WHERE cname = ? AND pass_code = ?";
            PreparedStatement selectAccountWithNameAndPassword = con.prepareStatement(sql);
            selectAccountWithNameAndPassword.setString(1, name);
            selectAccountWithNameAndPassword.setInt(2, passcode);

            ResultSet rs = selectAccountWithNameAndPassword.executeQuery();
            if (rs.next()) {
                int senderAc = rs.getInt("ac_no"); // logged-in user's account number
                BufferedReader sc = new BufferedReader(new InputStreamReader(System.in));
                int choice;
                System.out.println("\n✅ Hello, " + rs.getString("cname") + "! What would you like to do?");

                while (true) {
                    System.out.println("\n1) Transfer Money");
                    System.out.println("2) View Balance");
                    System.out.println("3) Logout");
                    System.out.print("Enter choice: ");
                    choice = Integer.parseInt(sc.readLine()); //reading what the user wrote 1,2 or 3

                    if (choice == 1) {
                        System.out.print("Enter Receiver Account No: ");
                        int receiverAc = Integer.parseInt(sc.readLine());
                        System.out.print("Enter Amount: ");
                        int amount = Integer.parseInt(sc.readLine());

                        if (transferMoney(senderAc, receiverAc, amount)) {
                            System.out.println("Transaction successful!");
                        } else {
                            System.out.println("Transaction failed!");
                        }
                    } else if (choice == 2) {
                        getBalance(senderAc);
                    } else if (choice == 3) {
                        System.out.println("Logged out successfully!");
                        break;
                    } else {
                        System.out.println("Invalid choice, try again!");
                    }
                }
                return true;
            } else {
                System.out.println("User not found, login failed");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void getBalance(int ac_no) {
        Connection connection = DBConnection.getConnection();

        try {
            String sql = "SELECT * from customer WHERE ac_no = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, ac_no);
            ResultSet resultSet = ps.executeQuery();

            System.out.println("\n-------------------------------------------------");
            System.out.printf("%12s %15s %10s\n", "Account No", "Customer Name", "Balance");

            if (resultSet.next()) {
                System.out.printf("%12d %15s %10d.00\n",
                        resultSet.getInt("ac_no"),
                        resultSet.getString("cname"),
                        resultSet.getInt("balance"));
            }
            System.out.println("-------------------------------------------------");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean transferMoney (int senderAc, int receiverAc, int amount) {
        Connection connection = DBConnection.getConnection();
        if (receiverAc == 0 || amount <= 0) {
            System.out.println("All fields are required!");
            return false;
        }

        try{
            connection.setAutoCommit(false);

            String checkBalance = "SELECT balance FROM customer WHERE ac_no = ?";
            PreparedStatement ps = connection.prepareStatement(checkBalance);
            ps.setInt(1, senderAc);
            ResultSet rs =  ps.executeQuery();

            if(rs.next() && rs.getInt("balance") < amount){
                System.out.println("Insufficient balance");
                return false;
            }else {
                //Debit sender
                String debit = "UPDATE customer SET balance = balance - ? WHERE ac_no = ?";
                PreparedStatement psDebit = connection.prepareStatement(debit);
                psDebit.setInt(1, amount);
                psDebit.setInt(2, senderAc);
                psDebit.executeUpdate();

                //Credit reveiver
                String credit = "UPDATE customer SET balance = balance + ? WHERE ac_no = ?";
                PreparedStatement psCredit = connection.prepareStatement(credit);
                psCredit.setInt(1, amount);
                psCredit.setInt(2, receiverAc);
                psCredit.executeUpdate();
                connection.commit();
                System.out.println("Transaction successful!");
                return true;
            }
        } catch(Exception e){
            try{
                connection.rollback();
                System.out.println("Transaction failed, rollback");
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
            e.printStackTrace();
            return false;
        }
    }

}
