import java.sql.*;
import java.util.Scanner;

public class User {
    private Connection connection;
    private Scanner scanner;

    public User(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void register() {
        scanner.nextLine();
        System.out.print("Full Name: ");
        String full_name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        if(user_exist(email)){
            System.out.println("User already exist for this Email!");
            return;
        }
        try{
            String querry = "INSERT INTO user(full_name, email, password) values(?,?,?);";
            PreparedStatement ps = connection.prepareStatement(querry);
            ps.setString(1, full_name);
            ps.setString(2, email);
            ps.setString(3, password);
            int rows = ps.executeUpdate();
            if(rows>0){
                System.out.println("Registration Successful!!");
            }
            else {
                System.out.println("Registration Failed!!");
            }
        }
        catch (SQLException e){
            System.out.println(e);
        }
    }

    public String login() {
        scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        String querry = "SELECT * FROM user WHERE email = ? AND password = ?";
       try{
           PreparedStatement ps = connection.prepareStatement(querry);
           ps.setString(1,email);
           ps.setString(2,password);
           ResultSet rs = ps.executeQuery();
           if(rs.next()){
               return email;
           }
           else{
               return null;
           }
       }
       catch (SQLException e){
           System.out.println(e);
       }
       return null;
    }

    public boolean user_exist(String email) {
        try{
            String querry = "SELECT * FROM user WHERE email = ?";
            PreparedStatement ps = connection.prepareStatement(querry);
            ps.setString(1,email);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return true;
            }
            else{
                return false;
            }
        }
        catch (SQLException e){
            System.out.println(e);
        }
        return  false;
    }
}
