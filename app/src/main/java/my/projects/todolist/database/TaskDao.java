package my.projects.todolist.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao()
public interface TaskDao {

    @Insert
    void insert(Task task);

    @Query("SELECT * FROM tasks_table")
    LiveData<List<Task>> getTasks();

    @Delete
    void delete(Task task);

}
