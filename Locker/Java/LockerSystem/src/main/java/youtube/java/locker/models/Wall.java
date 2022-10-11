package youtube.java.locker.models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import youtube.java.locker.Utils;
import youtube.java.locker.exceptions.ToManyWrongAttemptsException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
        this.historyData = new ArrayList<>();
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

    public int getAvailableAmount(){
        //get all available lockers
        int availableAmount = 0;
        for(int i = 0; i < lockers.length; i++){
            for(int j = 0; j < lockers[i].length; j++){
                if(!lockers[i][j].isLocked())
                    availableAmount++;
            }
        }
        return availableAmount;
    }

    /**
     * Build a grid of lockers
     * @param maxWidth the width of the grid
     * @param maxHeight the height of the grid
     * @return List<Locker> the list of lockers
     */
    private Locker[][] buildGrid(int maxWidth, int maxHeight){
        Locker[][] lockers = new Locker[maxWidth][maxHeight];
        for (int i = 0; i < maxWidth; i++) {
            for (int j = 0; j < maxHeight; j++) {
                lockers[i][j] = new Locker(i, j);
            }
        }
        return lockers;
    }

    /**
     * Add a locker reservation to the wall
     * @param lockedAt The time the locker was locked
     * <p>
     *  The time the locker was locked, this is the time the tenant can start using the locker
     *  and the time the locker is reserved for the tenant until the time the locker is unlocked.
     *  <ul>
     *      <li>if null, the current time will be used</li>
     *      <li>if not null, the time must be in the future</li>
     *      <li>if not null, the time must be less than 24 hours in the future</li>
     *  </ul>
     * </p>
     * @param expectedLockedUntil The time the locker is expected to be unlocked
     * <p>
     *  The time the locker is expected to be unlocked, this is the time the tenant can stop
     *  using the locker. The locker will not be unlocked at this time.
     *  <ul>
     *   <li>if null, the current time + 24 hours will be used</li>
     *   <li>if not null, the time must be in the future</li>
     *   <li>if not null, the time must be less than 24 hours in the future</li>
     *   <li>if not null, the time must be greater than lockedAt</li>
     *  </ul>
     * </p>
     * @param tenant The tenant that is reserving the locker
     * <p>
     *  The tenant that is reserving the locker and will be using the locker.
     *  <ul>
     *   <li>if null, the locker will not be reserved</li>
     *  </ul
     * </p>
     * @param lockerId the id of the locker to reserve
     *                 if null, the locker will not be reserved
     *                 if not null, the locker must be available
     *                 if not null, the locker must be on the wall

     * @return Locker the locker
     */
    public History addLockerReservation(
            LocalDateTime lockedAt,
            LocalDateTime expectedLockedUntil,
            Tenant tenant,
            String lockerId,
            String secretData
    )
    {
        Locker locker = getLocker(lockerId);
        History lockerOccupied = new History(
                lockedAt,
                expectedLockedUntil,
                tenant,
                locker,
                secretData
        );
        historyData.add(lockerOccupied);
        return lockerOccupied;
    }
    public History addLockerReservation(
            LocalDateTime lockedAt,
            LocalDateTime expectedLockedUntil,
            Tenant tenant,
            String lockerId,
            String secretData,
            String password
    )
    {
        System.out.println("Tenant: " + tenant.getName() + " is trying to reserve locker: [" + lockerId + "] with password: [" + password + "] ");
        Locker locker = getLocker(lockerId);
        History lockerOccupied = new History(
                lockedAt,
                expectedLockedUntil,
                tenant,
                locker,
                secretData,
                password
        );
        historyData.add(lockerOccupied);
        return lockerOccupied;
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

    public String openLocker(String lockerId, String password){
        System.out.println("Trying to open locker: [" + lockerId + "] with password: [" + password + "] ");

        var locker = getLocker(lockerId);
        Optional<History> tenantLocker = historyData.stream()
                .filter(history -> history.getLocker().equals(locker))
                .findFirst();

        if(tenantLocker.isPresent()){
            History history = tenantLocker.get();
            try{
                String secret = history.getLocker().unlock(password);

                if(secret != null){
                    System.out.println("Thank you for using our locker wall " + history.getTenant().getName() + ", until next time!");
                    System.out.println("Your secret data is: [" + secret + "]");
                    return secret;
                }
            }catch (ToManyWrongAttemptsException exception){
                tenantLocker.get().getTenant().notify("Too many wrong attempts");
                return null;
            }
        }

        return null;
    }


    /*
    * Build print string for wall
    * @return print string
     */
    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();
        for(int i = 0; i < lockers.length; i++){
            for(int j = 0; j < lockers[i].length; j++){
                result.append(lockers[i][j].getId() + " " + lockers[i][j].isLocked() + " \t");
                if(j == lockers[i].length - 1) {
                    result.append("\n");
                }
            }
        }
        return result.toString();
    }


    public void print(){
        System.out.println();
        System.out.println("Locker Wall: " + this.name);
        System.out.println("Available lockers: " + this.getAvailableAmount());
        for (int i = 0; i < this.lockers.length; i++){
            System.out.print("");
            for (int j = 0; j < this.lockers[0].length; j++){
                System.out.print("");
            }
            System.out.println();

            for (int j = 0; j < this.lockers[0].length; j++){
                if(this.lockers[i][j].isLocked()){
                    System.out.print("📕");
                } else {
                    System.out.print("\uD83D\uDCD7");
                }

                System.out.print(this.lockers[i][j].getId() + " \t\t");

            }
            for (int j = 0; j < this.lockers[0].length; j++){
                if(this.lockers[i][j].isLocked()){
                     int finalI = i;
                     int finalJ = j;
                     History lockMetaData = historyData.stream()
                            .filter(history -> history.getLocker().equals(this.lockers[finalI][finalJ]))
                            .findFirst()
                            .get();
                    System.out.print(lockers[i][j].getId() + " Locked at: " + lockMetaData.getLockedAt().format(DateTimeFormatter.ofPattern("MM/dd/yyyy 'at' hh:mma")) + " \t");
                }
            }

            System.out.println();
        }
        System.out.println();
    }
}
