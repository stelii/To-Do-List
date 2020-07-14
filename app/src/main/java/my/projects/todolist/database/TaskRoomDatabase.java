package my.projects.todolist.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import my.projects.todolist.database.converters.DateConverter;
import my.projects.todolist.database.converters.PriorityConverter;

@Database(entities = {Task.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class,PriorityConverter.class})
public abstract class TaskRoomDatabase extends RoomDatabase{

    public abstract TaskDao taskDao();

    private static TaskRoomDatabase instance ;

    public static synchronized TaskRoomDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context,TaskRoomDatabase.class,"tasks_database")
                    .build();
        }

        return instance ;
    }
}
