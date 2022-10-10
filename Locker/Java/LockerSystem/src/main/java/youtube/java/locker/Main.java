package youtube.java.locker;

import youtube.java.locker.models.Locker;
import youtube.java.locker.models.Tenant;
import youtube.java.locker.models.Wall;

import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Wall wall = new Wall("Wall one", 1000, 5);

        System.out.println(wall);

        Tenant tenant = new Tenant("Rik Peeters");

        String password = wall.addLockerReservation(
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                tenant,
                "B1"
        );
    }
}