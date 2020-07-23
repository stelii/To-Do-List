package my.projects.todolist.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao()
public interface TaskDao {

    @Insert
    void insert(Task task);

    @Query("SELECT * FROM tasks_table")
    LiveData<List<Task>> getTasks();

    @Delete
    void delete(Task task);

    @Update
    void update(Task task);

    @Query("SELECT * FROM tasks_table WHERE id = :id")
    Task getTask(int id);

    @Query("DELETE FROM tasks_table")
    void deleteAll();

    @Query("SELECT * FROM tasks_table WHERE LOWER(name) LIKE '%' || :search || '%'")
    LiveData<List<Task>> filter(String search);

}
