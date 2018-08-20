

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util.println;


public class BudgetCaculator {
    private IBudgetRepo budgetRepo;
    private Map<String, Double> mLookUpTable;
    private double totalAmount;

    public BudgetCaculator(IBudgetRepo budgetRepo) {

        this.budgetRepo = budgetRepo;
    }

    public double totalAmount(LocalDate start, LocalDate end) {

        List<Budget> budgetList = budgetRepo.getAll();

        Period period = new Period(start, end);
        period.start = start;
        period.end = end;

        mLookUpTable = new HashMap<String, Double>();

        if (budgetList.size() > 0) {
            for (Budget budget : budgetList) {
                LocalDate localFirstDay = createMonthPerDay(budget);
                double bugetperday = calculateBudgetPerMonth(budget, localFirstDay);
                mLookUpTable.put(budget.yearMonth, bugetperday);
            }
        }

        //Same month
        if (isSameMonth(period)) {
            String startYM = (period.start.getYear()) + "0" + period.start.getMonthValue();
            Double bugetStartMonthperDay = mLookUpTable.get(startYM);
            int days = period.start.lengthOfMonth() - period.start.getDayOfMonth() + 1;
            long diffDate = Duration.between(period.start.atStartOfDay(), period.end.atStartOfDay()).toDays() + 1;
            if (bugetStartMonthperDay != null) {
                totalAmount = diffDate * bugetStartMonthperDay;
            }

        } else {
            //Check Start day in DB

            String startYM = (period.start.getYear()) + String.format("%02d", period.start.getMonthValue());
            if (mLookUpTable.containsKey(startYM)) {
                Double bugetStartMonthperDay = mLookUpTable.get(startYM);
                int days = period.start.lengthOfMonth() - period.start.getDayOfMonth() + 1;
                totalAmount = days * bugetStartMonthperDay;
            }

            //中間的月份
            LocalDate loopStartDate = LocalDate.of(period.start.getYear(), period.start.getMonth(), 1).plusMonths(1);
            LocalDate loopEndartDate = LocalDate.of(period.end.getYear(), period.end.getMonth(), 1);
            for (LocalDate date = loopStartDate; date.compareTo(loopEndartDate) < 0; date = date.plusMonths(1)) {
                String middleYM = date.getYear() + String.format("%02d", date.getMonthValue());
                if (mLookUpTable.containsKey(middleYM)) {
                    Double bugetMiddleMonthperDay = mLookUpTable.get(middleYM);

                    int middleDays = date.lengthOfMonth();
                    totalAmount += middleDays * bugetMiddleMonthperDay;
                }
            }

            String endYM = (period.end.getYear()) + String.format("%02d", period.end.getMonthValue());
            if (mLookUpTable.containsKey(endYM)) {
                Double bugetEndMonthofDay = mLookUpTable.get(endYM);
                int endday = period.end.getDayOfMonth();
                if (bugetEndMonthofDay != null) {
                    totalAmount += endday * bugetEndMonthofDay;
                }
            }

        }

        return totalAmount;

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
