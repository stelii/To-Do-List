package my.projects.todolist.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;

import java.util.List;

public class TaskRepository {
    private TaskDao taskDao ;
    private LiveData<List<Task>> tasks;

    public TaskRepository(Application application){
        TaskRoomDatabase taskRoomDatabase = TaskRoomDatabase.getInstance(application.getApplicationContext());
        taskDao = taskRoomDatabase.taskDao();
        tasks = taskDao.getTasks();
    }

    public void insert(Task task){
        new InsertAsyncTask(taskDao).execute(task);
    }

    public LiveData<List<Task>> getTasks(){
        return tasks;
    }

    public void delete(Task task){
        new DeleteAsyncTask(taskDao).execute(task);
    }



    private class InsertAsyncTask extends AsyncTask<Task,Void,Void>{
        private TaskDao taskDao ;

        public InsertAsyncTask(TaskDao taskDao){
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            this.taskDao.insert(tasks[0]);
            return null;
        }
    }

    private class DeleteAsyncTask extends AsyncTask<Task,Void,Void>{
        private TaskDao taskDao ;

        public DeleteAsyncTask(TaskDao taskDao){
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            this.taskDao.delete(tasks[0]);
            return null;
        }
    }
}
