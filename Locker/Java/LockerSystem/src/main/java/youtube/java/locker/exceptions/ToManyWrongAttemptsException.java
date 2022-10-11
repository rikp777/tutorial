package youtube.java.locker.exceptions;

public class ToManyWrongAttemptsException extends Exception {
    public ToManyWrongAttemptsException(String message) {
        super(message);
    }
}
