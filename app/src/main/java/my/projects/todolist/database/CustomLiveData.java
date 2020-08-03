package my.projects.todolist.database;

import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import my.projects.todolist.database.TasksList;

public class CustomLiveData extends MediatorLiveData<Pair<TasksList,String>> {

    public CustomLiveData(final LiveData<TasksList> tasksListLiveData, final LiveData<String> filterTextLiveData) {
        addSource(tasksListLiveData, new Observer<TasksList>() {
            @Override
            public void onChanged(TasksList tasksList) {
                setValue(Pair.create(tasksList,filterTextLiveData.getValue()));
            }
        });

        addSource(filterTextLiveData, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                    setValue(Pair.create(tasksListLiveData.getValue(),s));
            }
        });
    }

}
