import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util.println;


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

            double amountOfMiddleMonth = 0;
            for (LocalDate date = LocalDate.of(period.start.getYear(), period.start.getMonth(), 1); date.compareTo(period.end) <= 0; date = date.plusMonths(1)) {
                String month = date.getYear() + String.format("%02d", date.getMonthValue());
                Budget budget = getBudget(month);

                if (budget != null) {
                    long overlappingDays = period.overlappingDays(budget.createPeriod());

                    amountOfMiddleMonth += overlappingDays * budget.getDailyAmount();
                }
            }
            totalAmount += amountOfMiddleMonth;

        }

        return totalAmount;

    }

    private boolean isStartMonth(LocalDate date, LocalDate start) {
        return date.compareTo(LocalDate.of(start.getYear(), start.getMonth(), 1)) == 0;
    }

    private double amountOfSingleMonth(Period period) {
        double amountOfSingleMonth = 0;
        String startYM = (period.start.getYear()) + "0" + period.start.getMonthValue();
        Budget budget = getBudget(startYM);
        if (budget != null) {
            Double bugetStartMonthperDay = budget.getDailyAmount();
            int days = period.start.lengthOfMonth() - period.start.getDayOfMonth() + 1;
            long diffDate = days(period.start, period.end);
            if (bugetStartMonthperDay != null) {
                amountOfSingleMonth = diffDate * bugetStartMonthperDay;
            }

        }
        return amountOfSingleMonth;
    }

    private double amountOfLastMonth(Period period) {
        double amountOfLastMonth = 0;
        String month = (period.end.getYear()) + String.format("%02d", period.end.getMonthValue());
        Budget budget = getBudget(month);

        if (budget != null) {
            LocalDate effectiveStart = budget.firstDate();
            LocalDate effectiveEnd = period.end;
            long effectiveDays = days(effectiveStart, effectiveEnd);
            amountOfLastMonth += effectiveDays * budget.getDailyAmount();
        }
        return amountOfLastMonth;
    }

    private long days(LocalDate effectiveStart, LocalDate effectiveEnd) {
        return Duration.between(effectiveStart.atStartOfDay(), effectiveEnd.atStartOfDay()).toDays() + 1;
    }

    private double amountOfMiddleMonth(Period period) {
        double amountOfMiddleMonth = 0;
        LocalDate loopStartDate = LocalDate.of(period.start.getYear(), period.start.getMonth(), 1).plusMonths(1);
        LocalDate loopEndartDate = LocalDate.of(period.end.getYear(), period.end.getMonth(), 1);
        for (LocalDate date = loopStartDate; date.compareTo(loopEndartDate) < 0; date = date.plusMonths(1)) {
            String month = date.getYear() + String.format("%02d", date.getMonthValue());
            Budget budget = getBudget(month);

            if (budget != null) {
                LocalDate effectiveStart = budget.firstDate();
                LocalDate effectiveEnd = budget.lastDate();

                long effectiveDays = days(effectiveStart, effectiveEnd);
                amountOfMiddleMonth += effectiveDays * budget.getDailyAmount();
            }
        }
        return amountOfMiddleMonth;
    }

    private double amountOfFirstMonth(Period period) {
        double amountOfFirstMonth = 0;
        String startYM = (period.start.getYear()) + String.format("%02d", period.start.getMonthValue());
        Budget budget = getBudget(startYM);

        if (budget != null) {
            LocalDate effectiveStart = period.start;
            LocalDate effectiveEnd = budget.lastDate();
            long effectiveDays = days(effectiveStart, effectiveEnd);
            amountOfFirstMonth = effectiveDays * budget.getDailyAmount();
        }
        return amountOfFirstMonth;
    }

    private boolean hasBudget(String month) {
        return budgetRepo.getAll().stream().anyMatch(it -> it.yearMonth.equals(month));
    }

    private Budget getBudget(String month) {
        return budgetRepo.getAll().stream().filter(it -> it.yearMonth.equals(month)).findFirst().orElse(null);
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

}