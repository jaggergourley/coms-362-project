package com.sportinggoods.controller;

import com.sportinggoods.model.Appointment;
import com.sportinggoods.repository.AppointmentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class AppointmentController {
    private AppointmentRepository appointmentRepo;

    public AppointmentController(AppointmentRepository appointmentRepo) {
        this.appointmentRepo = appointmentRepo;
    }

    public boolean createAppointment(int storeId, String customerName, String phoneNumber, String itemName, String issue, LocalDateTime appointmentTime) {
        String appointmentId = UUID.randomUUID().toString();
        Appointment appointment = new Appointment(appointmentId, storeId, customerName, phoneNumber, itemName, issue, appointmentTime, "Scheduled");
        return appointmentRepo.addAppointment(appointment);
    }

    public boolean addToWaitlist(int storeId, String customerName, String phoneNumber, String itemName, String issue) {
        return appointmentRepo.addToWaitlist(storeId, customerName, phoneNumber, itemName, issue);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepo.getAllAppointments();
    }
}
