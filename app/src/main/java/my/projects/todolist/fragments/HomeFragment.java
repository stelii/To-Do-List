package my.projects.todolist.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
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

public class HomeFragment extends Fragment {
    private TaskViewModel mTaskViewModel ;
    private TaskAdapter mTaskAdapter ;
    private RecyclerView mTaskList ;

    public static int count = 1 ;

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
    }


}