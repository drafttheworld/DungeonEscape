/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author Andrew
 */
public class FreezeTime {

    private final long time;
    private final TimeUnit timeUnit;

    public FreezeTime(long time, TimeUnit timeUnit) {
        this.time = time;
        this.timeUnit = timeUnit;
    }

    public long getTime() {
        return time;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public long getFreezeTimeForTimeUnit(TimeUnit timeUnit) {
        switch (timeUnit) {
            case DAYS:
                return this.timeUnit.toDays(time);
            case HOURS:
                return this.timeUnit.toHours(time);
            case MICROSECONDS:
                return this.timeUnit.toMicros(time);
            case MILLISECONDS:
                return this.timeUnit.toMillis(time);
            case MINUTES:
                return this.timeUnit.toMinutes(time);
            case NANOSECONDS:
                return this.timeUnit.toNanos(time);
            case SECONDS:
                return this.timeUnit.toSeconds(time);
        }

        throw new IllegalArgumentException("Unsupported time unit: " + timeUnit.name());
    }

}
