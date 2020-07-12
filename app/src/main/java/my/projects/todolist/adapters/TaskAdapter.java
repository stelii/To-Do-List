package my.projects.todolist.adapters;

import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import my.projects.todolist.R;
import my.projects.todolist.database.Task;

import static android.content.ContentValues.TAG;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private OnCheckboxListener mcheckboxListener ;
    private OnItemClickListener mOnItemClickListener;

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

        TaskViewHolder taskViewHolder = new TaskViewHolder(itemView,mOnItemClickListener);
        return taskViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = mDiffer.getCurrentList().get(position);
        holder.displayItem(task);

        //TODO : Add functionality to sort the list depending on priority

    }


    @Override
    public int getItemCount() {
        return mDiffer.getCurrentList().size();
    }

    public void setCheckboxListener(OnCheckboxListener listener){
        mcheckboxListener = listener;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mOnItemClickListener = listener;
    }

    public Task getItemAt(int position){
        return mDiffer.getCurrentList().get(position);
    }

    public void submitList(List<Task> submitedTasks){
        mDiffer.submitList(submitedTasks);
    }


    public class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTaskName;
        private CheckBox mCheckBox;
        private ImageView mPriorityArrow ;

        private OnItemClickListener mOnItemClickListener;

        public TaskViewHolder(@NonNull View itemView,OnItemClickListener listener) {
            super(itemView);
            mOnItemClickListener = listener ;
            itemView.setOnClickListener(this);

            mTaskName = itemView.findViewById(R.id.task_item_name);
            mCheckBox = itemView.findViewById(R.id.task_item_checkbox);
            mPriorityArrow = itemView.findViewById(R.id.task_item_priority_icon);

            mCheckBox.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    setItemStatus();
                }
            });
        }

        public void displayItem(Task task){
            mTaskName.setText(task.getName());

            if (task.isDone()) {
                mTaskName.setPaintFlags(mTaskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                mCheckBox.setChecked(true);
            } else {
                mTaskName.setPaintFlags(mTaskName.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                mCheckBox.setChecked(false);
            }
            mPriorityArrow.setColorFilter(task.getPriority().getColor());

        }


        private void setItemStatus(){
            int position = getAdapterPosition();
            Task task = getItemAt(position);

            boolean taskStatus = mcheckboxListener.changeItemStatus(task);
            if (taskStatus) mTaskName.setPaintFlags(mTaskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            else mTaskName.setPaintFlags(mTaskName.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick: " + "am apasat");
            int position = getAdapterPosition();
            Task task = getItemAt(position);
            this.mOnItemClickListener.onItemShortClick(task);
        }
    }


    public interface OnCheckboxListener{
        boolean changeItemStatus(Task task);
    }

    public interface OnItemClickListener{
        void onItemShortClick(Task task);
    }
}
