import java.util.ArrayList;
import java.util.HashMap;

public class YearlyReport {
    String year;
    final HashMap<String, Double> incomes = new HashMap<>();
    final HashMap<String, Double> expenses = new HashMap<>();
    boolean isRead = false;

    public YearlyReport(String year) {
        this.year = year;
    }

    public boolean isRead() {
        return isRead;
    }

    public void readFromFile(ArrayList<String> lines) {
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length != 3) {
                System.out.println("Неверный формат данных в файле!");
                continue;
            }

            String month = parts[0];
            double amount = Double.parseDouble(parts[1]);
            boolean isExpense = Boolean.parseBoolean(parts[2]);

            if (isExpense) {
                expenses.put(month, expenses.getOrDefault(month, 0.0) + amount);
            }
            else {
                incomes.put(month, amount + incomes.getOrDefault(month, 0.0));
            }
        }
        isRead = true;
    }

    public void printReport() {
        if (!isRead) {
            System.out.println("Сначала нужно считать данные!");
            return;
        }

        System.out.println("Рассматриваемый год: " + year);

        System.out.println("Прибыль по месяцам:");
        for (String month : incomes.keySet()) {
            System.out.println("Месяц: " + month + ", прибыль: " + incomes.get(month));
        }

        double totalExpense = expenses.values().stream().mapToDouble(Double::doubleValue).sum();
        double avarageExpense = totalExpense / expenses.size();

        double totalIncome = incomes.values().stream().mapToDouble(Double::doubleValue).sum();
        double avarageIncome = totalIncome / incomes.size();

        System.out.println("Средний расход за год: " + avarageExpense);
        System.out.println("Средний доход за год: " + avarageIncome);
    }
}
