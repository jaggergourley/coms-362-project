package com.sportinggoods.commands;

import java.util.HashMap;
import java.util.Map;

public class MenuInvoker {
    private Map<String, Runnable> commandMap;

    public MenuInvoker() {
        commandMap = new HashMap<>();
    }

    /**
     * Registers a command with a key.
     *
     * @param key The command key (e.g., user input)
     * @param command The command to execute
     */
    public void register(String key, Runnable command) {
        commandMap.put(key, command);
    }

    /**
     * Executes a command based on the key.
     *
     * @param key The command key
     */
    public void executeCommand(String key) {
        Runnable command = commandMap.get(key);
        if (command != null) {
            command.run();
        } else {
            System.out.println("Invalid choice. Please try again.");
        }
    }
}
