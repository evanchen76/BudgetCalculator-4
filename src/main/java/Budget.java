import java.time.LocalDate;

class Budget {
    String yearMonth;
    double budgetAmount;

    private Double getDailyAmount() {

        LocalDate budgetDate = firstDate();
        int daysOfMonth = budgetDate.lengthOfMonth();

        return budgetAmount / daysOfMonth;
    }

    private LocalDate firstDate() {
        return LocalDate.of(Integer.parseInt(yearMonth.substring(0, 4)), Integer.parseInt(yearMonth.substring(4)), 1);
    }

    private LocalDate lastDate() {
        return LocalDate.of(Integer.parseInt(yearMonth.substring(0, 4)), Integer.parseInt(yearMonth.substring(4)), firstDate().lengthOfMonth());
    }

    private Period createPeriod() {
        return new Period(firstDate(), lastDate());
    }

    double effectiveTotalAmount(Period period) {
        return period.overlappingDays(createPeriod()) * getDailyAmount();
    }
}
