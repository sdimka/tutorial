package org.test.getData;

import java.time.LocalDateTime;

public class SensorsSet {
    private LocalDateTime time;
    private Double temp;

    public SensorsSet(LocalDateTime time, Double temp) {
        this.time = time;
        this.temp = temp;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public Double getTemp() {
        return temp;
    }
}
