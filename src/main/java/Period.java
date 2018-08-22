import java.time.Duration;
import java.time.LocalDate;

class Period {
    LocalDate start;
    LocalDate end;

    Period(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }

    long getEffectiveDays( Budget budget) {
        LocalDate effectiveStart = start;
        if (budget.firstDate().compareTo(this.start) > 0) {
            effectiveStart = budget.firstDate();
        }
        LocalDate effectiveEnd = end;
        if (budget.lastDate().compareTo(this.end) < 0) {
            effectiveEnd = budget.lastDate();
        }

        return Duration.between(effectiveStart.atStartOfDay(), effectiveEnd.atStartOfDay()).toDays() + 1;
    }
}