package youtube.java.locker.models;

import jdk.jshell.execution.Util;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import youtube.java.locker.Utils;

import java.util.Comparator;

@Getter
public class Locker {
    private String id;

    public Locker(int xAxis, int yAxis) {
        this.id = buildId(xAxis, yAxis);
    }

    private String buildId(int xAxis, int yAxis){
        String letterCode = Utils.toAlphabetic(xAxis);
        int numberCode = yAxis + 1;

        return letterCode + numberCode;
    }

    static class LockerComparator implements Comparator<Locker> {
        @Override
        public int compare(Locker o1, Locker o2) {
            return o1.getId().compareTo(o2.getId());
        }
    }
}
