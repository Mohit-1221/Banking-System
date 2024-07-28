import java.sql.*;
import java.util.Scanner;

public class BankingApp {
    private static final String url = "jdbc:mysql://localhost:3306/Banking_System";
    private static final String username = "root";
    private static final String password = "mohit@12rohit";

    public static void main(String[] args){
       try{
           Class.forName("com.mysql.cj.jdbc.Driver");

           Connection connection = DriverManager.getConnection(url,username,password);
           Scanner scanner = new Scanner(System.in);
           User user = new User(connection, scanner);
           Accounts accounts = new Accounts(connection, scanner);
           Accountmanager accountmanager = new Accountmanager(connection, scanner);

           String email;
           long account_number;

           while(true){
               System.out.println("**** Welcome to Banking System ******");
               System.out.println();
               System.out.println("1: Register");
               System.out.println("2: Login");
               System.out.println("3: Exit");
               System.out.print("Enter your choice: ");
               int choice1 = scanner.nextInt();
               switch (choice1){
                   case 1:
                       user.register();
                       break;
                   case 2:
                       email = user.login();
                       if(email!=null){
                           System.out.println();
                           System.out.println("User Logged In");
                           if(!accounts.account_exist(email)){
                               System.out.println("1. Open a New Bank Account");
                               System.out.println("2. Exit");
//                               System.out.print("Enter Your Choice: ");
//                               int choice2=scanner.nextInt();
                               if(scanner.nextInt()==1){
                                   account_number = accounts.open_account(email);
                                   System.out.println("Account Created Successfully!!");
                                   System.out.println("Your Account Number is: "+account_number);
                               }
                               else {
                                   break;
                               }
                           }
                           account_number = accounts.getAccountNumber(email);
                           int choice3=0;
                           while (choice3!=5){
                               System.out.println("1. Debit Money");
                               System.out.println("2. Credit Money");
                               System.out.println("3. Transfer Money");
                               System.out.println("4. Check Balance");
                               System.out.println("5. Log Out");
                               System.out.print("Enter Your Choice: ");
                               choice3 = scanner.nextInt();
                               switch (choice3){
                                   case 1:
                                       accountmanager.debit_money(account_number);
                                       break;
                                   case 2:
                                       accountmanager.credit_money(account_number);
                                       break;
                                   case 3:
                                       accountmanager.transfer_money(account_number);
                                       break;
                                   case 4:
                                       accountmanager.getBalance(account_number);
                                       break;
                                   case 5:
                                       break;
                                   default:
                                       System.out.println("Enter Valid Choice!");
                                       break;
                               }
                           }
                       }
                       else{
                           System.out.println("Incorrect Email and Password!");
                       }
                   case 3:
                       System.out.println("THANK YOU FOR USING BANKING SYSTEM!!");
                       System.out.println("Exiting System!");
                       return;
                   default:
                       System.out.println("Enter Valid Choice!");
                       break;
               }
           }
       }
       catch (Exception e){
           System.out.println(e);
       }

    }
}
