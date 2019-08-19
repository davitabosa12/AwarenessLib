package smd.ufc.br.easycontext.fence.parameter;

import java.util.TimeZone;

public class TimeParameter implements FenceParameter {
    private int timeInstant;
    private long startOffsetMillis;
    private long stopOffsetMillis;

    private TimeZone timeZone;
    private long startTimeOfDayMillis;
    private long stopTimeOfDayMillis;
    private long startTimeMillis;
    private long stopTimeMillis;

    private int dayOfWeek;
    private int timeInterval;

    public TimeParameter() {
    }

    public TimeParameter setTimeInstant(int timeInstant) {
        this.timeInstant = timeInstant;
        return this;
    }

    public TimeParameter setStartOffsetMillis(long startOffsetMillis) {
        this.startOffsetMillis = startOffsetMillis;
        return this;
    }

    public TimeParameter setStopOffsetMillis(long stopOffsetMillis) {
        this.stopOffsetMillis = stopOffsetMillis;
        return this;
    }

    public TimeParameter setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
        return this;
    }

    public TimeParameter setStartTimeOfDayMillis(long startTimeOfDayMillis) {
        this.startTimeOfDayMillis = startTimeOfDayMillis;
        return this;
    }

    public TimeParameter setStopTimeOfDayMillis(long stopTimeOfDayMillis) {
        this.stopTimeOfDayMillis = stopTimeOfDayMillis;
        return this;
    }

    public TimeParameter setStartTimeMillis(long startTimeMillis) {
        this.startTimeMillis = startTimeMillis;
        return this;
    }

    public TimeParameter setStopTimeMillis(long stopTimeMillis) {
        this.stopTimeMillis = stopTimeMillis;
        return this;
    }

    public TimeParameter setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
        return this;
    }

    public TimeParameter setTimeInterval(int timeInterval) {
        this.timeInterval = timeInterval;
        return this;
    }
}
