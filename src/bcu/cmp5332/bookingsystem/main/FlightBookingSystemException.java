package bcu.cmp5332.bookingsystem.main;

/**
 * FlightBookingSystemException extends {@link Exception} class and is a custom exception
 * that is used to notify the user about errors or invalid commands.
 * 
 */
public class FlightBookingSystemException extends Exception {
	private static final long serialVersionUID = 1L;


    public FlightBookingSystemException(String message) {
        super(message);
    }
}
