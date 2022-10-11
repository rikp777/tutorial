package youtube.java.locker.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.java.Log;

@Getter
@Log
public class Tenant {
    private String name;

    public Tenant(String name){
        this.name = name;
        this.lastNotification = "";
        notify("Welcome to the system, you can now lock and unlock lockers");
    }

    private String lastNotification;


    public void notify(String message){
        if(lastNotification.equals(message))
           return;

        this.lastNotification = message;
        System.out.println("Notification for " + this.name + " : " + message);
    }
}
