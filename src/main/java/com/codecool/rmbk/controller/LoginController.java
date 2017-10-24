package com.codecool.rmbk.controller;

import com.codecool.rmbk.view.LoginView;
import com.codecool.rmbk.view.ConsoleLoginView;
import com.codecool.rmbk.dao.*;

public class LoginController {

    private UserControllerProvider provider;
    private LoginView view;
    private UserInfoDAO userDao;
    private Boolean mainAppStarted = false;

    public LoginController() {

        provider = new UserControllerProvider();
        view = new ConsoleLoginView();
    }

    public void startSqlLoginService() {

        SQLLoginDAO.setPermission();
        LoginDAO dataAccess = new SQLLoginDAO();
        userDao = new SQLUsers();

        while (!mainAppStarted) {
            String[] loginInfo = view.LoginScreen();

            if (dataAccess.login(loginInfo)) {
                startUserController(loginInfo[0]);
            } else {
                view.showWrongDataMessage();
            }
        }
    }

    private void startUserController(String userLogin) {

        mainAppStarted = true;

        String userType = userDao.getUserTypeByLogin(userLogin);
        UserController userController = provider.getByUserType(userType);
        userController.start(userDao.getUserByLogin(userLogin));
    }

}
