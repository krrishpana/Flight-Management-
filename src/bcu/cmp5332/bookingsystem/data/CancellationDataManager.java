package bcu.cmp5332.bookingsystem.data;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.*;

public class CancellationDataManager {

    private static final String RESOURCE = "./resources/data/cancellations.txt";
    private static final int UNDO_HOURS = 24;

    public void logCancellation(Booking booking, LocalDateTime cancellationTime) throws IOException {
        File file = new File(RESOURCE);
        file.getParentFile().mkdirs();

        try (PrintWriter pw = new PrintWriter(new FileWriter(file, true))) {
            LocalDateTime undoDeadline = cancellationTime.plusHours(UNDO_HOURS);

            pw.println(
                    booking.getCustomer().getId() + "::" +
                            booking.getFlight().getId() + "::" +
                            booking.getBookingDate() + "::" +
                            booking.getPaidPrice() + "::" +
                            booking.getCancellationFee() + "::" +
                            cancellationTime + "::" +
                            undoDeadline + "::" +
                            booking.hasInfant() + "::" +
                            booking.isVegetarian()
            );
        }
    }

    public CancelledBooking findCancellation(int customerId, int flightId) throws IOException, FlightBookingSystemException {
        File file = new File(RESOURCE);
        if (!file.exists()) {
            return null;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 0;

            while ((line = br.readLine()) != null) {
                lineNumber++;
                if (line.isBlank()) continue;

                String[] parts = line.split("::");
                if (parts.length != 7) {
                    continue; // Skip malformed lines
                }

                int storedCustomerId = Integer.parseInt(parts[0]);
                int storedFlightId = Integer.parseInt(parts[1]);

                if (storedCustomerId == customerId && storedFlightId == flightId) {
                    LocalDate bookingDate = LocalDate.parse(parts[2]);
                    double paidPrice = Double.parseDouble(parts[3]);
                    double cancellationFee = Double.parseDouble(parts[4]);
                    LocalDateTime cancellationTime = LocalDateTime.parse(parts[5]);
                    LocalDateTime undoDeadline = LocalDateTime.parse(parts[6]);
                    boolean hadInfant = Boolean.parseBoolean(parts[7]);
                    boolean wasVegetarian = Boolean.parseBoolean(parts[8]);

                    return new CancelledBooking(
                            customerId, flightId, bookingDate, paidPrice,
                            cancellationFee, cancellationTime, undoDeadline,hadInfant, wasVegetarian
                    );
                }
            }
        }
        return null;
    }

    public boolean removeCancellation(int customerId, int flightId) throws IOException {
        File inputFile = new File(RESOURCE);
        File tempFile = new File(RESOURCE + ".tmp");

        if (!inputFile.exists()) {
            return false;
        }

        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
             PrintWriter pw = new PrintWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) {
                    pw.println(line);
                    continue;
                }

                String[] parts = line.split("::");
                if (parts.length >= 2) {
                    int storedCustomerId = Integer.parseInt(parts[0]);
                    int storedFlightId = Integer.parseInt(parts[1]);

                    if (storedCustomerId == customerId && storedFlightId == flightId) {
                        found = true;
                        continue;
                    }
                }
                pw.println(line);
            }
        }

        if (found) {
            if (!inputFile.delete()) {
                throw new IOException("Could not delete original file");
            }
            if (!tempFile.renameTo(inputFile)) {
                throw new IOException("Could not rename temp file");
            }
        } else {
            // Delete temp file if no changes
            tempFile.delete();
        }

        return found;
    }
}