package hexlet.code.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Timestamp;

public final class Time {
    public static Timestamp getTime() {
        var date = new Date();
        return new Timestamp(date.getTime());
    }

    public static String getSimpleTime(Timestamp time) {
        var simpleTime = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        return simpleTime.format(time);
    }
}
