package bcu.cmp5332.bookingsystem.data;

import java.io.IOException;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
/**
 * Interface for data managers that handle file I/O for the booking system.
 * Defines the contract for loading and storing system data.
 */
public interface DataManager {
    
    public static final String SEPARATOR = "::";

    /**
     * Loads data from files into the flight booking system.
     * @param fbs the flight booking system to populate with data
     * @throws IOException if there's an error reading from files
     * @throws FlightBookingSystemException if data format is invalid
     */
    public void loadData(FlightBookingSystem fbs) throws IOException, FlightBookingSystemException;

    /**
     * Saves data from the flight booking system to files.
     * @param fbs the flight booking system containing data to save
     * @throws IOException if there's an error writing to files
     */
    public void storeData(FlightBookingSystem fbs) throws IOException;
}
