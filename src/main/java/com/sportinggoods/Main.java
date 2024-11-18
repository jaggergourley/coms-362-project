package com.sportinggoods;

import com.sportinggoods.menu.MainMenu;
import com.sportinggoods.util.InitializationManager;

/**
 * The entry point of the Sporting Goods Management System.
 */
public class Main {

    public static void main(String[] args) {
        // Initialize the system components
        InitializationManager initManager = new InitializationManager();

        // Instantiate and display the main menu
        MainMenu mainMenu = new MainMenu(initManager);
        mainMenu.display();
    }

}
