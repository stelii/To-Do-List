package my.projects.todolist.database.converters;


import android.text.TextUtils;

import androidx.room.TypeConverter;

import my.projects.todolist.models.Priority;

public class PriorityConverter {

    @TypeConverter
    public static String fromPriorityToString(Priority priority){
        if(priority == null) return null ;

        return priority.getName();
    }

    @TypeConverter
    public static Priority fromStringToPriority(String priority){
        if(TextUtils.isEmpty(priority)) return null;

        for(Priority p : Priority.values()){
            if(p.getName().equals(priority)) return p ;
        }

        throw new IllegalArgumentException(priority + " is not valid");
    }
}
