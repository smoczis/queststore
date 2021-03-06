package com.codecool.rmbk.model.quest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class QuestTemplateTest {

    String[] data;
    ArrayList<String> listData;

    @BeforeEach
    public void setup() {

    String[] data = {"Explore a dungeon", "Finishing a Teamwork week", "100", "0", "1"};

    ArrayList<String> listData = new ArrayList<>();
    listData.add("Explore a dungeon");
    listData.add("Finishing a Teamwork week");
    listData.add("100");
    listData.add("0");
    listData.add("1");

    this.data = data;
    this.listData = listData;
    }

    @Test
    public void testParameterizedConstuctor1() {

        QuestTemplate questTemplate = new QuestTemplate(data);
        assertNotNull(questTemplate.getName());
    }

    @Test
    public void testParameterizedConstuctior1WithNegativeValue() {

        String[] data = {"Explore a dungeon", "Finishing a Teamwork week", "-100", "0", "1"};
        assertThrows(IllegalArgumentException.class, () -> {
            QuestTemplate questTemplate = new QuestTemplate(data);
        });
    }

    @Test
    public void testParameterizedConstuctor2() {

        QuestTemplate questTemplate = new QuestTemplate(listData);
        assertNotNull(questTemplate.getName());
    }

    @Test
    public void testParameterizedConstuctor2WithNegativeValue() {

        ArrayList<String> listData = new ArrayList<>();
        listData.add("Explore a dungeon");
        listData.add("Finishing a Teamwork week");
        listData.add("-100");
        listData.add("0");
        listData.add("1");

        assertThrows(IllegalArgumentException.class, () -> {
            QuestTemplate questTemplate = new QuestTemplate(listData);
        });
    }

    @Test
    public void testGetName() {

        QuestTemplate questTemplate = new QuestTemplate(data);

        String expected = "Explore a dungeon";

        assertEquals(expected, questTemplate.getName());

    }

    @Test
    public void testSetValueNegativeValue() {

        QuestTemplate questTemplate = new QuestTemplate(data);
        assertThrows(IllegalArgumentException.class, () -> {
            questTemplate.setValue(-100);
        });
    }

    @Test
    public void testSetGetDescription() {

        QuestTemplate questTemplate = new QuestTemplate(data);
        questTemplate.setDescription("test description");
        assertEquals("test description", questTemplate.getDescription());
    }

    @Test
    public void testGetQuestTemplate() {

        QuestTemplate questTemplate = new QuestTemplate(data);
        String[] expected = {"Explore a dungeon", "Finishing a Teamwork week", "100", "0", "1"};

        assertArrayEquals(expected, questTemplate.getQuestTemplate());
    }

    @Test
    public void testUpdateData() {

        QuestTemplate questTemplate = new QuestTemplate(data);

        String [] newData = {"Test description", "50", "0", "1"};
        questTemplate.updateData(newData);

        String[] expected = {"Explore a dungeon", "Test description", "50", "0", "1"};

        assertArrayEquals(expected, questTemplate.getQuestTemplate());
    }
}