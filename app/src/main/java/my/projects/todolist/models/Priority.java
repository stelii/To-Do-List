package my.projects.todolist.models;

import android.graphics.Color;

import my.projects.todolist.R;

public enum Priority {
    LOW("Low", 0,Color.GREEN),
    MEDIUM("Medium",1,Color.rgb(255,145,17)),
    HIGH("High",2,Color.RED);

    private final String name ;
    private final int value ;
    private final int color ;


    Priority(String name,int value,int color) {
        this.name = name ;
        this.value = value ;
        this.color = color ;
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }

    public int getValue() {
        return value;
    }
}
