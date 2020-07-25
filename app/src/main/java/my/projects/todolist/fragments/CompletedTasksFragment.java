package my.projects.todolist.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import my.projects.todolist.R;
import my.projects.todolist.adapters.TaskAdapter;
import my.projects.todolist.database.Task;
import my.projects.todolist.database.TaskViewModel;

public class CompletedTasksFragment extends Fragment {

    private RecyclerView mCompletedTasksRecyclerView ;
    private TaskAdapter mTaskAdapter ;
    private TaskViewModel mTaskViewModel ;

    public CompletedTasksFragment() {
        // Required empty public constructor
    }


    public static CompletedTasksFragment newInstance(String param1, String param2) {
        CompletedTasksFragment fragment = new CompletedTasksFragment();
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
        return inflater.inflate(R.layout.fragment_completed_tasks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCompletedTasksRecyclerView = view.findViewById(R.id.completedTasksFragment_recyclerview_completed_tasks);
        mTaskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        mTaskAdapter = new TaskAdapter();

        mCompletedTasksRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        mCompletedTasksRecyclerView.setAdapter(mTaskAdapter);

        mTaskViewModel.getCompletedTasks().observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                mTaskAdapter.submitList(tasks);
            }
        });
    }
}