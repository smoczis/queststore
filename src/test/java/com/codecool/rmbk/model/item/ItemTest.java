package com.codecool.rmbk.model.item;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ItemTest {

    @Test
    void testCreateItem() {
        ItemTemplate itemTemplate = mock(ItemTemplate.class);
        when(itemTemplate.getSpecial()).thenReturn("1");
        Item item = new Item(itemTemplate, 1);
        assertNotNull(item);
    }

    @Test
    void testGetSetOwner() {
        ItemTemplate itemTemplate = mock(ItemTemplate.class);
        when(itemTemplate.getSpecial()).thenReturn("1");
        Item item = new Item(itemTemplate, 1);
        item.setOwner(2);
        assertEquals(2, (int) item.getOwner());
    }

    @Test
    void testGetTemplate() {
        ItemTemplate itemTemplate = mock(ItemTemplate.class);
        when(itemTemplate.getSpecial()).thenReturn("1");
        Item item = new Item(itemTemplate, 1);
        assertNotNull(item.getTemplate());
    }

    @Test
    void testGetBuyTime() {
        ItemTemplate itemTemplate = mock(ItemTemplate.class);
        when(itemTemplate.getSpecial()).thenReturn("1");
        Item item = new Item(itemTemplate, 1);
        assertNotNull(item.getBuyTime());
    }

    @Test
    void testGetCompletion() {
        ItemTemplate itemTemplate = mock(ItemTemplate.class);
        when(itemTemplate.getSpecial()).thenReturn("1");
        Item item = new Item(itemTemplate, 1);
        assertEquals("30", item.getCompletion());
    }

    @Test
    void testSetRightCompletion() {
        ItemTemplate itemTemplate = mock(ItemTemplate.class);
        when(itemTemplate.getSpecial()).thenReturn("0");
        Item item = new Item(itemTemplate, 1);
        assertEquals("NULL", item.getCompletion());
    }

    @Test
    void testSetRightOwner() {
        ItemTemplate itemTemplate = mock(ItemTemplate.class);
        when(itemTemplate.getSpecial()).thenReturn("0");
        assertThrows(IllegalArgumentException.class, () -> new Item(itemTemplate, -1));
    }
}