package com.sportinggoods.menu;

import com.sportinggoods.commands.MenuInvoker;
import com.sportinggoods.util.InitializationManager;

import java.util.Scanner;

/**
 * Abstract base class for all menus, providing common functionality for display and input handling.
 */
public abstract class BaseMenu {
    protected InitializationManager initManager;
    protected MenuInvoker invoker;
    protected Scanner scanner;

    /**
     * Initializes the menu with shared dependencies.
     *
     * @param initManager InitializationManager for dependencies.
     * @param scanner     Shared Scanner for user input.
     */
    public BaseMenu(InitializationManager initManager, Scanner scanner) {
        this.initManager = initManager;
        this.invoker = new MenuInvoker();
        this.scanner = scanner;
        registerCommands();
    }

    /**
     * Clears the console.
     */
    protected void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            for (int i = 0; i < 50; i++) System.out.println(); // Fallback
        }
    }

    /**
     * Displays the menu and handles input.
     */
    public void display() {
        while (true) {
            printMenuOptions();
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine().trim();

            if (isExitChoice(choice)) {
                handleExit();
                break;
            }

            invoker.executeCommand(choice);
        }
    }

    /**
     * Handles exit action.
     */
    protected abstract void handleExit();

    /**
     * Checks if the input is an exit choice.
     *
     * @param choice User input.
     * @return True if exit choice, else false.
     */
    protected abstract boolean isExitChoice(String choice);

    /**
     * Prints menu options.
     */
    protected abstract void printMenuOptions();

    /**
     * Registers menu commands.
     */
    protected abstract void registerCommands();
}
