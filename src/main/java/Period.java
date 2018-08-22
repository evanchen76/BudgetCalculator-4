import java.time.Duration;
import java.time.LocalDate;

class Period {
    LocalDate start;
    LocalDate end;

    Period(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }

    long overlappingDays(Period otherPeriod) {
        LocalDate effectiveStart = start;
        if (otherPeriod.start.compareTo(start) > 0) {
            effectiveStart = otherPeriod.start;
        }
        LocalDate effectiveEnd = end;
        if (otherPeriod.end.compareTo(end) < 0) {
            effectiveEnd = otherPeriod.end;
        }

        return Duration.between(effectiveStart.atStartOfDay(), effectiveEnd.atStartOfDay()).toDays() + 1;
    }
}