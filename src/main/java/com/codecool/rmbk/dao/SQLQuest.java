package com.codecool.rmbk.dao;

import java.util.ArrayList;

public class SQLQuest extends SqlDAO {

    public void getAllQuests() {
        String query = "SELECT * FROM quests";

        processQuery(query);
    }

    public void getQuestByOwner(int owner) {
        String query = "SELECT * FROM quests WHERE owner = '" + owner + "';";

        processQuery(query);
    }
}