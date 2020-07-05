package my.projects.todolist.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import my.projects.todolist.R;
import my.projects.todolist.database.Task;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private AsyncListDiffer<Task> mDiffer = new AsyncListDiffer<Task>(this,DIFF_CALLBACK);

    private static final DiffUtil.ItemCallback<Task> DIFF_CALLBACK = new DiffUtil.ItemCallback<Task>() {
        @Override
        public boolean areItemsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem.getName().equals(newItem.getName());
        }
    };

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.task_item,parent,false);

        TaskViewHolder taskViewHolder = new TaskViewHolder(itemView);
        return taskViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = mDiffer.getCurrentList().get(position);
        holder.mTaskName.setText(task.getName());
    }

    @Override
    public int getItemCount() {
        return mDiffer.getCurrentList().size();
    }

    public Task getItemAt(int position){
        return mDiffer.getCurrentList().get(position);
    }

    public void submitList(List<Task> submitedTasks){
        mDiffer.submitList(submitedTasks);
    }

    public void addAndNotifyItem(int position,Task task){
        mDiffer.getCurrentList().add(position,task);
        notifyItemInserted(position);
    }

    public void removeAndNotifyItem(int position){
        mDiffer.getCurrentList().remove(position);
        notifyItemRemoved(position);
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView mTaskName;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            mTaskName = itemView.findViewById(R.id.task_from_list_name);
        }
    }
}
