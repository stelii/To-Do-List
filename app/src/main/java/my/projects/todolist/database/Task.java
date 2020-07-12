package my.projects.todolist.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import my.projects.todolist.database.converters.PriorityConverter;
import my.projects.todolist.models.Priority;

@Entity(tableName = "tasks_table")
@TypeConverters({PriorityConverter.class})
public class Task {

    @PrimaryKey(autoGenerate = true)
    private int id ;

    private String name ;
    private boolean done ;

    private Priority priority ;

    public Task(TaskBuilder taskBuilder) {
        this.name = taskBuilder.name;
        this.priority = taskBuilder.priority;
    }

    public Task(String name, Priority priority) {
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }


    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }


    public static class TaskBuilder {
        private String name;
        private Priority priority;

        public TaskBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public TaskBuilder setPriority(Priority priority) {
            this.priority = priority;
            return this;
        }

        public Task createTask() {
            return new Task(this);
        }
    }
}
