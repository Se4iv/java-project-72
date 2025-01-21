package hexlet.code.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Timestamp;

public final class Time {
    public static Timestamp getTime() {
        var date = new Date();
        var time = new Timestamp(date.getTime());
        return time;
    }

    public static String getSimpleTime(Timestamp time) {
        var simpleTime = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        var simplifiedTime = simpleTime.format(time);
        return simplifiedTime;
    }
}