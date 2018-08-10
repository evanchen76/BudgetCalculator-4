import org.junit.Assert;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BudgetCaculatorTest
{

    private BudgetCaculator budgetCaculator;

    public BudgetCaculatorTest() throws ParseException {
    }

    @Test
    public void same_day() {

        IBudgetRepo budgetRepo = new IBudgetRepo() {
            @Override
            public List<Budget> getAll() {
                Budget budget = new Budget();
                budget.yearMonth = "201801";
                budget.budgetAmount = 310;
                return null;
            }
        };

        budgetCaculator = new BudgetCaculator(budgetRepo);
        LocalDate start_day =  LocalDate.of(2018, 1, 1);
        LocalDate end_day =  LocalDate.of(2018, 1, 1);
        totalAmountShouldBe(start_day, end_day, 10.00);
    }

    @Test
    public void range_two_days() {

        IBudgetRepo budgetRepo = givenBudgetRepo();
        budgetCaculator = new BudgetCaculator(budgetRepo);
        LocalDate start_day =  LocalDate.of(2018, 1, 1);
        LocalDate end_day =  LocalDate.of(2018, 1, 2);
        totalAmountShouldBe(start_day, end_day, 20.00);
    }

    @Test
    public void full_month() {

        IBudgetRepo budgetRepo = givenBudgetRepo();

        budgetCaculator = new BudgetCaculator(budgetRepo);
        LocalDate start_day =  LocalDate.of(2018, 1, 1);
        LocalDate end_day =  LocalDate.of(2018, 1, 31);
        totalAmountShouldBe(start_day, end_day, 310.00);
    }

    @Test
    public void cross_month() {

        IBudgetRepo budgetRepo = givenBudgetRepo();

        budgetCaculator = new BudgetCaculator(budgetRepo);
        LocalDate start_day =  LocalDate.of(2018, 1, 31);
        LocalDate end_day =  LocalDate.of(2018, 2, 1);
        totalAmountShouldBe(start_day, end_day, 10.00);
    }

    @Test
    public void two_different_months() {

        IBudgetRepo budgetRepo = givenBudgetRepo();

        budgetCaculator = new BudgetCaculator(budgetRepo);
        LocalDate start_day =  LocalDate.of(2018, 3, 31);
        LocalDate end_day =  LocalDate.of(2018, 4, 1);
        totalAmountShouldBe(start_day, end_day, 220.00);
    }


    @Test
    public void donotoverlap() {

        IBudgetRepo budgetRepo = givenBudgetRepo();

        budgetCaculator = new BudgetCaculator(budgetRepo);
        LocalDate start_day =  LocalDate.of(2018, 2, 2);
        LocalDate end_day =  LocalDate.of(2018, 2, 5);
        totalAmountShouldBe(start_day, end_day, 0);
    }

    private void totalAmountShouldBe(LocalDate start_day, LocalDate end_day, double v) {
        double budget = budgetCaculator.totalAmount(start_day, end_day);
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
                List<Budget> budgetList = new ArrayList<>();
                budgetList.add(budget);
                budgetList.add(budget2);
                budgetList.add(budget3);
                return budgetList;
            }
        };
    }
}
