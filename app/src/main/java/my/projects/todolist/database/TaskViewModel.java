package my.projects.todolist.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    private LiveData<List<Task>> tasks;
    private TaskRepository mRepository ;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        mRepository = new TaskRepository(application);
        tasks = mRepository.getTasks();
    }

    public void insert(Task task){
        mRepository.insert(task);
    }

    public LiveData<List<Task>> getTasks(){
        return tasks;
    }

    public void delete(Task task){
        mRepository.delete(task);
    }

    public void update(Task task){
        mRepository.update(task);
    }

    public Task getTask(int idPassed) {
        return mRepository.getTask(idPassed);
    }

    public void deleteAll(){
        mRepository.deleteAll();
    }
}
