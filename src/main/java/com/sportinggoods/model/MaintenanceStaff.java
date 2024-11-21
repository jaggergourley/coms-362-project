package com.sportinggoods.model;

public class MaintenanceStaff extends Employee {
    public MaintenanceStaff(String name, int id, Schedule workSchedule) {
        super(name, id, workSchedule);
    }

    public void performMaintenance(MaintenanceRequest request) {
        System.out.println("Performing maintenance for request: " + request.getRequestId());
        request.setStatus("Resolved");
    }
}
