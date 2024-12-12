package com.sportinggoods.menu;

import com.sportinggoods.controller.AppointmentController;
import com.sportinggoods.model.Appointment;
import com.sportinggoods.util.InitializationManager;

import java.util.ArrayList;
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
                .forEach(appointment -> System.out.printf(
                        "ID: %s, Customer: %s, Date: %s, Time: %s, Status: %s%n",
                        appointment.getAppointmentId(),
                        appointment.getCustomerName(),
                        appointment.getAppointmentDate(),
                        appointment.getAppointmentTime(),
                        appointment.getStatus()
                ));

        pause();
    }

    /**
     * Updates the status of an appointment.
     */
    private void updateAppointmentStatus() {
        clearConsole();

        // Display appointments for the store
        System.out.println("Appointments for Store ID " + storeId + ":");
        List<Appointment> appointments = appointmentController.getAllAppointments();
        List<Appointment> storeAppointments = appointments.stream()
                .filter(appointment -> appointment.getStoreId() == storeId)
                .toList();

        if (storeAppointments.isEmpty()) {
            System.out.println("No appointments scheduled for this store.");
            pause();
            return;
        }

        storeAppointments.forEach(appointment -> System.out.printf(
                "ID: %s | Customer: %s | Date: %s | Time: %s | Status: %s%n",
                appointment.getAppointmentId(),
                appointment.getCustomerName(),
                appointment.getAppointmentDate(),
                appointment.getAppointmentTime(),
                appointment.getStatus()
        ));

        System.out.print("\nEnter Appointment ID to update: ");
        String appointmentId = scanner.nextLine();

        Appointment appointment = storeAppointments.stream()
                .filter(a -> a.getAppointmentId().equals(appointmentId))
                .findFirst()
                .orElse(null);

        if (appointment == null) {
            System.out.println("Appointment not found.");
            pause();
            return;
        }

        System.out.println("Current Status: " + appointment.getStatus());
        System.out.print("Enter new status (Completed, Canceled, Service Delayed): ");
        String newStatus = scanner.nextLine().trim();

        if (!newStatus.equalsIgnoreCase("Completed") &&
                !newStatus.equalsIgnoreCase("Canceled") &&
                !newStatus.equalsIgnoreCase("Service Delayed")) {
            System.out.println("Invalid status. Please enter a valid status.");
            pause();
            return;
        }

        appointment.setStatus(newStatus);

        if (newStatus.equalsIgnoreCase("Service Delayed")) {
            System.out.print("Enter reason for delay: ");
            String delayReason = scanner.nextLine();
            System.out.println("Reason recorded: " + delayReason);
        }

        boolean updated = appointmentController.updateAppointmentStatus(appointment.getAppointmentId(), newStatus);

        if (updated) {
            System.out.println("Appointment status updated successfully.");
        } else {
            System.out.println("Failed to update appointment status.");
        }

        pause();
    }

    /**
     * Displays and manages the waitlist.
     */
    /**
     * Displays and manages the waitlist.
     */
    private void manageWaitlist() {
        clearConsole();
        System.out.println("Waitlist for Store ID " + storeId + ":");

        // Fetch the waitlist for the store
        List<Appointment> waitlist = appointmentController.getWaitlistByStoreId(storeId);

        if (waitlist.isEmpty()) {
            System.out.println("The waitlist is currently empty.");
            pause();
            return;
        }

        // Display the waitlist
        for (int i = 0; i < waitlist.size(); i++) {
            Appointment appointment = waitlist.get(i);
            System.out.printf("%d. Customer: %s | Phone: %s | Item: %s | Issue: %s%n",
                    i + 1, appointment.getCustomerName(), appointment.getPhoneNumber(),
                    appointment.getItemName(), appointment.getIssue());
        }

        System.out.print("\nEnter the number of the customer to elevate from the waitlist (or type 'cancel' to exit): ");
        String input = scanner.nextLine().trim();

        if (input.equalsIgnoreCase("cancel")) {
            System.out.println("Exiting waitlist management.");
            pause();
            return;
        }

        int choice;
        try {
            choice = Integer.parseInt(input) - 1;
            if (choice < 0 || choice >= waitlist.size()) {
                throw new IndexOutOfBoundsException();
            }
        } catch (Exception e) {
            System.out.println("Invalid choice. Returning to menu.");
            pause();
            return;
        }

        // Selected customer from the waitlist
        Appointment selectedCustomer = waitlist.get(choice);

        // Schedule an appointment for the selected customer
        System.out.println("Scheduling an appointment for " + selectedCustomer.getCustomerName() + ":");
        System.out.print("Enter preferred appointment date (yyyy-MM-dd): ");
        String appointmentDate = scanner.nextLine();

        List<String> timeSlots = generateTimeSlots();
        List<String> unavailableSlots = appointmentController.getAllAppointments().stream()
                .filter(a -> a.getAppointmentDate().equals(appointmentDate) && a.getStoreId() == storeId)
                .map(Appointment::getAppointmentTime)
                .toList();

        System.out.println("Available time slots:");
        for (int i = 0; i < timeSlots.size(); i++) {
            if (unavailableSlots.contains(timeSlots.get(i))) {
                System.out.printf("%d. %s (Unavailable)%n", i + 1, timeSlots.get(i));
            } else {
                System.out.printf("%d. %s%n", i + 1, timeSlots.get(i));
            }
        }

        System.out.print("Select a time slot (enter the number): ");
        int timeSlotChoice;
        try {
            timeSlotChoice = Integer.parseInt(scanner.nextLine()) - 1;
            if (timeSlotChoice < 0 || timeSlotChoice >= timeSlots.size()) {
                throw new IndexOutOfBoundsException();
            }
        } catch (Exception e) {
            System.out.println("Invalid time slot selection. Returning to menu.");
            return;
        }

        String chosenTime = timeSlots.get(timeSlotChoice);

        if (unavailableSlots.contains(chosenTime)) {
            System.out.println("The selected time slot is unavailable. Customer remains on the waitlist.");
        } else {
            boolean scheduled = appointmentController.createAppointment(
                    storeId, selectedCustomer.getCustomerName(), selectedCustomer.getPhoneNumber(),
                    selectedCustomer.getItemName(), selectedCustomer.getIssue(), appointmentDate, chosenTime);

            if (scheduled) {
                System.out.println("Appointment scheduled successfully for " + selectedCustomer.getCustomerName());
                // Remove the customer from the waitlist
                boolean removedFromWaitlist = appointmentController.removeFromWaitlist(selectedCustomer);
                if (removedFromWaitlist) {
                    System.out.println("Customer removed from the waitlist.");
                } else {
                    System.out.println("Failed to remove customer from the waitlist.");
                }
            } else {
                System.out.println("Failed to schedule the appointment.");
            }
        }

        pause();
    }

    /**
     * Generates a list of available time slots (9 AM to 5 PM, every 30 minutes).
     */
    private List<String> generateTimeSlots() {
        List<String> timeSlots = new ArrayList<>();
        int startHour = 9; // 9 AM
        int endHour = 17; // 5 PM

        for (int hour = startHour; hour < endHour; hour++) {
            timeSlots.add(String.format("%02d:00", hour));
            timeSlots.add(String.format("%02d:30", hour));
        }

        return timeSlots;
    }


    /**
     * Pauses for user input.
     */
    private void pause() {
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }
}
