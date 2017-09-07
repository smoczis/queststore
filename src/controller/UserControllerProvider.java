package controller;

import java.util.ArrayList;

public class UserControllerProvider {

    private ArrayList<UserController> userControllers = new ArrayList<>();

    public UserControllerProvider() {

        userControllers.add(new StudentController());
        userControllers.add(new MentorController());
        userControllers.add(new AdminController());
    }

    public UserController getByUserType(String userType) {

        for (UserController controller : userControllers) {
            if (controller.getUserType().equals(userType)) {
                return controller;
            }
        }
        return null;
    }

}
