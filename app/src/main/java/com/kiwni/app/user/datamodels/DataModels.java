package com.kiwni.app.user.datamodels;

import com.kiwni.app.user.interfaces.BookingListItemClickListener;

import java.util.List;

public class DataModels {

    private List<BookingModel> nestedList;
    private String itemText;
    private boolean isExpandable;
    BookingListItemClickListener listener;

    public DataModels(List<BookingModel> itemList, String itemText, BookingListItemClickListener listener) {
        this.nestedList = itemList;
        this.itemText = itemText;
        this.listener = listener;
        isExpandable = false;
    }

    public void setExpandable(boolean expandable) {
        isExpandable = expandable;
    }

    public List<BookingModel> getNestedList() {
        return nestedList;
    }

    public String getItemText() {
        return itemText;
    }

    public boolean isExpandable() {
        return isExpandable;
    }
}