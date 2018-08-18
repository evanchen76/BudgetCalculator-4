import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BudgetCaculatorTest {

    private BudgetCaculator budgetCaculator;

    @Before
    public void setup() {
        IBudgetRepo budgetRepo = givenBudgetRepo();
        budgetCaculator = new BudgetCaculator(budgetRepo);
    }

    @Test
    public void same_day() {

        totalAmountShouldBe("20180101", "20180101", 10.00);
    }

    @Test
    public void range_two_days() {

        totalAmountShouldBe("20180101", "20180102", 20.00);
    }

    @Test
    public void full_month() {
        totalAmountShouldBe("20180101", "20180131", 310.00);
    }

    @Test
    public void cross_month() {
        totalAmountShouldBe("20180131", "20180201", 10.00);
    }

    @Test
    public void two_different_months() {
        totalAmountShouldBe("20180331", "20180401", 220.00);
    }


    @Test
    public void donotoverlap() {

        totalAmountShouldBe("20180202", "20180205", 0);
    }

    @Test
    public void multiple_months() {
        totalAmountShouldBe("20180331", "20180501", 820.00);

    }

    @Test
    public void multiple_months_with_some_month_no_budget() {

        totalAmountShouldBe("20180201", "20180601", 7420.00);
    }

    private void totalAmountShouldBe(String start_day, String end_day, double v) {
        LocalDate start = LocalDate.of(Integer.parseInt(start_day.substring(0, 4)), Integer.parseInt(start_day.substring(4, 6)), Integer.parseInt(start_day.substring(6)));
        LocalDate end = LocalDate.of(Integer.parseInt(end_day.substring(0, 4)), Integer.parseInt(end_day.substring(4, 6)), Integer.parseInt(end_day.substring(6)));
        double budget = budgetCaculator.totalAmount(start, end);
        Assert.assertEquals(v, budget, 0.001);
    }

    private IBudgetRepo givenBudgetRepo() {
        return new IBudgetRepo() {
            @Override
            public List<Budget> getAll() {
                Budget budget = new Budget();
                budget.yearMonth = "201801";
                budget.budgetAmount = 310;
                Budget budget2 = new Budget();
                budget2.yearMonth = "201803";
                budget2.budgetAmount = 6200;
                Budget budget3 = new Budget();
                budget3.yearMonth = "201804";
                budget3.budgetAmount = 600;
                Budget budget4 = new Budget();
                budget4.yearMonth = "201805";
                budget4.budgetAmount = 620;
                List<Budget> budgetList = new ArrayList<>();
                budgetList.add(budget);
                budgetList.add(budget2);
                budgetList.add(budget3);
                budgetList.add(budget4);
                return budgetList;
            }
        };
    }
}
