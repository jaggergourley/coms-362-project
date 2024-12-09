package com.sportinggoods.model;

public class MaintenanceStaff extends Employee {
    public MaintenanceStaff(String name, int id, Schedule workSchedule,int storeID) {
        super(name, id, workSchedule, storeID);
    }

    public void performMaintenance(MaintenanceRequest request) {
        System.out.println("Performing maintenance for request: " + request.getRequestId());
        request.setStatus("Resolved");
    }
}
