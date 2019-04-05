package Supervision;

public class FlakinessException extends RuntimeException {
    static final long serialVersionUID = 1;

    public FlakinessException() {
        super("Flakiness");
    }
}