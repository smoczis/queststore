package com.codecool.rmbk.controller;

import com.codecool.rmbk.model.usr.User;
import com.codecool.rmbk.model.usr.Mentor;
import com.codecool.rmbk.model.usr.Class;
import com.codecool.rmbk.view.ConsoleAdminView;

public class AdminController extends UserController {

    ConsoleAdminView view;

    public AdminController() {

        view = new ConsoleAdminView();
        super.view = view;
    }

    public void start (User admin) {

        setUser(admin);
        handleMainMenu();
    }

    public void handleMainMenu() {

        boolean isBrowsed = true;

        while (isBrowsed) {
            view.clearScreen();
            view.showShortInfo(user);
            Integer choice = view.handleMainMenu();

            if (choice == 1) {
                view.showFullInfo(user);
                editUserData(user);
            } else if (choice == 2) {
                ClassController classController = new ClassController();
                classController.start(user);
            } else if (choice == 3) {
                MentorController mentorController = new MentorController();
                mentorController.start(user);
            } else if (choice == 0) {
                isBrowsed = false;
            }
        }
    }

    public String getUserType() {

        return "Admin";
    }
}