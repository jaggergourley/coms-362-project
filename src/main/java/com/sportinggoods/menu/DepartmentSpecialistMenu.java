package com.sportinggoods.menu;

import com.sportinggoods.controller.AppointmentController;
import com.sportinggoods.model.Appointment;
import com.sportinggoods.util.InitializationManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class DepartmentSpecialistMenu extends BaseMenu {
    private AppointmentController appointmentController;
    private int storeId;

    public DepartmentSpecialistMenu(InitializationManager initManager, Scanner scanner, int storeId) {
        super(initManager, scanner);
        this.appointmentController = initManager.getAppointmentController();
        this.storeId = storeId;
    }

    @Override
    protected void registerCommands() {
        invoker.register("1", this::viewAppointments);
        invoker.register("2", this::updateAppointmentStatus);
        invoker.register("3", this::manageWaitlist);
    }

    @Override
    protected void printMenuOptions() {
        clearConsole();
        System.out.println("\n=== Department Specialist Menu ===");
        System.out.println("1. View Scheduled Appointments");
        System.out.println("2. Update Appointment Status");
        System.out.println("3. Manage Waitlist");
        System.out.println("4. Back to Main Menu");
    }

    @Override
    protected boolean isExitChoice(String choice) {
        return choice.equals("4");
    }

    @Override
    protected void handleExit() {
        System.out.println("Returning to Main Menu...");
    }

    /**
     * Displays a list of appointments for the store.
     */
    private void viewAppointments() {
        clearConsole();
        List<Appointment> appointments = appointmentController.getAllAppointments();
        appointments.stream()
                .filter(appointment -> appointment.getStoreId() == storeId)
                .forEach(appointment -> System.out.println(appointment));

        pause();
    }

    /**
     * Updates the status of an appointment.
     */
    private void updateAppointmentStatus() {
        clearConsole();
        System.out.print("Enter Appointment ID to update: ");
        String appointmentId = scanner.nextLine();

        List<Appointment> appointments = appointmentController.getAllAppointments();
        Appointment appointment = appointments.stream()
                .filter(a -> a.getAppointmentId().equals(appointmentId) && a.getStoreId() == storeId)
                .findFirst()
                .orElse(null);

        if (appointment == null) {
            System.out.println("Appointment not found.");
            pause();
            return;
        }

        System.out.println("Current Status: " + appointment.getStatus());
        System.out.print("Enter new status (Completed, Canceled, Service Delayed): ");
        String newStatus = scanner.nextLine();

        appointment.setStatus(newStatus);
        if (newStatus.equalsIgnoreCase("Service Delayed")) {
            System.out.print("Enter reason for delay: ");
            String delayReason = scanner.nextLine();
            System.out.println("Reason recorded: " + delayReason);
        }

        // Save updated appointment (in real scenario, you would call a method in AppointmentRepository)
        appointmentController.createAppointment(
                appointment.getStoreId(),
                appointment.getCustomerName(),
                appointment.getPhoneNumber(),
                appointment.getItemName(),
                appointment.getIssue(),
                appointment.getAppointmentTime()
        );

        System.out.println("Appointment status updated.");
        pause();
    }

    /**
     * Displays and manages the waitlist.
     */
    private void manageWaitlist() {
        clearConsole();
        System.out.println("Viewing and managing the waitlist is currently under development.");
        pause();
    }

    /**
     * Pauses for user input.
     */
    private void pause() {
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }
}
