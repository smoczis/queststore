package com.codecool.rmbk.model.quest;

import java.time.LocalDate;
import java.util.ArrayList;

import com.codecool.rmbk.model.usr.User;

public class Quest {

    private Integer ownerID;
    private String templateName;
    private LocalDate startTime;
    private String originalValue;

    public Quest(ArrayList<String> template, User user) {

        this.ownerID = user.getID();
        this.templateName = template.get(0);
        this.startTime = LocalDate.now();
        this.originalValue = template.get(2);
    }

    public Integer getOwnerID() {

        return ownerID;
    }

    public String getTemplateName() {

        return templateName;
    }

    public String getStartTime() {

        return startTime.toString();
    }

    public String getValue() {

        return this.originalValue;
    }
}