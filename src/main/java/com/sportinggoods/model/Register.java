package src.main.java.com.sportinggoods.model;

public class Register {
    private double cashBalance;

    /**
     * Process payment of inputted amount with specific paymentMethod
     * @param amount
     * @param paymentMethod
     * @return True if successful and False if payment is not successful
     */
    public boolean processPayment(double amount, String paymentMethod) {
        // Check and process the payment based on method
        System.out.println("Processing payment of $" + amount + " via " + paymentMethod);
        cashBalance += amount;
        return true; // Assume success for simplicity
    }


    /**
     * Issues refund for a return decreasing the current cashBalance by the amount of refund
     * @param amount
     */
    public void issueRefund(double amount) {
        System.out.println("Issuing refund of $" + amount);
        cashBalance -= amount;
    }


    /**
     * Getter method to get the current balance of the cash register
     * @return balance of register in cash
     */
    public double getBalance() {
        return cashBalance;
    }

}
