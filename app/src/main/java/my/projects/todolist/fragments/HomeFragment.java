package my.projects.todolist.fragments;

import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import my.projects.todolist.R;
import my.projects.todolist.adapters.TaskAdapter;
import my.projects.todolist.database.Task;
import my.projects.todolist.database.TaskViewModel;

import static android.content.ContentValues.TAG;

public class HomeFragment extends Fragment implements TaskAdapter.OnCheckboxListener {
    private TaskViewModel mTaskViewModel ;
    private TaskAdapter mTaskAdapter ;
    private RecyclerView mTaskList ;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTaskAdapter = new TaskAdapter();
        mTaskList = view.findViewById(R.id.home_fragment_item_list_recyclerview);
        mTaskList.setLayoutManager(new LinearLayoutManager(getContext()));
        mTaskList.setAdapter(mTaskAdapter);

        mTaskAdapter.setCheckboxListener(this);

        mTaskViewModel =
                new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                        .getInstance(getActivity().getApplication())).get(TaskViewModel.class);

        mTaskViewModel.getTasks().observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                mTaskAdapter.submitList(tasks);
            }
        });

        view.findViewById(R.id.home_fragment_fab_button_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireActivity(),R.id.nav_host_fragment);
                navController.navigate(R.id.action_homeFragment_to_addEditFragment);
            }
        });



        enableSwipeToDeleteAndUndo(mTaskList);

    }

    private void enableSwipeToDeleteAndUndo(RecyclerView recyclerView){
        ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(0,ItemTouchHelper.START | ItemTouchHelper.END);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();
                Task taskDeleted = mTaskAdapter.getItemAt(pos);
                mTaskViewModel.delete(taskDeleted);
//                final int position = viewHolder.getAdapterPosition();
//                final Task taskToDelete = mTaskAdapter.getItemAt(position);
//                mTaskAdapter.removeAndNotifyItem(position);
//
//                final View parentView = getView();
////                assert parentView != null;
////                parentView.setTag(position);
//
//                Snackbar.make(parentView,"This is a snackbar", BaseTransientBottomBar.LENGTH_LONG)
//                        .addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
//                            @Override
//                            public void onDismissed(Snackbar snackbar, int event) {
//                                super.onDismissed(snackbar,event);
////                                int deletedPosition = (int) parentView.getTag();
//
//                                switch (event){
//                                    case Snackbar.Callback.DISMISS_EVENT_ACTION :
//                                        Log.d(TAG, "onDismissed: " + " se intampla ceva???");
//
//                                        mTaskAdapter.addAndNotifyItem(position,taskToDelete);
//                                        break;
//                                    default:
//                                        mTaskViewModel.delete(taskToDelete);
//                                        break;
//                                }
//                            }
//
//                        })
//                        .setAction("UNDO", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                            }
//                        }).show();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }


    //this method will return true if the task is set to done, false otherwise
    @Override
    public boolean changeItemStatus(Task task) {
        if(task.isDone()) task.setDone(false);
        else task.setDone(true);

        //TODO : update item in database
        return task.isDone();
    }
}