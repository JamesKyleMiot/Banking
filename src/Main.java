import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BankSystem bankSystem = new BankSystem(scanner);
        
        System.out.println("<===== CAGUIOA BANK =====>\n");
        System.out.println("The Choice of the People");
        System.out.println("With Only 5% Loan Interest");
        
        bankSystem.run();
        
       
    }
}