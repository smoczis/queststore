package com.codecool.rmbk.view;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.codecool.rmbk.model.usr.User;


public class ConsoleUserView extends ConsoleView implements UserView {

    public String handleMenu(LinkedHashMap<Integer,String> menu) {
        ArrayList<String> options = new ArrayList<>(menu.values());
        showMenu(options);
        return getMenuChoice(options);
    }

    public void showShortInfo(User user) {

        System.out.println(user.getClass().getSimpleName());
        System.out.println(user.getFirstName() + " " + user.getLastName());
    }

    public void showFullInfo(User user) {

        System.out.println("First name: " + user.getFirstName());
        System.out.println("Last name: " + user.getLastName());
        System.out.println("E-mail: " + user.getEmail());
        System.out.println("Adress: " + user.getAddress());
    }

    public String[] getNewUserData(){

        System.out.println("\nEnter new user data");
        String[] labels = new String[]{"first name", "last name", "email", "address"};
        String[] newUserData = new String[4];

        for(int i = 0; i < 4; i++) {
            newUserData[i] = getString("Type new user's " + labels[i] + ": ");
        }
        return newUserData;
    }

    public String handleBrowse(LinkedHashMap<Integer,String> menu, ArrayList<ArrayList<String>> users) {
        ArrayList<String> userNames = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            userNames.add(users.get(i).get(1));
        }
        showEnumeratedList(userNames);
        System.out.println("\n");

        return handleMenu(menu);
    }

    public String handleDetails(LinkedHashMap<Integer,String> menu, User user) {

        showShortInfo(user);
        System.out.println("\n");

        return handleMenu(menu);
    }
}
