package youtube.java.locker.models;

import jdk.jshell.execution.Util;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import youtube.java.locker.Utils;
import youtube.java.locker.exceptions.ToManyWrongAttemptsException;

import java.util.Comparator;

@Log
public class Locker {
    @Getter
    private String id;

    private String mySecretData;

    @Getter
    private boolean isLocked;
    private String password;

    private int wrongPasswordCount;

    public Locker(int xAxis, int yAxis) {
        this.id = buildId(xAxis, yAxis);
        this.isLocked = false;
    }

    private String buildId(int xAxis, int yAxis){
        String letterCode = Utils.toAlphabetic(xAxis);
        int numberCode = yAxis + 1;

        return letterCode + numberCode;
    }

    public void lock(String password, String mySecretData){
        if(!this.isLocked){
            this.password = password;
            this.mySecretData = mySecretData;
            this.isLocked = true;
            log.info("Locker " + this.id + " is locked");
        }else{
            log.info("Locker is already locked");
        }
    }

    public String unlock(String password) throws ToManyWrongAttemptsException {
        int maxAttempts = 3;
        if(this.wrongPasswordCount >= maxAttempts){
            throw new ToManyWrongAttemptsException("Too many wrong password");
        }

        if(this.password.equals(password)){
            log.info("Locker " + this.id + " is unlocked");
            this.isLocked = false;
            return this.mySecretData;
        }

        this.wrongPasswordCount++;
        log.warning("Locker " + this.id + " wrong password [" + (maxAttempts - this.wrongPasswordCount) + " attempts left]");

        return null;
    }

    static class LockerComparator implements Comparator<Locker> {
        @Override
        public int compare(Locker o1, Locker o2) {
            return o1.getId().compareTo(o2.getId());
        }
    }
}
