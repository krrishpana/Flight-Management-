package bcu.cmp5332.bookingsystem.model;

/**
 * Defines user roles in the system.
 * ADMIN users have full system access.
 * CUSTOMER users can book and manage their flights.
 */
public enum UserRole {
    /**
     * System administrator with full access to all features.
     */
    ADMIN,

    /**
     * Regular customer who can book and manage flights.
     */
    CUSTOMER
}