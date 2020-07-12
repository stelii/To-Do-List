package my.projects.todolist.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;

import java.util.List;
import java.util.concurrent.ExecutionException;

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

    public void update(Task task){
        new UpdateAsyncTask(taskDao).execute(task);
    }

    public Task getTask(int id){
        GetTaskAsyncTask getTaskAsyncTask = new GetTaskAsyncTask(taskDao);
        try {
            return getTaskAsyncTask.execute(id).get();
        } catch (ExecutionException | InterruptedException e) {
            return null ;
        }
    }



    private static class InsertAsyncTask extends AsyncTask<Task,Void,Void>{
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

    private static class DeleteAsyncTask extends AsyncTask<Task,Void,Void>{
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

    private static class UpdateAsyncTask extends AsyncTask<Task,Void,Void>{
        private TaskDao taskDao ;

        public UpdateAsyncTask(TaskDao taskDao){
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.update(tasks[0]);
            return null;
        }
    }

    private static class GetTaskAsyncTask extends AsyncTask<Integer,Void,Task>{
        private TaskDao taskDao ;

        public GetTaskAsyncTask(TaskDao taskDao){
            this.taskDao = taskDao;
        }

        @Override
        protected Task doInBackground(Integer... integers) {
            return taskDao.getTask(integers[0]);
        }
    }
}
