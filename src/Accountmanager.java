import java.sql.*;
import java.util.Scanner;

public class  Accountmanager {
    private Connection connection;
    private Scanner scanner;

    public Accountmanager(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void credit_money(long account_number) throws SQLException{
        scanner.nextLine();
        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = scanner.nextLine();
        try{
            connection.setAutoCommit(false);
            if(account_number!=0) {
                PreparedStatement ps = connection.prepareStatement("SELECT * FROM accounts WHERE account_number=? AND security_pin=?");
                ps.setLong(1, account_number);
                ps.setString(2, security_pin);
                ResultSet resultSet = ps.executeQuery();
                if(resultSet.next()){
                    String credit_querry = "UPDATE accounts SET balance = balance + ? WHERE account_number=?";
                    PreparedStatement preparedStatement=connection.prepareStatement(credit_querry);
                    preparedStatement.setDouble(1,amount);
                    preparedStatement.setLong(2,account_number);
                    int rows = preparedStatement.executeUpdate();
                    if(rows>0){
                        System.out.println("Rs."+amount+" Credited Successfully");
                        connection.commit();
                        connection.setAutoCommit(true);
                        return;
                    }
                    else{
                        System.out.println("Transaction Failed!");
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }
                }
                else{
                    System.out.println("Invalid Security Pin!");
                }
            }
        }
        catch(SQLException e){
            System.out.println(e);
        }
        connection.setAutoCommit(true);
    }

    public void debit_money(long account_number) throws SQLException{
        scanner.nextLine();
        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = scanner.nextLine();
        try{
            connection.setAutoCommit(false);
            if(account_number!=0){
                PreparedStatement ps = connection.prepareStatement("SELECT * FROM accounts WHERE account_number=? AND security_pin=?");
                ps.setLong(1,account_number);
                ps.setString(2,security_pin);
                ResultSet resultSet = ps.executeQuery();
                if(resultSet.next()){
                    double current_balance = resultSet.getDouble("balance");
                    if(amount<=current_balance){
                        String debit_querry = "UPDATE accounts SET balance = balance - ? WHERE account_number=?";
                        PreparedStatement preparedStatement=connection.prepareStatement(debit_querry);
                        preparedStatement.setDouble(1,amount);
                        preparedStatement.setLong(2,account_number);
                        int rows = preparedStatement.executeUpdate();
                        if(rows>0){
                            System.out.println("Rs."+amount+" Debited Successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        }
                        else{
                            System.out.println("Transaction Failed!");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }
                    else{
                        System.out.println("Insufficient Balance!");
                    }
                }
                else{
                    System.out.println("Invalid Security Pin!");
                }
            }
        }
        catch(SQLException e){
            System.out.println(e);
        }
        connection.setAutoCommit(true);
    }

    public void transfer_money(long sender_account_number) throws SQLException{
        scanner.nextLine();
        System.out.print("Enter Receiver Account Number: ");
        long receiver_account_number = scanner.nextLong();
        scanner.nextLine();
        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = scanner.nextLine();
        try {
            connection.setAutoCommit(false);
            if(sender_account_number!=0 && receiver_account_number!=0){
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE account_number = ? AND security_pin = ?");
                preparedStatement.setLong(1,sender_account_number);
                preparedStatement.setString(2,security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()){
                    double current_balance = resultSet.getDouble("balance");
                    if(amount<=current_balance){
                        PreparedStatement credit_preparedStatement = connection.prepareStatement("UPDATE accounts SET balance = balance + ? WHERE account_number=?");
                        PreparedStatement debit_preparedStatement = connection.prepareStatement("UPDATE accounts SET balance = balance - ? WHERE account_number=?");
                        credit_preparedStatement.setDouble(1,amount);
                        credit_preparedStatement.setLong(2,receiver_account_number);
                        debit_preparedStatement.setDouble(1,amount);
                        debit_preparedStatement.setLong(2,sender_account_number);
                        int credit_row = credit_preparedStatement.executeUpdate();
                        int debit_row = debit_preparedStatement.executeUpdate();
                        if(credit_row>0 && debit_row>0){
                            System.out.println("Transaction Successful!");
                            System.out.println("Rs."+amount+" Transferred Successfully!");
                            connection.commit();
                            connection.setAutoCommit(true);
                        }
                        else{
                            System.out.println("Transaction Failed!");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }
                    else{
                        System.out.println("Insufficient balance!");
                    }
                }
                else{
                    System.out.println("Invalid Security Pin!");
                }
            }
        }
        catch(SQLException e){
            System.out.println(e);
        }
        connection.setAutoCommit(true);

    }

    public void getBalance(long account_number){
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin=scanner.nextLine();
        try{
            PreparedStatement preparedStatement=connection.prepareStatement("SELECT balance FROM accounts WHERE account_number = ? AND security_pin = ?");
            preparedStatement.setLong(1,account_number);
            preparedStatement.setString(2,security_pin);
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                double balance=resultSet.getDouble("balance");
                System.out.println("balance: "+balance);
            }
            else{
                System.out.println("Invalid Security Pin");
            }
        }
        catch (SQLException e){
            System.out.println(e);
        }
    }
}
