import java.time.Duration;
import java.time.LocalDate;

class Period {
    LocalDate start;
    LocalDate end;

    Period(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }

    long getEffectiveDays(LocalDate date, Budget budget) {
        LocalDate effectiveStart = start;
        LocalDate effectiveEnd = end;
        if (date.compareTo(LocalDate.of(start.getYear(), start.getMonth(), 1)) == 0) {
            effectiveStart = start;
            effectiveEnd = budget.lastDate();
        } else if (date.compareTo(LocalDate.of(end.getYear(), end.getMonth(), 1)) == 0) {
            effectiveStart = budget.firstDate();
            effectiveEnd = end;
        } else {
            effectiveStart = budget.firstDate();
            effectiveEnd = budget.lastDate();
        }

        return Duration.between(effectiveStart.atStartOfDay(), effectiveEnd.atStartOfDay()).toDays() + 1;
    }
}