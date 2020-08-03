package my.projects.todolist.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao()
public abstract class TaskDao {

    @Insert
    abstract void insert(Task task);

    @Query("SELECT * FROM tasks_table")
    abstract LiveData<List<Task>> getTasks();

    @Delete
    abstract void delete(Task task);

    @Update
    abstract void update(Task task);

    @Query("SELECT * FROM tasks_table WHERE id = :id")
    abstract Task getTask(int id);

    @Query("SELECT * FROM tasks_table WHERE done == 1")
    abstract LiveData<List<Task>> getCompletedTasks();

    @Query("DELETE FROM tasks_table")
    abstract void deleteAll();

    @Query("SELECT * FROM tasks_table WHERE LOWER(name) LIKE '%' || :search || '%' AND listId == :listId")
    abstract LiveData<List<Task>> filter(long listId ,String search);

    @Insert
    abstract long insertList(TasksList tasksList);

    @Query("SELECT * FROM taskslist_table WHERE id = :id")
    abstract TasksList getList(long id);

    @Query("SELECT * FROM taskslist_table")
    abstract LiveData<List<TasksList>> getAllLists();

    @Delete
    abstract void deleteList(TasksList tasksList);

    @Query("SELECT * FROM tasks_table WHERE listId = :listId")
    abstract LiveData<List<Task>> getTasksFromList(long listId);

    @Transaction
    public void insertTaskToList(TasksList tasksList,Task task){
        task.setListId(tasksList.getId());
        insert(task);
    }

    @Query("DELETE FROM tasks_table WHERE listId = :listId")
    abstract void deleteTasksFromList(long listId);

    @Query("DELETE FROM taskslist_table")
    abstract void deleteAllLists();

}
