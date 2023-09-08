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

            int input_choice = scanner.nextInt();
            if (input_choice == 0)
                break;

            if (input_choice > 5 || input_choice < 0) {
                System.out.println("Неверная опция. Повторите.");
                continue;
            }

            Choice choice = Choice.values()[--input_choice];

            switch (choice) {
                case MONTHLY_READ:
                    fileNames = "m.202101.csv,m.202102.csv,m.202103.csv";
                    for (String fileName : fileNames.split(","))
                    {
                        ArrayList<String> monthLines = fileReader.readFileContents(fileName);
                        monthLines.remove(0);

                        String month = fileName.substring(6, 8);

                        MonthlyReport monthlyReport = new MonthlyReport(month);
                        monthlyReport.readFromFile(monthLines);

                        monthlyReports.put(month, monthlyReport);
                    }
                    break;

                case YEARLY_READ:
                    fileNames = "y.2021.csv";
                    for (String fileName : fileNames.split(","))
                    {
                        ArrayList<String> yearLines = fileReader.readFileContents(fileName);
                        yearLines.remove(0);

                        String year = fileName.substring(2, 6);

                        YearlyReport yearlyReport = new YearlyReport(year);
                        yearlyReport.readFromFile(yearLines);

                        yearlyReports.put(year, yearlyReport);
                    }
                    break;

                case VERIFY:
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

                        monthlyIncomes.put(month, report.getTotalIncome());
                        monthlyExpenses.put(month, report.getTotalExpense());
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

                case MONTHLY_PRINT:
                    if (monthlyReports.isEmpty())
                    {
                        System.out.println("Сначала нужно считать месячные отчеты!");
                        break;
                    }

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

                case YERLY_PRINT:
                    if (yearlyReports.isEmpty())
                    {
                        System.out.println("Сначала нужно считать годовые отчеты!");
                        break;
                    }

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
            }
        }
    }
}
