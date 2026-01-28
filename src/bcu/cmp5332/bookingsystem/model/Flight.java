package bcu.cmp5332.bookingsystem.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;

public class Flight {
    
    private int id;
    private String flightNumber;
    private String origin;
    private String destination;
    private LocalDate departureDate;

    private final Set<Customer> passengers;

    public Flight(int id, String flightNumber, String origin, String destination, LocalDate departureDate) {
        this.id = id;
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureDate = departureDate;
        
        passengers = new HashSet<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }
    
    public String getOrigin() {
        return origin;
    }
    
    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public List<Customer> getPassengers() {
        return new ArrayList<>(passengers);
    }
	
    public String getDetailsShort() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/YYYY");
        return "Flight #" + id + " - " + flightNumber + " - " + origin + " to " 
                + destination + " on " + departureDate.format(dtf);
    }

    public String getDetailsLong() {
        StringBuilder sb = new StringBuilder();

        // PDF format: "Flight #1 Flight No: LH2560 Origin: Birmingham Destination: Munich Departure Date: 2020-11-25"
        sb.append("Flight #").append(id);
        sb.append(" Flight No: ").append(flightNumber);
        sb.append(" Origin: ").append(origin);
        sb.append(" Destination: ").append(destination);
        sb.append(" Departure Date: ").append(departureDate);
        sb.append("\n\nPassengers:\n");

        if (passengers.isEmpty()) {
            sb.append("  No passengers booked.");
        } else {
            for (Customer c : passengers) {
                sb.append("* Id: ").append(c.getId())
                        .append(" - ").append(c.getName())
                        .append(" - ").append(c.getPhone()).append("\n");
            }
            sb.append(passengers.size()).append(" passenger(s)");
        }
        return sb.toString();
    }

    public void addPassenger(Customer passenger) throws FlightBookingSystemException {
        // Check by customer ID
        for (Customer existing : passengers) {
            if (existing.getId() == passenger.getId()) {
                throw new FlightBookingSystemException(
                        "Passenger #" + passenger.getId() + " is already booked on this flight"
                );
            }
        }

        passengers.add(passenger);
    }

    public void removePassenger(Customer passenger) throws FlightBookingSystemException {
        Customer toRemove = null;
        for (Customer existing : passengers) {
            if (existing.getId() == passenger.getId()) {
                toRemove = existing;
                break;
            }
        }

        if (toRemove == null) {
            throw new FlightBookingSystemException(
                    "Passenger #" + passenger.getId() + " is not booked on this flight"
            );
        }
        passengers.remove(toRemove);
    }
}
