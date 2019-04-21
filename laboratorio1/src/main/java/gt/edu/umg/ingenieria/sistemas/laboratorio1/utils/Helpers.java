package gt.edu.umg.ingenieria.sistemas.laboratorio1.utils;

import java.util.Calendar;
import java.util.Locale;
import static java.util.Calendar.*;
import java.util.Date;

public class Helpers {
    public static boolean isOverOrEqualsNYears(Date birtDate, int N) {
        Calendar currentCalendar = getCalendar(new Date());
        Calendar inputCalendar = getCalendar(birtDate);
        int diff = currentCalendar.get(YEAR) - inputCalendar.get(YEAR);
        if
        (
            inputCalendar.get(MONTH) > currentCalendar.get(MONTH) ||
            (inputCalendar.get(MONTH) == currentCalendar.get(MONTH) && inputCalendar.get(DATE) > currentCalendar.get(DATE))
        )
        {
            diff--;
        }
        return diff >= N;
    }

    private static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return cal;
    }
}
