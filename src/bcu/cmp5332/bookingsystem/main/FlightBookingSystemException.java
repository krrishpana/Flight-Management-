package bcu.cmp5332.bookingsystem.main;

/**
 * FlightBookingSystemException extends {@link Exception} class and is a custom exception
 * that is used to notify the user about errors or invalid commands.
 * 
 */
public class FlightBookingSystemException extends Exception {
	private static final long serialVersionUID = 1L;

    /**
     * Creates a new exception with a specific error message.
     * @param message the error message to display
     */
    public FlightBookingSystemException(String message) {
        super(message);
    }
}
