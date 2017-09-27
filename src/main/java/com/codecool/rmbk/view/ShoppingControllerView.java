package com.codecool.rmbk.view;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ShoppingControllerView extends ConsoleUserView {


    public Integer handleMainMenu() {
        return handleMenu(createMainMenu());
    }

    public LinkedHashMap<String, Integer> createMainMenu() {

        LinkedHashMap<String, Integer> mainMenu = new LinkedHashMap<>();

        mainMenu.put("Artifacts", 1);
        mainMenu.put("Quests", 2);
        mainMenu.put("Log out", 0);

        return mainMenu;
    }

}
