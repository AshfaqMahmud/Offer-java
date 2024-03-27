package com.example.offer_java.ImageLoadingClasses;

import java.util.ArrayList;

public class ParentModelClass {
    String title;
    ArrayList<ChildModelClass> childModelClassArrayList;

    public ParentModelClass(String title, ArrayList<ChildModelClass> childModelClassArrayList) {
        this.title = title;
        this.childModelClassArrayList = childModelClassArrayList;
    }
}
