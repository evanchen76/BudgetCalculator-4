import java.time.LocalDate;
import java.util.List;


class BudgetCaculator {
    private IBudgetRepo budgetRepo;

    BudgetCaculator(IBudgetRepo budgetRepo) {

        this.budgetRepo = budgetRepo;
    }

    double totalAmount(LocalDate start, LocalDate end) {

        List<Budget> budgetList = budgetRepo.getAll();

        Period period = new Period(start, end);
        period.start = start;
        period.end = end;

        double totalAmount = 0.0;

        for (Budget budget : budgetList) {
            totalAmount += budget.effectiveTotalAmount(period);
        }

        return totalAmount;

    }
}