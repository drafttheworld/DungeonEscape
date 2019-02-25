/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.mine;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author Andrew
 */
public class FreezeTime {

    private final int time;
    private final TimeUnit timeUnit;

    public FreezeTime(int time, TimeUnit timeUnit) {
        this.time = time;
        this.timeUnit = timeUnit;
    }

    public int getTime() {
        return time;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public long getFreezeTimeForTimeUnit(TimeUnit timeUnit) {
        switch (timeUnit) {
            case DAYS:
                return timeUnit.toDays(time);
            case HOURS:
                return timeUnit.toHours(time);
            case MICROSECONDS:
                return timeUnit.toMicros(time);
            case MILLISECONDS:
                return timeUnit.toMillis(time);
            case MINUTES:
                return timeUnit.toMinutes(time);
            case NANOSECONDS:
                return timeUnit.toNanos(time);
            case SECONDS:
                return timeUnit.toSeconds(time);
        }

        throw new IllegalArgumentException("Unsupported time unit: " + timeUnit.name());
    }

}
