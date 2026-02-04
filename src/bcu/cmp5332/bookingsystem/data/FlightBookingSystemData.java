package bcu.cmp5332.bookingsystem.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Coordinates all data loading and saving operations for the system.
 * Manages a collection of data managers and orchestrates their execution.
 */
public class FlightBookingSystemData {

    /**
     * List of data managers that handle different types of data.
     * Initialized with managers for flights, customers, and bookings.
     */
    private static final List<DataManager> dataManagers = new ArrayList<>();

    // Static initializer - runs when class is loaded
    static {
        dataManagers.add(new FlightDataManager());
        dataManagers.add(new CustomerDataManager());
        dataManagers.add(new BookingDataManager());
    }

    /**
     * Loads all system data from files using the registered data managers.
     * @return a fully populated FlightBookingSystem object
     * @throws FlightBookingSystemException if any data format is invalid
     * @throws IOException if any file cannot be read
     */
    public static FlightBookingSystem load() throws FlightBookingSystemException, IOException {

        FlightBookingSystem fbs = new FlightBookingSystem();
        for (DataManager dm : dataManagers) {
            dm.loadData(fbs);
        }
        return fbs;
    }

    /**
     * Saves all system data to files using the registered data managers.
     * @param fbs the flight booking system containing data to save
     * @throws IOException if any file cannot be written
     */
    public static void store(FlightBookingSystem fbs) throws IOException {

        for (DataManager dm : dataManagers) {
            dm.storeData(fbs);
        }
    }
}