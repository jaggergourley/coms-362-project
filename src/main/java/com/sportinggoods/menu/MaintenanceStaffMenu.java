package com.sportinggoods.menu;

import com.sportinggoods.controller.MaintenanceRequestController;
import com.sportinggoods.model.MaintenanceRequest;
import com.sportinggoods.util.InitializationManager;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Represents the Maintenance Staff Menu in the Sporting Goods Management System.
 * Allows maintenance staff to view and address maintenance issues.
 */
public class MaintenanceStaffMenu extends BaseMenu {
    private int storeId;

    private final MaintenanceRequestController maintenanceRequestController;

    public MaintenanceStaffMenu(InitializationManager initManager, Scanner scanner, int storeId) {
        super(initManager, scanner);
        this.storeId = storeId;
        this.maintenanceRequestController = initManager.getMaintenanceRequestController();
    }

    @Override
    protected void registerCommands() {
        invoker.register("1", this::viewAllIssues);
        invoker.register("2", this::fixMostUrgentIssue);
    }

    @Override
    protected void printMenuOptions() {
        clearConsole();
        System.out.println("\nMaintenance Staff Menu:");
        System.out.println("1. View All Issues");
        System.out.println("2. Fix Most Urgent Issue");
        System.out.println("3. Back to Main Menu");
    }

    @Override
    protected boolean isExitChoice(String choice) {
        return choice.equals("3");
    }

    @Override
    protected void handleExit() {
        System.out.println("Returning to Main Menu...");
    }

    // ==========================
    // Menu Options
    // ==========================

    /**
     * Displays all maintenance issues.
     */
    private void viewAllIssues() {
        List<MaintenanceRequest> requests = maintenanceRequestController.getAllRequests(storeId);

        if (requests.isEmpty()) {
            System.out.println("No maintenance issues logged.");
        } else {
            System.out.println("Logged Maintenance Issues:");
            requests.forEach(System.out::println);
        }
        promptReturn();
    }

    /**
     * Handles the most urgent issue.
     * If resolved, marks it as resolved. Otherwise, updates the status based on alternate flows.
     */
    private void fixMostUrgentIssue() {
        List<MaintenanceRequest> requests = maintenanceRequestController.getAllRequests(storeId);

        if (requests.isEmpty()) {
            System.out.println("No maintenance issues logged.");
            return;
        }

        // Filter out resolved issues and those with timeRemaining == 0
        List<MaintenanceRequest> pendingRequests = requests.stream()
                .filter(request -> request.getTimeRemaining() > 0 && !"Resolved".equalsIgnoreCase(request.getStatus()))
                .collect(Collectors.toList());

        if (pendingRequests.isEmpty()) {
            System.out.println("No pending maintenance issues.");
            return;
        }

        // Sort by timeRemaining
        pendingRequests.sort(Comparator.comparing(MaintenanceRequest::getTimeRemaining));

        MaintenanceRequest urgentRequest = pendingRequests.get(0);
        System.out.println("\nMost Urgent Maintenance Issue:");
        System.out.println(urgentRequest);

        System.out.print("\nWas the issue resolved? (yes/no): ");
        String resolved = scanner.nextLine().trim().toLowerCase();

        if ("yes".equals(resolved)) {
            maintenanceRequestController.updateRequestStatus(urgentRequest.getRequestId(), "Resolved");
            if(urgentRequest.getLocation().equals("HVAC") || urgentRequest.getLocation().equals("Lighting") || urgentRequest.getLocation().equals("Water")){
                String reqLoc = urgentRequest.getLocation();
                String utilityId;
                if(reqLoc.equals("HVAC")){
                    utilityId = "U001";
                }
                else if(reqLoc.equals("Lighting")){
                    utilityId = "U002";
                }
                else{
                    utilityId = "U003";
                }
                boolean success = initManager.getUtilityController().updateUtilityStatus(utilityId, storeId, "Active");

                if (success) {
                    System.out.println("Utility updated.");
                } else {
                    System.out.println("Failed to create maintenance request.");
                }
            }
            System.out.println("Issue marked as resolved.");
        } else {
            handleAlternateFlows(urgentRequest);
        }
        promptReturn();
    }


    /**
     * Handles alternate flows based on the issue's situation.
     *
     * @param request The maintenance request to update.
     */
    private void handleAlternateFlows(MaintenanceRequest request) {
        System.out.println("\nHandling Alternate Flows:");
        System.out.println("1. Immediate Safety Hazard");
        System.out.println("2. External Services Needed");
        System.out.println("3. Parts Shortage");
        System.out.print("Choose an option: ");
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                maintenanceRequestController.updateRequestStatus(request.getRequestId(), "Very Urgent");
                System.out.println("Marked as very urgent. Area should be blocked off.");
                break;

            case "2":
                System.out.print("Enter the name of the external vendor contacted: ");
                String vendor = scanner.nextLine();
                maintenanceRequestController.updateRequestStatus(request.getRequestId(), "External Services Called: " + vendor);
                System.out.println("External services noted.");
                break;

            case "3":
                maintenanceRequestController.updateRequestStatus(request.getRequestId(), "Delayed (Parts Shortage)");
                System.out.println("Issue marked as delayed due to parts shortage.");
                break;

            default:
                System.out.println("Invalid choice. No changes made.");
        }
    }

    /**
     * Prompts the user to press Enter to return to the menu.
     */
    private void promptReturn() {
        System.out.println("\nPress Enter to return to the menu...");
        scanner.nextLine();
    }
}
