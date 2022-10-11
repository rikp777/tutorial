package youtube.java.locker;

import youtube.java.locker.models.History;
import youtube.java.locker.models.Locker;
import youtube.java.locker.models.Tenant;
import youtube.java.locker.models.Wall;

import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Wall wall = new Wall("Wall one", 5, 5);

        System.out.println(wall);

        Tenant tenant = new Tenant("Rik Peeters");

        String password = "test";
        History history = wall.addLockerReservation(
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                tenant,
                "B1",
                "I like to store my stuff here, and its very secret",
                password
        );
        System.out.println(wall);

        wall.openLocker("B1", "wrong password");
        wall.openLocker("B1", "wrong password");
        wall.openLocker("B1", "wrong password");
        wall.openLocker("B1", "wrong password");
        wall.openLocker("B1", "wrong password");

        wall.openLocker("B1", password);

        System.out.println(wall);

    }
}