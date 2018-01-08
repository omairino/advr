package com.example.lenovo.start.Models;

import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * Created by Lenovo on 07/01/2018.
 */

public class Items {
    List description;

    public void setDescription(List description) {
        this.description = description;
    }

    public List getDescription() {

        return description;
    }


    public Items(List description) {
        this.description = description;
    }

    public Items() {

    }
}
