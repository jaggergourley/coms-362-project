package com.sportinggoods.controller;

import com.sportinggoods.model.Appointment;
import com.sportinggoods.repository.AppointmentRepository;

import java.util.List;
import java.util.UUID;

public class AppointmentController {
    private AppointmentRepository appointmentRepo;

    public AppointmentController(AppointmentRepository appointmentRepo) {
        this.appointmentRepo = appointmentRepo;
    }

    public boolean createAppointment(int storeId, String customerName, String phoneNumber, String itemName,
                                     String issue, String appointmentDate, String appointmentTime) {
        String appointmentId = UUID.randomUUID().toString();
        Appointment appointment = new Appointment(appointmentId, storeId, customerName, phoneNumber, itemName,
                issue, appointmentDate, appointmentTime, "Scheduled");
        return appointmentRepo.addAppointment(appointment);
    }

    public boolean addToWaitlist(int storeId, String customerName, String phoneNumber, String itemName, String issue) {
        return appointmentRepo.addToWaitlist(storeId, customerName, phoneNumber, itemName, issue);
    }

    public boolean removeFromWaitlist(Appointment appointment) {
        return appointmentRepo.removeFromWaitlist(appointment);
    }

    public List<Appointment> getWaitlistByStoreId(int storeId){
        return appointmentRepo.getWaitlistByStoreId(storeId);
    }


    public List<Appointment> getAllAppointments() {
        return appointmentRepo.getAllAppointments();
    }


    public boolean updateAppointmentStatus(String appointmentId, String newStatus) {

        List<Appointment> appointments = appointmentRepo.getAllAppointments();


        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentId().equals(appointmentId)) {
                // Update the status
                appointment.setStatus(newStatus);

                // Save changes to the repository
                return appointmentRepo.updateAppointments(appointments);
            }
        }

        return false;
    }
}
