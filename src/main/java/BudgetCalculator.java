import java.time.LocalDate;

class BudgetCalculator {
    private IBudgetRepo budgetRepo;

    BudgetCalculator(IBudgetRepo budgetRepo) {

        this.budgetRepo = budgetRepo;
    }

    double totalAmount(LocalDate start, LocalDate end) {

        Period period = new Period(start, end);

        return budgetRepo.getAll().stream().mapToDouble(b -> b.effectiveTotalAmount(period)).sum();

    }
}