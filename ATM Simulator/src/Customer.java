public class Customer {
        private int id;
        private String username;
        private String PIN;
        private double balance;

    public Customer(int id, String username, String pin, double balance) {
        this.id = id;
        this.username = username;
        this.PIN = pin;
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }


    public String getPIN() {
        return PIN;
    }

    public void setPIN(String PIN) {
        this.PIN = PIN;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

}
