package com.codecool.rmbk.controller.web;

import com.codecool.rmbk.dao.SQLQuest;
import com.codecool.rmbk.dao.SQLQuestTemplate;
import com.codecool.rmbk.helper.StringParser;
import com.codecool.rmbk.model.quest.Quest;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class QuestWebController extends CommonHandler {

    private SQLQuest sqlQuest = new SQLQuest();
    private SQLQuestTemplate sqlQuestTemplate = new SQLQuestTemplate();
    private List<String> templateData;
    private Map<String, String> request;
    private String accessLevel;
    private String name;
    private String urlBuy = "templates/buyable.twig";
    private String urlListQuests = "templates/list_student_quests.twig";
    private String urlListMentorQuests = "templates/list_mentor_quests.twig";

    public void handle(HttpExchange httpExchange) throws IOException {

        prepareController(httpExchange);
        request = parseURIstring(getRequestURI());
        handleAccessRights();
    }

    private void prepareController(HttpExchange httpExchange) throws IOException {

        setConnectionData(httpExchange);
        accessLevel = validateRequest();
    }

    private void handleAccessRights() throws IOException {

        name = loggedUser.getFirstName();

        switch (accessLevel) {
            case "Student":
                handleStudentQuest();
                break;
            case "Mentor":
                handleMentorQuest();
                break;
            default:
                send403();
                break;
        }
    }

    private void handleMentorQuest() throws IOException {

        String object = request.get("object");
        String action = request.get("action");

        if (object == null) {
            showAll();
        } else if (object.equals("new")) {
            addQuestTemplate();
        } else if (object.equals("grade")) {
            showQuestsToAccept();
        } else {
            if (action == null) {
                showItem(object);
            } else if (action.equals("remove")) {
                removeTemplate(object);
            } else if (action.equals("edit")) {
                editTemplate(object);
            } else if (action.equals("accept")) {
                acceptQuest(object);
                send302("/quests/grade/");
            }
        }
        send200(response);
    }

    private void showAll() {

        String[] options = {"Add", "Grade"};
        Map <String, String> contextMenu = prepareContextMenu(options);
        Map <String, String> allQuests = sqlQuestTemplate.getTemplatesMap();

        response = webDisplay.getSiteContent(name, mainMenu, contextMenu, allQuests, urlList);
    }

    private void showItem(String object) {

        try {
            Integer.parseInt(object);
            showQuest(object);
        } catch (NumberFormatException e) {
            showTemplate(object);
        }
    }

    private void showTemplate(String object) {

        String[] options = {"Edit", "Remove"};

        Map <String, String> contextMenu = prepareContextMenu(options);
        Map <String, String> allQuests = sqlQuestTemplate.getTemplateInfo(object);

        response = webDisplay.getSiteContent(name, mainMenu, contextMenu, allQuests, urlItem);
    }

    private void showQuest(String object) {

        Map <String, String> allQuests = sqlQuest.getQuestInfo(object);

        response = webDisplay.getSiteContent(name, mainMenu, null, allQuests, urlItem);
    }

    private void addQuestTemplate() throws IOException {

        String method = httpExchange.getRequestMethod();
        String title = "Create New Template:";
        List<String> labels = sqlQuestTemplate.getTemplateLabels();

        if (method.equals("GET")) {
            response = webDisplay.getSiteContent(name, mainMenu, null, title, labels, urlAdd);
        } else if (method.equals("POST")) {
            readQuestInputs();
            Boolean properData = verifyInputs();
            if (properData) {
                sqlQuestTemplate.addQuestTemplate(templateData);
                send302("/quests/");
            } else {
                showFailureMessage();
            }
        }
    }

    private void removeTemplate(String object) throws IOException {

        sqlQuestTemplate.removeQuestTemplate(object);
        send302("/quests/");
    }

    private void editTemplate(String object) throws IOException {

        String method = httpExchange.getRequestMethod();
        String title = "Editing " + StringParser.addWhitespaces(object) + ":";
        Map<String, String> labels = sqlQuestTemplate.getTemplateInfo(object);

        if (method.equals("GET")) {
            response = webDisplay.getSiteContent(name, mainMenu, null, title, labels, urlEdit);
        } else if (method.equals("POST")) {
            readQuestInputs();
            Boolean properData = verifyInputs();
            if (properData) {
                sqlQuestTemplate.editQuestTemplate(object, templateData);
                send302("/quests/");
            } else {
                showFailureMessage();
            }
        }
    }

    private void showQuestsToAccept() {

        String title = "Accept quests submission:";
        Map<String, String> submittedQuests = sqlQuest.getAllSubmittedQuestsMap();

        response = webDisplay.getSiteContent(name, mainMenu, null, title, submittedQuests, urlListMentorQuests);
    }

    private void acceptQuest(String object) {

        Map<String, String> questInfo = sqlQuest.getQuestInfo(object);
        List<String> data = Arrays.asList(questInfo.get("template_name"), questInfo.get("value"));

        Quest quest = new Quest(data, questInfo.get("owner"));
        sqlQuest.acceptQuest(quest);
    }

    private void readQuestInputs() throws IOException {

        Map<String, String> inputs = readInputs();
        templateData = new ArrayList<>();

        templateData.add(inputs.get("name"));
        templateData.add(inputs.get("description"));
        templateData.add(inputs.get("value"));
        templateData.add(inputs.get("special"));
        templateData.add(inputs.get("active"));
    }

    private Boolean verifyInputs() {

        List<Integer> binaries = new ArrayList<>();
        binaries.add(0);
        binaries.add(1);

        try {
            Integer value = Integer.parseInt(templateData.get(2));
            Integer special = Integer.parseInt(templateData.get(3));
            Integer active = Integer.parseInt(templateData.get(4));

            return value > 0 && binaries.contains(special) && binaries.contains(active);

        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void handleStudentQuest() throws IOException {

        String object = request.get("object");
        String action = request.get("action");

        if (object == null) {
            showMyQuests();
        } else if (object.equals("new")) {
            acquireNewQuest();
        } else if (object.equals("submitted")) {
            showSubmitted();
        } else {
            if (action == null) {
                showQuestDetails(object);
            } else if (action.equals("submit")) {
                submitQuest(object);
                send302("/quests/");
            }
        }
        send200(response);
    }

    private void showSubmitted() {

        String title = "My submitted quests";
        String[] options = {"Acquire"};
        Map<String, String> contextMenu = prepareContextMenu(options);
        Map<String, String> submittedQuests = sqlQuest.getSubmittedQuestMapBy(loggedUser);

        response = webDisplay.getSiteContent(name, mainMenu, contextMenu, title, submittedQuests, urlJustList);
    }

    private void showMyQuests() {

        String title = "My pending quests";
        String[] options = {"Acquire", "Submitted"};
        Map<String, String> contextMenu = prepareContextMenu(options);
        Map<String, String> myQuests = sqlQuest.getQuestMapBy(loggedUser);

        response = webDisplay.getSiteContent(name, mainMenu, contextMenu, title, myQuests, urlListQuests);
    }

    private void acquireNewQuest() throws IOException {

        String method = httpExchange.getRequestMethod();
        Map<String, String> availableQuests = sqlQuest.getAvailableQuests(loggedUser);

        if (method.equals("GET")) {
            response = webDisplay.getSiteContent(name, mainMenu, null, availableQuests, urlBuy);
        } else if (method.equals("POST")) {
            readStudentNewQuestsInputs();
            saveNewQuests();
            send302("/quests/");
        }
    }

    private void readStudentNewQuestsInputs() throws IOException {

        Map<String, String> inputs = readInputs();
        templateData = new ArrayList<>();

        for (String entry: inputs.keySet()) {
            templateData.add(entry);
            templateData.add(inputs.get(entry));
        }
    }

    private void saveNewQuests() {

        for (int i = 0 ; i < templateData.size() ; i += 2) {

            String questName = templateData.get(i);
            String questValue = templateData.get(i+1);

            List<String> questData = Arrays.asList(questName, questValue);
            Quest quest = new Quest(questData, loggedUser);

            sqlQuest.getNewQuest(quest);
        }
    }

    private void submitQuest(String object) {

        Map<String, String> questInfo = sqlQuest.getQuestInfo(object);
        List<String> data = Arrays.asList(questInfo.get("template_name"), questInfo.get("value"));

        Quest quest = new Quest(data, loggedUser);
        sqlQuest.submitQuest(quest);
    }

    private void showQuestDetails(String object) {

        Map<String, String> quest = sqlQuest.getQuestInfo(object);

        response = webDisplay.getSiteContent(name, mainMenu, null, quest, urlItem);
    }

}
