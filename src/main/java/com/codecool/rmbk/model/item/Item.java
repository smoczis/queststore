package com.codecool.rmbk.model.item;

import java.time.LocalDateTime;
import java.util.ArrayList;
import com.codecool.rmbk.model.usr.Holder;
import com.codecool.rmbk.model.item.ItemTemplate.CATEGORY;

public class Item {

    private Holder owner;
    private LocalDateTime buyTime;
    private int id;
    private CATEGORY category;
    private static ArrayList<Item> itemList = new ArrayList<>();

    public Item(Holder owner) {

        this.id = id;
        this.owner = owner;
        this.buyTime = LocalDateTime.now();
        this.category = CATEGORY.BASIC;
        itemList.add(this);
    }

    public Holder getOwner() {

        return this.owner;
    }

    public void setOwner(Holder owner) {

        this.owner = owner;
    }

    public LocalDateTime getBuyTime() {

        return this.buyTime;
    }

    public void setBuyTime(LocalDateTime buyTime) {

        this.buyTime = buyTime;
    }

    public CATEGORY getCategory() {

        return this.category;
    }

    public void setCategory(CATEGORY category) {

        this.category = category;
    }
}
