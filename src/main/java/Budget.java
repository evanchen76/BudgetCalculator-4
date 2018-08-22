import java.time.LocalDate;

public class Budget {
    String yearMonth;
    double budgetAmount;

    Double getDailyAmount() {

        LocalDate budgetDate = firstDate();
        int daysOfMonth = budgetDate.lengthOfMonth();

        return budgetAmount / daysOfMonth;
    }

    LocalDate firstDate() {
        return LocalDate.of(Integer.parseInt(yearMonth.substring(0, 4)), Integer.parseInt(yearMonth.substring(4)), 1);
    }

    LocalDate lastDate() {
        return LocalDate.of(Integer.parseInt(yearMonth.substring(0, 4)), Integer.parseInt(yearMonth.substring(4)), firstDate().lengthOfMonth());
    }

    Period createPeriod() {
        return new Period(firstDate(), lastDate());
    }

    double effectiveTotalAmount(Period period) {
        return period.overlappingDays(createPeriod()) * getDailyAmount();
    }
}
