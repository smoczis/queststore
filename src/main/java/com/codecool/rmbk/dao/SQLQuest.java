package com.codecool.rmbk.dao;

import com.codecool.rmbk.helper.StringParser;
import com.codecool.rmbk.model.quest.Quest;
import com.codecool.rmbk.model.usr.Holder;

import java.util.*;

public class SQLQuest extends SqlDAO {

    private SQLBacklog backlog = new SQLBacklog();

    public ArrayList<ArrayList<String>> getMyQuests(Integer id, String teamName) {

        String query;

        if (teamName.equals("solo")) {
            query = "SELECT * FROM quests WHERE owner = ?;";
            processQuery(query, new String[] {"" + id});
        } else {
            query = "SELECT * FROM quests JOIN groups, user_groups " +
                    "ON quests.owner = user_groups.user_id AND groups.id = user_groups.group_id " +
                    "WHERE groups.name = ?;";
            processQuery(query, new String[] {teamName});
        }

        return getResults();
    }

    public Map<String,String> getQuestMap(Integer id, String teamName) {

        List<ArrayList<String>> questList = getMyQuests(id, teamName).subList(1, getResults().size());
        Map<String,String> result = new HashMap<>();
        for(ArrayList<String> arr : questList) {
            result.put(arr.get(0), arr.get(1));
        }
        return result;
    }

    public void getNewQuest(Quest quest) {

        String name = quest.getTemplateName();
        String owner = quest.getOwnerID().toString();
        String today = quest.getStartTime();
        String value = quest.getValue();

        String query = "INSERT INTO quests(template_name, owner) VALUES(?, ?);";

        backlog.saveToBacklog(new String[] {today, name, "acquired", value, owner});
        processQuery(query, new String[] {name, owner});
    }

    public void submitQuest(Quest quest) {

        String name = quest.getTemplateName();
        String owner = quest.getOwnerID().toString();
        String today = quest.getStartTime();
        String value = quest.getValue();

        String query = "UPDATE `quests` " +
                "SET return_date = ? " +
                "WHERE owner = ? AND template_name = ?;";

        backlog.saveToBacklog(new String[] {today, name, "submitted", value, owner});
        processQuery(query, new String[] {today, owner, name});
    }

    public void acceptQuest(Quest quest) {

        String name = quest.getTemplateName();
        String owner = quest.getOwnerID().toString();
        String today = quest.getStartTime();
        String value = quest.getValue();

        String query = "UPDATE `quests` " +
                "SET accept_date = ? " +
                "WHERE owner = ? AND template_name = ?;";

        backlog.saveToBacklog(new String[] {today, name, "accepted", value, owner});
        processQuery(query, new String[] {today, owner, name});
    }

    public Map<String,String> getQuestMapBy(Holder holder) {

        Map<String,String> result = new HashMap<>();

        String query = "SELECT `id`, `template_name` " +
                "FROM quests " +
                "WHERE `owner` = ? AND return_date IS NULL;";
        String[] data = {String.valueOf(holder.getID())};

        processQuery(query, data);

        for(ArrayList<String> outcome : getResults().subList(1, getResults().size())) {
            String href = "/quests/" + outcome.get(0);
            String name = outcome.get(1);
            result.put(href, name);
        }

        return result;
    }

    public Map<String,String> getSubmittedQuestMapBy(Holder holder) {

        Map<String,String> result = new HashMap<>();

        String query = "SELECT `id`, `template_name` " +
                "FROM quests " +
                "WHERE `owner` = ? AND accept_date IS NULL AND return_date IS NOT NULL;";
        String[] data = {String.valueOf(holder.getID())};

        processQuery(query, data);

        for(ArrayList<String> outcome : getResults().subList(1, getResults().size())) {
            String href = "/quests/" + outcome.get(0);
            String name = outcome.get(1);
            result.put(href, name);
        }

        return result;
    }

    public Map<String, String> getQuestInfo(String templateId) {

        Map<String,String> result = new HashMap<>();

        String query = "SELECT template_name, accept_date, return_date, quest_template.value " +
                "FROM quests " +
                "JOIN quest_template " +
                "ON quests.template_name = quest_template.name " +
                "WHERE id = ?;";
        String[] data = {StringParser.addWhitespaces(templateId)};

        processQuery(query, data);

        for(int i=0; i<getResults().get(0).size(); i++) {
            String key = getResults().get(0).get(i);
            String value = getResults().get(1).get(i);
            result.put(key, value);
        }
        return result;
    }

    public Map<String, String> getAvailableQuests(Holder holder) {

        Map<String,String> result = new TreeMap<>();

        String query = "SELECT name, value FROM quest_template " +
                "WHERE name NOT IN " +
                "(SELECT template_name FROM quests WHERE owner = ?);";
        String[] data = {"" + holder.getID()};

        processQuery(query, data);

        for(ArrayList<String> outcome : getResults().subList(1, getResults().size())) {
            String name = outcome.get(0);
            String value = outcome.get(1);
            result.put(name, value);
        }

        return result;
    }

}
