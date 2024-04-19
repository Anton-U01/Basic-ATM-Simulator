import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ATMSystem {
        private static int CUSTOMERS_COUNT = 0;
        private List<Customer> customers;
        private Customer currentLogged;
        private int loginAttempts;

        private static final int MAX_LOGIN_ATTEMPTS = 3;

        private Scanner scanner;

        public ATMSystem() {
                customers = new ArrayList<>();
                currentLogged = null;
                scanner = new Scanner(System.in);
                loginAttempts = 1;
        }

        public void start() throws Exception {
                while (true) {

                        printHomeText();

                        String input = scanner.nextLine();
                        switch (input) {
                                case "1":
                                        startRegistration();
                                        break;
                                case "2":
                                        login();
                                        break;
                                default:
                                        System.out.println("----------------------------------------");
                                        System.out.println("Invalid choice. Please try again.");
                                        System.out.println("----------------------------------------");
                                        break;
                        }
                        while (currentLogged != null) {
                                displayMenu();
                                input = scanner.nextLine();
                                switch (input) {
                                        case "1":
                                                withdrawMoney();
                                                break;
                                        case "2":
                                                depositMoney();
                                                break;
                                        case "3":
                                                checkBalance();
                                                break;
                                        case "4":
                                                logOut();
                                                break;
                                        default:
                                                System.out.println("----------------------------------------");
                                                System.out.println("Invalid choice. Please try again.");
                                                System.out.println("----------------------------------------");
                                                break;
                                }
                        }
                }
        }

        private void logOut() {
                currentLogged = null;
                System.out.println("----------------------------------------");
                System.out.println("Successfully logged out from the account!");
                System.out.println("----------------------------------------");
        }

        private void checkBalance() {
                System.out.println("----------------------------------------");
                System.out.printf("Your current account balance is: %.2f$\n",currentLogged.getBalance());
                System.out.println("----------------------------------------");
        }

        private void depositMoney() {
                System.out.println("ENTER DEPOSIT AMOUNT:");
                String depositAmountStr = scanner.nextLine();
                if(checkDeposit(depositAmountStr)){
                        double depositAmount = Double.parseDouble(depositAmountStr);
                        currentLogged.setBalance(currentLogged.getBalance() + depositAmount);
                        System.out.println("----------------------------------------");
                        System.out.printf("Successfully deposited %.2f$!\n",depositAmount);
                        System.out.println("----------------------------------------");
                }
        }

        private void withdrawMoney() {
                System.out.println("ENTER AMOUNT FOR WITHDRAW:");
                String amountForWithdrawStr= scanner.nextLine();
                if(checkPossibleWithdraw(amountForWithdrawStr)){
                        double withdrawAmount = Double.parseDouble(amountForWithdrawStr);
                        currentLogged.setBalance(currentLogged.getBalance() - withdrawAmount);
                        System.out.println("----------------------------------------");
                        System.out.printf("Successfully withdrawn %.2f$!\n",withdrawAmount);
                        System.out.println("----------------------------------------");
                }
        }

        private boolean checkPossibleWithdraw(String amountForWithdrawStr) {
                if(isNumeric(amountForWithdrawStr)){
                        double withdrawAmount = Double.parseDouble(amountForWithdrawStr);
                        if(currentLogged.getBalance() - withdrawAmount >= 0){
                                return true;
                        }
                        System.out.println("----------------------------------------");
                        System.out.println("Not enough balance!!!");
                        System.out.println("----------------------------------------");
                        return false;
                }
                withdrawMoney();
                return false;
        }

        private void displayMenu() {
                System.out.println("Additional functions:");
                System.out.println("1 - Withdraw");
                System.out.println("2 - Deposit");
                System.out.println("3 - Check Balance");
                System.out.println("4 - Log out");
        }

        private void login() throws Exception {
                System.out.println("ENTER USERNAME");
                String username = scanner.nextLine();
                while (!checkUsernameIfExistsByLogin(username)){
                        username = scanner.nextLine();
                        loginAttempts++;
                        if(loginAttempts >= MAX_LOGIN_ATTEMPTS){
                                loginAttempts = 1;
                                System.out.println("----------------------------------------");
                                System.out.println("Maximum login attempts exceeded. Please register.");
                                System.out.println("----------------------------------------");
                                start();
                        }
                }
                System.out.println("ENTER PIN");
                Customer customer = findCustomerByUsername(username);
                String pin = scanner.nextLine();
                while (!checkCorrectPin(username,pin)){
                        pin = scanner.nextLine();
                        loginAttempts++;
                        if(loginAttempts >= MAX_LOGIN_ATTEMPTS){
                                loginAttempts = 1;
                                System.out.println("----------------------------------------");
                                System.out.println("Maximum login attempts exceeded. Please try later.");
                                System.out.println("----------------------------------------");
                                start();
                        }
                }
                currentLogged = customer;
                printLoggedInText();
        }

        private boolean checkCorrectPin(String username, String pin) {
                Customer customer = findCustomerByUsername(username);
                if(customer != null){
                        if(customer.getPIN().equals(pin)){
                                return true;
                        }
                }
                System.out.println("----------------------------------------");
                System.out.println("Password is not correct!");
                System.out.println("Try again!");
                System.out.println("----------------------------------------");
                return false;
        }
        private Customer findCustomerByUsername(String username){
                for (Customer customer : customers) {
                        if(customer.getUsername().equals(username)){
                                return customer;
                        }
                }
                return null;
        }

        private void startRegistration() throws Exception {
                System.out.println("ENTER USERNAME(at least 5 symbols)");
                String username = scanner.nextLine();
                while (!checkUsernameForRegister(username)){
                        username = scanner.nextLine();
                }
                System.out.println("ENTER PIN(4 digits)");
                String PIN = scanner.nextLine();
                while (!checkPIN(PIN)){
                        PIN = scanner.nextLine();
                }
                Customer customer = new Customer(++CUSTOMERS_COUNT,username,PIN,0.0);
                register(customer);
                currentLogged = customer;
                printLoggedInText();
        }

        private void printLoggedInText() {
                System.out.println("----------------------------------------");
                System.out.printf("WELCOME %s!\n",currentLogged.getUsername());
                System.out.println("----------------------------------------");
        }

        private static boolean isNumeric(String strNum) {
                if (strNum == null) {
                        return false;
                }
                try {
                        double d = Double.parseDouble(strNum);
                } catch (NumberFormatException nfe) {
                        System.out.println("----------------------------------------");
                        System.out.println("Invalid input! Please enter a valid numeric amount.");
                        System.out.println("----------------------------------------");
                        return false;
                }
                return true;
        }

        private boolean checkDeposit(String balanceStr) {
                if(isNumeric(balanceStr)){
                        double balance = Double.parseDouble(balanceStr);
                        if(balance <= 0){
                                System.out.println("----------------------------------------");
                                System.out.println("THE DEPOSIT MUST BE A POSITIVE NUMBER!");
                                System.out.println("----------------------------------------");
                                return false;
                        }
                        return true;
                }
                depositMoney();
                return false;
        }


        private static void printHomeText() {
                System.out.print("""
                        WELCOME TO THE ATM!
                        Select an option:
                        1 - REGISTRATION
                        2 - LOGIN
                        """);
        }

        public void register(Customer customer) throws Exception {
                this.customers.add(customer);
                System.out.println("----------------------------------------");
                System.out.printf("Customer %s is successfully registered!\n",customer.getUsername());
                System.out.println("----------------------------------------");
        }

        private boolean checkPIN(String PIN) {
                String pinRegex = "\\d{4}";
                if (!PIN.matches(pinRegex)) {
                        System.out.println("----------------------------------------");
                        System.out.println("PIN must contain exactly 4 digits.");
                        System.out.println("----------------------------------------");
                        return false;
                }
                return true;
        }

        private boolean checkUsernameForRegister(String username) throws Exception {
                if(username.length() < 5){
                        System.out.println("----------------------------------------");
                        System.out.println("Try again!Username must contain at least 5 symbols!");
                        System.out.println("----------------------------------------");
                        return false;
                }
                if(findCustomerByUsername(username) != null){
                        System.out.println("----------------------------------------");
                        System.out.println("Try again!Username already exists!");
                        System.out.println("----------------------------------------");
                        return false;
                }
                return true;
        }

        private boolean checkUsernameIfExistsByLogin(String username){
                if(findCustomerByUsername(username) != null){
                        return true;
                }
                System.out.println("----------------------------------------");
                System.out.println("This username can not be found! Try again!");
                System.out.println("----------------------------------------");
                return false;
        }
}
