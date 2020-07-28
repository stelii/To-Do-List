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

    private LiveData<List<Task>> tasksFromList ;

    private MutableLiveData<String> mFilterText = new MutableLiveData<>();

    private MutableLiveData<String> mListName = new MutableLiveData<>();

    private MutableLiveData<TasksList> currentList = new MutableLiveData<>();

    public void setCurrentList(TasksList list){
        currentList.setValue(list);
    }

    public LiveData<TasksList> getCurrentList(){
        return currentList;
    }

    public void setListName(String name){
        mListName.setValue(name);
    }

    public LiveData<String> getListName(){
        return mListName;
    }



    public TaskViewModel(@NonNull Application application) {
        super(application);
        mRepository = new TaskRepository(application);

            tasksFromList = Transformations.switchMap(mFilterText, new Function<String, LiveData<List<Task>>>() {
                @Override
                public LiveData<List<Task>> apply(String input) {
                    if(input == null || input.isEmpty()){
                        Log.d(TAG, "apply: " + "?!!!");
                        return mRepository.getTasksFromList(currentList.getValue().getId());
                    }
                    Log.d(TAG, "apply: " + currentList.getValue().getId());
                    return mRepository.filter(currentList.getValue().getId(),input);
                }
            });

            tasksFromList = Transformations.switchMap(currentList, new Function<TasksList, LiveData<List<Task>>>() {
                @Override
                public LiveData<List<Task>> apply(TasksList input) {
                    return mRepository.getTasksFromList(input.getId());
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

    public LiveData<List<TasksList>> getAllLists(){
        return mRepository.getAllLists();
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

    public long insertList(TasksList tasksList){
       return  mRepository.insertList(tasksList);
    }

    public TasksList getList(long id){
        return mRepository.getList(id);
    }

    public LiveData<List<Task>> getTasksFromList(long id){
        return tasksFromList;
    }

    public void insertTaskToList(TasksList tasksList, Task task){
        mRepository.insertTaskToList(tasksList,task);
    }
}
