package my.projects.todolist.database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "taskslist_table")
public class TasksList {

    @PrimaryKey(autoGenerate = true)
    private int id ;

    private String name ;

    public TasksList(String name){
        this.name = name ;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Ignore
    private List<Task> tasks_of_this_list ;

}
