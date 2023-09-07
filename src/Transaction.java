public class Transaction {
    String itemName;
    boolean isExpense;
    double unitPrice;
    int quantity;

    public Transaction(String itemName, boolean isExpense, double unitPrice, int quantity) {
        this.itemName = itemName;
        this.isExpense = isExpense;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public double getTotal() {
        return unitPrice * quantity;
    }
}
