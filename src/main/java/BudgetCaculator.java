

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

public class BudgetCaculator {
    private IBudgetRepo budgetRepo;
    private double totalAmount;

    public BudgetCaculator(IBudgetRepo budgetRepo) {

        this.budgetRepo = budgetRepo;
    }

    public double totalAmount(LocalDate start, LocalDate end) {

        List<Budget> budgetList = budgetRepo.getAll();

        Period period = new Period(start, end);
        period.start = start;
        period.end = end;

        //Same month
        if (isSameMonth(period)) {
            totalAmount += amountOfSingleMonth(period);

        } else {
            totalAmount += amountOfFirstMonth(period);

            totalAmount += amountOfMiddleMonth(period);

            totalAmount += amountOfLastMonth(period);
        }

        return totalAmount;

    }

    private double amountOfSingleMonth(Period period) {
        double amountOfSingleMonth = 0;
        String startYM = (period.start.getYear()) + "0" + period.start.getMonthValue();
        Double bugetStartMonthperDay = getDailyAmount(startYM);
        int days = period.start.lengthOfMonth() - period.start.getDayOfMonth() + 1;
        long diffDate = Duration.between(period.start.atStartOfDay(), period.end.atStartOfDay()).toDays() + 1;
        if (bugetStartMonthperDay != null) {
            amountOfSingleMonth = diffDate * bugetStartMonthperDay;
        }
        return amountOfSingleMonth;
    }

    private double amountOfLastMonth(Period period) {
        double amountOfLastMonth = 0;
        String month = (period.end.getYear()) + String.format("%02d", period.end.getMonthValue());
        if (hasBudget(month)) {
            Double dailyAmount = getDailyAmount(month);
            int days = period.end.getDayOfMonth();
            amountOfLastMonth += days * dailyAmount;
        }
        return amountOfLastMonth;
    }

    private double amountOfMiddleMonth(Period period) {
        double amountOfMiddleMonth = 0;
        LocalDate loopStartDate = LocalDate.of(period.start.getYear(), period.start.getMonth(), 1).plusMonths(1);
        LocalDate loopEndartDate = LocalDate.of(period.end.getYear(), period.end.getMonth(), 1);
        for (LocalDate date = loopStartDate; date.compareTo(loopEndartDate) < 0; date = date.plusMonths(1)) {
            String month = date.getYear() + String.format("%02d", date.getMonthValue());
            if (hasBudget(month)) {
                Double dailyAmount = getDailyAmount(month);

                int days = date.lengthOfMonth();
                amountOfMiddleMonth += days * dailyAmount;
            }
        }
        return amountOfMiddleMonth;
    }

    private double amountOfFirstMonth(Period period) {
        double amountOfFirstMonth = 0;
        String startYM = (period.start.getYear()) + String.format("%02d", period.start.getMonthValue());
        if (hasBudget(startYM)) {
            Double dailyAmount = getDailyAmount(startYM);
            int days = period.start.lengthOfMonth() - period.start.getDayOfMonth() + 1;
            amountOfFirstMonth = days * dailyAmount;
        }
        return amountOfFirstMonth;
    }

    private Double getDailyAmount(String month) {
        Budget budget = budgetRepo.getAll().stream().filter(it -> it.yearMonth.equals(month)).findFirst().orElse(null);
        if (budget != null) {
            LocalDate budgetDate = createMonthPerDay(budget);
            return calculateBudgetPerMonth(budget, budgetDate);
        }
        return 0.0;
    }

    private boolean hasBudget(String month) {
        return budgetRepo.getAll().stream().anyMatch(it -> it.yearMonth.equals(month));
    }

    private boolean isSameMonth(Period period) {
        return period.start.getMonthValue() == period.end.getMonthValue();
    }

    private LocalDate createMonthPerDay(Budget budget) {
        return LocalDate.of(Integer.parseInt(budget.yearMonth.substring(0, 4)), Integer.parseInt(budget.yearMonth.substring(4)), 1);
    }

    private double calculateBudgetPerMonth(Budget budget, LocalDate budgetFirstDay) {
        int daysOfMonth = budgetFirstDay.lengthOfMonth();

        return budget.budgetAmount / daysOfMonth;
    }

    private boolean isNotSameDay(LocalDate start_day, LocalDate end_day) {
        return start_day.compareTo(end_day) != 0;
    }

    private class Period {
        LocalDate start;
        LocalDate end;

        private Period(LocalDate start, LocalDate end) {
            this.start = start;
            this.end = end;
        }

    }
}
