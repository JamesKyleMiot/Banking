import java.util.Scanner;

public class InputValidator {
    
    public String getValidName(Scanner scanner) {
        String name = "";
        boolean valid = false;
        
        while (!valid) {
            System.out.print("Enter Fullname: ");
            name = scanner.nextLine();
            
            if (name.trim().isEmpty()) {
                System.out.println("Error: Fullname cannot be empty!");
            } else if (name.matches("[a-zA-Z ]+")) {
                valid = true;
            } else {
                System.out.println("Error: Fullname must contain only letters and spaces! (No numbers or special characters)");
            }
        }
        return name;
    }
    
    public int getValidAge(Scanner scanner) {
        int age = 0;
        boolean valid = false;
        
        while (!valid) {
            System.out.print("Enter Age: ");
            String ageInput = scanner.nextLine();
            
            if (ageInput.matches("\\d+")) {
                age = Integer.parseInt(ageInput);
                if (age >= 18 && age <= 120) {
                    valid = true;
                } else if (age < 18) {
                    System.out.println("Error: You must be at least 18 years old to open an account!");
                } else {
                    System.out.println("Error: Please enter a valid age!");
                }
            } else {
                System.out.println("Error: Age must be a number!");
            }
        }
        return age;
    }
    
    public String getValidAddress(Scanner scanner) {
        String address = "";
        boolean valid = false;
        
        while (!valid) {
            System.out.print("Enter Address: ");
            address = scanner.nextLine();
            
            if (address.trim().isEmpty()) {
                System.out.println("Error: Address cannot be empty!");
            } else {
                valid = true;
            }
        }
        return address;
    }
    
    public String getValidGmail(Scanner scanner) {
        String gmail = "";
        boolean valid = false;
        
        while (!valid) {
            System.out.print("Enter Gmail: ");
            gmail = scanner.nextLine();
            
            if (gmail.matches("^[a-zA-Z0-9._%+-]+@gmail\\.com$")) {
                valid = true;
            } else {
                System.out.println("Error: Please enter a valid Gmail address (example@gmail.com)");
            }
        }
        return gmail;
    }
    
    public String getValidTelephone(Scanner scanner) {
        String telephone = "";
        boolean valid = false;
        
        while (!valid) {
            System.out.print("Enter Telephone (11 digits): ");
            telephone = scanner.nextLine();
            
            if (telephone.matches("\\d{11}")) {
                valid = true;
            } else {
                System.out.println("Error: Telephone must be exactly 11 digits!");
            }
        }
        return telephone;
    }
    
    public int getValidPIN(Scanner scanner) {
        int pin = 0;
        boolean valid = false;
        
        while (!valid) {
            System.out.print("Create a 6-digit PIN: ");
            String pinInput = scanner.nextLine();
            
            if (pinInput.matches("\\d+")) {
                if (pinInput.length() == 6) {
                    pin = Integer.parseInt(pinInput);
                    valid = true;
                } else {
                    System.out.println("Error: PIN must be exactly 6 digits! You entered " + pinInput.length() + " digits.");
                }
            } else {
                System.out.println("Error: PIN must contain only numbers!)");
            }
        }
        return pin;
    }
}