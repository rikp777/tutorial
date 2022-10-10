package youtube.java.locker.models;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;


public class History {
    private LocalDateTime lockedAt;
    private LocalDateTime unlockedAt;

    private Tenant tenant;
    private Locker locker;
    private String password;

    public History(LocalDateTime lockedAt, LocalDateTime unlockedAt, Tenant tenant, Locker locker) {
        this.lockedAt = lockedAt;
        this.unlockedAt = unlockedAt;
        this.tenant = tenant;
        this.locker = locker;
        this.password = generatePassword();
    }

    private String generatePassword(){
        return "1234";
    }
}
