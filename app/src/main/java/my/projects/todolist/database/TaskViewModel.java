package my.projects.todolist.database;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    private LiveData<List<Task>> tasks ;
    private TaskRepository mRepository ;
    public static final String TAG = "TaskViewModel";



    private MutableLiveData<String> mFilterText = new MutableLiveData<>();


    public TaskViewModel(@NonNull Application application) {
        super(application);
        mRepository = new TaskRepository(application);
        tasks = Transformations.switchMap(mFilterText, new Function<String, LiveData<List<Task>>>() {
            @Override
            public LiveData<List<Task>> apply(String input) {
                if(input == null || input.isEmpty()){
                    Log.d(TAG, "apply: " + "?!!!");
                    return mRepository.getTasks();
                }
                return mRepository.filter(input);
            }
        });
    }

    public void setFilter(String query){
        Log.d(TAG, "setFilter: " + query);
        mFilterText.setValue(query);
//        tasks = Transformations.switchMap(mFilterText, new Function<String, LiveData<List<Task>>>() {
//            @Override
//            public LiveData<List<Task>> apply(String input) {
//                if(input == null || input.isEmpty()){
//                    Log.d(TAG, "apply: " + "?!!!");
//                    return mRepository.getTasks();
//                }
//                return mRepository.filter(input);
//            }
//        });
    }


    public void insert(Task task){
        mRepository.insert(task);
    }

    public LiveData<List<Task>> getTasks(){
        return tasks;
    }

    public LiveData<List<Task>> getCompletedTasks(){
        return mRepository.getCompletedTasks();
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
