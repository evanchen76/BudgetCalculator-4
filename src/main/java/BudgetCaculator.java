import java.time.LocalDate;
import java.util.List;


class BudgetCaculator {
    private IBudgetRepo budgetRepo;

    BudgetCaculator(IBudgetRepo budgetRepo) {

        this.budgetRepo = budgetRepo;
    }

    double totalAmount(LocalDate start, LocalDate end) {

        Period period = new Period(start, end);

        return budgetRepo.getAll().stream().mapToDouble(b -> b.effectiveTotalAmount(period)).sum();

    }
}