//package bcu.cmp5332.bookingsystem.commands;
//
//import bcu.cmp5332.bookingsystem.main.AuthenticationService;
//import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
//
//public class LogoutCommand implements Command {
//
//    private final AuthenticationService authService;
//
//    public LogoutCommand(AuthenticationService authService) {
//        this.authService = authService;
//    }
//
//    @Override
//    public void execute(FlightBookingSystem fbs) {
//        authService.logout();
//        System.out.println("Logged out successfully.");
//    }
//}