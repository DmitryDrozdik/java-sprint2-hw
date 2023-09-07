import java.util.HashMap;
import java.util.Scanner;
import java.util.ArrayList;

class Main {
    static HashMap<String, MonthlyReport> monthlyReports = new HashMap<>();
    static HashMap<String, YearlyReport> yearlyReports = new HashMap<>();
    static FileReader fileReader = new FileReader();

    public static void printMenu () {
        System.out.println("Что необходимо сделать?");
        System.out.println("1 - Считать все месячные отчёты.");
        System.out.println("2 - Считать годовой отчёт.");
        System.out.println("3 - Сверить отчёты.");
        System.out.println("4 - Вывести информацию обо всех месячных отчётах.");
        System.out.println("5 - Вывести информацию о годовом отчёте.");
        System.out.println("0 - Завершить работу с программой.");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String fileNames;

        while (true) {
            printMenu();

            int choice = scanner.nextInt();
            if (choice == 0)
                break;

            switch (choice) {
                case 1:
                    fileNames = "m.202101.csv,m.202102.csv,m.202103.csv";
                    for (String fileName : fileNames.split(","))
                    {
                        ArrayList<String> monthLines = fileReader.readFileContents(fileName);
                        monthLines.remove(0);

                        String month = fileName.substring(6, 7);

                        MonthlyReport monthlyReport = new MonthlyReport(month);
                        monthlyReport.readFromFile(monthLines);

                        monthlyReports.put(month, monthlyReport);
                    }
                    break;

                case 2:
                    fileNames = "y.2021.csv";
                    for (String fileName : fileNames.split(","))
                    {
                        ArrayList<String> yearLines = fileReader.readFileContents(fileName);
                        yearLines.remove(0);

                        String year = fileName.substring(2, 5);

                        YearlyReport yearlyReport = new YearlyReport(year);
                        yearlyReport.readFromFile(yearLines);

                        yearlyReports.put(year, yearlyReport);
                    }
                    break;

                case 3:
//                  1.  is reports
                    if (monthlyReports.isEmpty() || yearlyReports.isEmpty())
                    {
                        System.out.println("Сначала нужно считать данные!");
                        break;
                    }

//                  2. sums
                    HashMap<String, Double> monthlyIncomes = new HashMap<>();
                    HashMap<String, Double> monthlyExpenses = new HashMap<>();

                    for (String month : monthlyReports.keySet()) {
                        MonthlyReport report = monthlyReports.get(month);
                        double income = 0.0, expense = 0.0;

                        for (Transaction t : report.transactions) {
                            if (t.isExpense) {
                                expense += t.getTotal();
                            }
                            else {
                                income += t.getTotal();
                            }
                        }

                        monthlyIncomes.put(month, income);
                        monthlyExpenses.put(month, expense);
                    }

//                  3. equals
                    boolean differenceFound = false;

                    for (YearlyReport yReport : yearlyReports.values()) {
                        for (String month : monthlyIncomes.keySet()) {
                            if (!yReport.incomes.getOrDefault(month, 0.0).equals(monthlyIncomes.get(month)))
                            {
                                System.out.println("Найдено несоответсвие доходов в месяце: " + month);
                                differenceFound = true;
                            }
                            if (!yReport.expenses.getOrDefault(month, 0.0).equals(monthlyExpenses.get(month)))
                            {
                                System.out.println("Найдено несоответсвие расходов в месяце: " + month);
                                differenceFound = true;
                            }
                        }
                    }

//                  4. output
                    if (!differenceFound)
                        System.out.println("Сверка успешно завершена!");
                break;

                case 4:
                    for (MonthlyReport report : monthlyReports.values())
                    {
                        if (report != null)
                        {
                            report.printReport();
                        }
                        else
                        {
                            System.out.println("Сначала нужно считать месячные отчеты!");
                        }
                    }
                    break;

                case 5:
                    for (YearlyReport report : yearlyReports.values())
                    {
                        if (report != null)
                        {
                            report.printReport();
                        }
                        else
                        {
                            System.out.println("Сначала нужно считать годовые отчеты!");
                        }
                    }
                    break;

                default:
                    System.out.println("Неверная опция. Повторите.");
            }
        }
    }
}
