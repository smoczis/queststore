package com.codecool.rmbk.controller.web;

import com.codecool.rmbk.dao.SQLSession;
import com.codecool.rmbk.helper.CookieHandler;
import com.codecool.rmbk.helper.MimeTypeResolver;
import com.codecool.rmbk.model.Session;
import com.codecool.rmbk.model.usr.User;
import com.codecool.rmbk.view.WebDisplay;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public abstract class CommonHandler implements HttpHandler {

    String response;
    HttpExchange httpExchange;
    CookieHandler cookieHandler;
    WebDisplay webDisplay = new WebDisplay();
    SQLSession sessionDao = new SQLSession();

    static User user;
    static Session session;

    void send404() throws IOException {

        URL file404 = getFileURL("./static/404.html");

        if (file404 == null) {
            sendString("404: not found", 404);
        } else {
            sendFile(file404, 404);
        }
    }

    void send302(URI location) throws IOException {

        httpExchange.getResponseHeaders().set("Location", location.toString());
        httpExchange.sendResponseHeaders(302, -1);
    }

    void send302(String location) throws IOException {

        httpExchange.getResponseHeaders().set("Location", location);
        httpExchange.sendResponseHeaders(302, -1);
    }

    void send200(String response) throws IOException {

        sendString(response, 200);
    }

    void send200(URL fileURL) throws IOException {

        if (fileURL == null) {
            send401();
        } else {
            sendFile(fileURL, 200);
        }
    }

    void send403() throws IOException {

        URL file403 = getFileURL("./static/403.html");
        if (file403 == null) {
            sendString("403: access denied", 403);
        } else {
            sendFile(file403, 403);
        }

    }

    private void send401() throws IOException {

        URL file401 = getFileURL("./static/401.html");
        if (file401 == null) {
            sendString("401: session expired", 401);
        } else {
            sendFile(file401, 401);
        }
    }

    String validateRequest() throws IOException {


        String sessionStatus = cookieHandler.getSessionStatus();
        Boolean active = sessionDao.isSessionActive(cookieHandler.getSessionId());
        String requestStatus = null;

        if (sessionStatus == null || sessionStatus.equals("loggedOut")) {
            send302("/login");
        } else {
            if (active) {
                requestStatus = user.getAccessLevel();
            } else {
                requestStatus = "expired";
                cookieHandler.clearCookie();
                send401();
            }
        }
        return requestStatus;
    }

    void sendFile(URL fileURL, int httpCode) throws IOException {

        File file = new File(fileURL.getFile());

        MimeTypeResolver resolver = new MimeTypeResolver(file);
        String mime = resolver.getMimeType();

        httpExchange.getResponseHeaders().set("Content-Type", mime);
        httpExchange.sendResponseHeaders(httpCode, 0);

        OutputStream os = httpExchange.getResponseBody();

        FileInputStream fs = new FileInputStream(file);
        final byte[] buffer = new byte[0x10000];
        int count;
        while ((count = fs.read(buffer)) >= 0) {
            os.write(buffer,0,count);
        }
        os.close();
    }

    private void sendString(String response, int httpCode) throws IOException {

        httpExchange.sendResponseHeaders(httpCode, response.getBytes().length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    URL getFileURL(String path) {

        ClassLoader classLoader = getClass().getClassLoader();
        return classLoader.getResource(path);
    }

    void setHttpExchange(HttpExchange httpExchange) {

        this.httpExchange = httpExchange;
        cookieHandler = new CookieHandler(httpExchange);

        if (session != null) {
            sessionDao.updateSession(session);
        }
    }

    void clearUser() {

        user = null;
        session = null;
    }

    Map<String, String> parseFormData(String formData) throws IOException {

        Map<String, String> map = new HashMap<>();
        String[] pairs = formData.split("&");

        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            String value = URLDecoder.decode(keyValue[1], "UTF-8");
            map.put(keyValue[0], value);
        }
        return map;
    }
  
    Map<String, String> prepareMentorOptions(String location) {

        Map<String, String> options = new HashMap<>();
        options.put("Display", prepareURI(location, "display"));
        options.put("Edit", prepareURI(location, "edit"));
        options.put("Add", prepareURI(location, "add"));
        options.put("Delete", prepareURI(location, "delete"));

        return options;
    }

    Map<String, String> prepareStudentOptions(String location) {

        Map<String, String> options = new HashMap<>();
        options.put("Display", prepareURI(location, "display"));
        options.put("Edit", prepareURI(location, "edit"));
        options.put("Acquire", prepareURI(location, "acquire"));

        return options;
    }

    private String prepareURI(String location, String option) {

        StringBuilder URI = new StringBuilder();
        URI.append("/");
        URI.append(location);
        URI.append("/");
        URI.append(option);

        return URI.toString();
    }
}
