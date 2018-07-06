package org.test.getData;


import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;

public class Test {
    public static void main(String[] args) {
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime newTime = time.minusMinutes(1440);
        for (int i = 0; i < 1440; i += 30) {
            System.out.println(newTime.plusMinutes(i).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        }
//        LocalDateTime end = LocalDateTime.now();
//        LocalDateTime begin = end.minus(Period.ofDays(1));
//        LocalDateTime begin2 = end.minusDays(1);
//        LocalDateTime begin3 = end.minusMinutes(1440);
//        System.out.println(begin.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
//        System.out.println(begin2.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
//        System.out.println(begin3.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
    }
}
