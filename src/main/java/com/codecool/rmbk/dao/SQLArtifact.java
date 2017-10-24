package com.codecool.rmbk.dao;

public class SQLArtifact extends SqlDAO {

    public void getAllArtifacts() {

        String query = "SELECT * FROM artifacts";

        processQuery(query, null);
    }

    public void getArtifact(String name) {

        String query = "SELECT * FROM artifacts WHERE id = ?;";

        processQuery(query, new String[] {name});
    }

    public void addArtifact(String[] info) {

        String query = "INSERT INTO artifacts (template_name, owner, completion) " +
                       "VALUES (?, ?, ?);" + info;

        processQuery(query, info);
    }

}
