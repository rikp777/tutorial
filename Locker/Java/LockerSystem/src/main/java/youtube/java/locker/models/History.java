package youtube.java.locker.models;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.time.LocalDateTime;

@Log
@Getter
public class History {
    private LocalDateTime lockedAt;
    private LocalDateTime unlockedAt;

    private Tenant tenant;
    private Locker locker;


    public History(LocalDateTime lockedAt, LocalDateTime unlockedAt, Tenant tenant, Locker locker, String secretData) {
        this.lockedAt = lockedAt;
        this.unlockedAt = unlockedAt;
        this.tenant = tenant;
        this.locker = locker;
        this.locker.lock(this.generatePassword(), secretData);
    }
    public History(LocalDateTime lockedAt, LocalDateTime unlockedAt, Tenant tenant, Locker locker, String secretData, String password) {
        this.lockedAt = lockedAt;
        this.unlockedAt = unlockedAt;
        this.tenant = tenant;
        this.locker = locker;
        this.locker.lock(password, secretData);
    }

    public void setUnlockedAt(LocalDateTime unlockedAt){
        this.unlockedAt = unlockedAt;
    }

    private String generatePassword(){
        int combinationLength = 4;
        String combination = "";
        for(int i = 0; i < combinationLength; i++){
            int answer = (int) (Math.random() * 9) + 1;
            combination += answer;
        }
        log.info("Password to lock " + this.locker.getId() + " : " + combination);
        return combination;
    }
}
