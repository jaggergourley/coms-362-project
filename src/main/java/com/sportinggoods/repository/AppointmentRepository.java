package com.sportinggoods.repository;

import com.sportinggoods.model.Appointment;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AppointmentRepository {
    private static final String FILE_PATH = "data/appointments.csv";
    private static final String WAITLIST_FILE_PATH = "data/waitlist.csv";

    public AppointmentRepository() {
        initializeFiles();
    }

    /**
     * Initializes the CSV files if they do not exist.
     */
    private void initializeFiles() {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    writer.write("appointmentId,storeId,customerName,customerPhone,itemName,issue,appointmentTime,status\n");
                }
            }

            File waitlistFile = new File(WAITLIST_FILE_PATH);
            if (!waitlistFile.exists()) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(waitlistFile))) {
                    writer.write("appointmentId,storeId,customerName,customerPhone,itemName,issue,status\n");
                }
            }
        } catch (IOException e) {
            System.err.println("Error initializing files: " + e.getMessage());
        }
    }

    /**
     * Adds a new appointment.
     *
     * @param appointment The appointment to add.
     * @return True if the appointment is added successfully.
     */
    public boolean addAppointment(Appointment appointment) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(appointment.toCSV());
            writer.newLine();
            return true;
        } catch (IOException e) {
            System.err.println("Error writing to appointments file: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves all appointments.
     *
     * @return A list of all appointments.
     */
    public List<Appointment> getAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                Appointment appointment = Appointment.fromCSV(line);
                if (appointment != null) {
                    appointments.add(appointment);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading appointments file: " + e.getMessage());
        }
        return appointments;
    }

    /**
     * Retrieves all appointments by storeId.
     *
     * @param storeId The store ID to filter appointments.
     * @return A list of appointments for the specified store.
     */
    public List<Appointment> getAppointmentsByStoreId(int storeId) {
        return getAllAppointments().stream()
                .filter(appointment -> appointment.getStoreId() == storeId)
                .collect(Collectors.toList());
    }

    /**
     * Updates the status of an appointment.
     *
     * @param appointmentId The ID of the appointment to update.
     * @param newStatus     The new status to set.
     * @return True if the update is successful.
     */
    public boolean updateAppointmentStatus(String appointmentId, String newStatus) {
        List<Appointment> appointments = getAllAppointments();
        boolean updated = false;

        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentId().equals(appointmentId)) {
                appointment.setStatus(newStatus);
                updated = true;
                break;
            }
        }

        if (updated) {
            saveAppointmentsToFile(appointments);
        }

        return updated;
    }

    /**
     * Saves all appointments back to the file.
     *
     * @param appointments The list of appointments to save.
     */
    private void saveAppointmentsToFile(List<Appointment> appointments) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write("appointmentId,storeId,customerName,customerPhone,itemName,issue,appointmentTime,status\n");
            for (Appointment appointment : appointments) {
                writer.write(appointment.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving appointments to file: " + e.getMessage());
        }
    }

    /**
     * Adds a customer to the waitlist.
     *
     * @return True if added successfully.
     */
    public boolean addToWaitlist(int storeId, String customerName, String phoneNumber, String itemName, String issue) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(WAITLIST_FILE_PATH, true))) {
            writer.write(storeId + "," + customerName + "," + phoneNumber + "," + itemName + "," + issue);
            writer.newLine();
            return true;
        } catch (IOException e) {
            System.err.println("Error saving to waitlist: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves the waitlist.
     *
     * @return A list of all waitlist entries.
     */
    public List<Appointment> getWaitlist() {
        List<Appointment> waitlist = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(WAITLIST_FILE_PATH))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                Appointment appointment = Appointment.fromCSV(line);
                if (appointment != null) {
                    waitlist.add(appointment);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading waitlist file: " + e.getMessage());
        }
        return waitlist;
    }

    public List<Appointment> getWaitlistByStoreId(int storeId) {
        List<Appointment> waitlist = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(WAITLIST_FILE_PATH))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                Appointment appointment = Appointment.fromCSV(line);
                if (appointment != null && appointment.getStoreId() == storeId) {
                    waitlist.add(appointment);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading waitlist: " + e.getMessage());
        }
        return waitlist;
    }

    public boolean removeFromWaitlist(Appointment appointment) {
        List<Appointment> waitlist = new ArrayList<>();
        boolean removed = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(WAITLIST_FILE_PATH))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                Appointment current = Appointment.fromCSV(line);
                if (current != null && !current.equals(appointment)) {
                    waitlist.add(current);
                } else if (current != null && current.equals(appointment)) {
                    removed = true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading waitlist for removal: " + e.getMessage());
            return false;
        }

        if (removed) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(WAITLIST_FILE_PATH))) {
                writer.write("storeId,customerName,itemName,issue,timeSlot\n"); // Write header
                for (Appointment a : waitlist) {
                    writer.write(a.toCSV() + "\n");
                }
                return true;
            } catch (IOException e) {
                System.err.println("Error writing updated waitlist: " + e.getMessage());
                return false;
            }
        }

        return false;
    }
}
