package com.sportinggoods.commands;

import java.util.HashMap;
import java.util.Map;

public class MenuInvoker {
    private Map<String, Command> commandMap;

    public MenuInvoker() {
        commandMap = new HashMap<>();
    }

    /**
     * Registers a command with a key.
     *
     * @param key The command key
     * @param command The command to execute
     */
    public void register(String key, Command command) {
        commandMap.put(key, command);
    }

    /**
     * Executes a command based on the key.
     *
     * @param key The command key
     */
    public void executeCommand(String key) {
        Command command = commandMap.get(key);
        if (command != null) {
            command.execute();
        } else {
            System.out.println("Invalid choice. Please try again.");
        }
    }
}
