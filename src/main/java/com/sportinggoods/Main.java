package com.sportinggoods;

import com.sportinggoods.menu.RegionalManagerMenu;
import com.sportinggoods.util.InitializationManager;

/**
 * The entry point of the Sporting Goods Management System.
 */
public class Main {

    public static void main(String[] args) {
        // Initialize the system components
        InitializationManager initManager = new InitializationManager();

        // Instantiate and display the regional manager menu
        RegionalManagerMenu regionalManagerMenu = new RegionalManagerMenu(initManager, initManager.getScanner());
        regionalManagerMenu.display();
    }

}
