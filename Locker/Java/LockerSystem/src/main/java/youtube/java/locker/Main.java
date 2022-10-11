package youtube.java.locker;

import youtube.java.locker.models.History;
import youtube.java.locker.models.Locker;
import youtube.java.locker.models.Tenant;
import youtube.java.locker.models.Wall;

import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Wall wall = new Wall("Proxy", 5, 5);
        wall.print();

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

        wall.print();

        wall.openLocker("B1", "wrong password");
        wall.openLocker("B1", "wrong password");
//        wall.openLocker("B1", "wrong password");
//        wall.openLocker("B1", "wrong password");
//        wall.openLocker("B1", "wrong password");

        String secret = wall.openLocker("B1", password);

        wall.print();
    }
}