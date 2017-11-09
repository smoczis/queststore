package com.codecool.rmbk.controller.web;

import com.codecool.rmbk.dao.SQLMenuDAO;
import com.codecool.rmbk.model.usr.User;
import com.codecool.rmbk.view.WebDisplay;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class BacklogWebController extends CommonHandler {

    private SQLMenuDAO sqlMenuDAO = new SQLMenuDAO();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        String response = WebDisplay.getSiteContent(getLoggedUser(httpExchange).getFirstName(),
                sqlMenuDAO.getSideMenu(getLoggedUser(httpExchange)),
                null, "templates/backlog.twig");
        send200(httpExchange, response);
    }
}
