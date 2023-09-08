import java.util.ArrayList;
import java.util.HashMap;

public class MonthlyReport {
    String month;
    final ArrayList<Transaction> transactions = new ArrayList<>();
    boolean isRead = false;

    public boolean isRead() {
        return  isRead;
    }

    public MonthlyReport(String month) {
        this.month = month;
    }

    public void readFromFile(ArrayList<String> lines)
    {
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length != 4) {
                System.out.println("Неверный формат данных в файле!");
                continue;
            }

            String itemName = parts[0];
            boolean isExpense = Boolean.parseBoolean(parts[1]);
            double unitPrice = Double.parseDouble(parts[2]);
            int quantity = Integer.parseInt(parts[3]);

            Transaction transaction = new Transaction(
                    itemName,
                    isExpense,
                    unitPrice,
                    quantity
            );
            transactions.add(transaction);
        }
        isRead = true;
    }

    public void  printReport() {
        if (!isRead) {
            System.out.println("Сначала нужно считать данные!");
            return;
        }

        System.out.println("Название месяца: " + month);

        Transaction maxProfitTransaction = null;
        Transaction maxExpenseTransaction = null;

        for (Transaction transaction : transactions) {
            if (!transaction.isExpense && (maxProfitTransaction == null ||
                    transaction.getTotal() > maxProfitTransaction.getTotal()))
            {
                maxProfitTransaction = transaction;
            }

            if (transaction.isExpense && (maxExpenseTransaction == null ||
                    transaction.getTotal() > maxExpenseTransaction.getTotal()))
            {
                maxExpenseTransaction = transaction;
            }
        }

        if (maxProfitTransaction != null) {
            System.out.print("Самый прибыльный товар: " + maxProfitTransaction.itemName);
            System.out.println(", сумма: " + maxProfitTransaction.getTotal());
        }
        else
        {
            System.out.println("Данные о прибыльных товарах отсутствуют");
        }

        if (maxExpenseTransaction != null) {
            System.out.print("Самая большая трата: " + maxExpenseTransaction.itemName);
            System.out.println(", сумма: " + maxExpenseTransaction.getTotal());
        }
        else
        {
            System.out.println("Данные о расходах отсутствуют");
        }
    }

    public Double getTotalIncome() {
        double income = 0.0;

        for (Transaction t : transactions)
            if (!t.isExpense)
                income += t.getTotal();

        return income;
    }

    public Double getTotalExpense() {
        double expense = 0.0;

        for (Transaction t : transactions)
            if (t.isExpense)
                expense += t.getTotal();

        return expense;
    }
}
