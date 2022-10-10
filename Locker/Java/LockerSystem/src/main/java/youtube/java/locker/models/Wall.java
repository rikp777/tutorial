package youtube.java.locker.models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import youtube.java.locker.Utils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Log
public class Wall {
    private String name;
    private String masterKey;
    private Locker[][] lockers;
    private List<History> historyData;

    public Wall(String name, int maxWidth, int maxHeight){
        this.name = name;
        this.lockers = buildGrid(maxWidth, maxHeight);
    }

    public void setMasterKey(String masterKey){
        this.masterKey = masterKey;
    }

    public int getWallHeight(){
        return lockers.length;
    }
    public int getWallWidth(){
        return lockers[0].length;
    }

    /**
     * Build a grid of lockers
     * @param maxWidth
     * @param maxHeight
     * @return List<Locker>
     */
    public Locker[][] buildGrid(int maxWidth, int maxHeight){
        Locker[][] lockers = new Locker[maxWidth][maxHeight];
        for (int i = 0; i < maxWidth; i++) {
            for (int j = 0; j < maxHeight; j++) {
                lockers[i][j] = new Locker(i, j);
            }
        }
        return lockers;
    }

    public String addLockerReservation(
            LocalDateTime lockedAt,
            LocalDateTime expectedLockedUntil,
            Tenant tenant,
            String lockerId
    )
    {
        Locker locker = getLocker(lockerId);
        History lockerOccupied = new History(
                lockedAt,
                expectedLockedUntil,
                tenant,
                locker
        );
        return null;
    }

    private Locker getLocker(String lockerId){
        //convert lockerId to x and y
        var result = lockerId.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");

        //Validation
        if(!Utils.isString(result[0]))
            throw new IllegalArgumentException("LockerId is not valid");
        if(!Utils.isNumeric(result[1]))
            throw new IllegalArgumentException("LockerId is not valid");

        var xAxis = Utils.toNumeric(result[0]) - 1;
        var yAxis = Integer.parseInt(result[1]) - 1;

        return this.lockers[xAxis][yAxis];
    }

    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();
        for(int i = 0; i < lockers.length; i++){
            for(int j = 0; j < lockers[i].length; j++){
                result.append(lockers[i][j].getId() + " ");
                if(j == lockers[i].length - 1) {
                    result.append("\n");
                }
            }
        }
        return result.toString();
    }
}
