package my.projects.todolist.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import my.projects.todolist.R;
import my.projects.todolist.database.Task;
import my.projects.todolist.database.TaskViewModel;
import my.projects.todolist.database.converters.PriorityConverter;
import my.projects.todolist.models.Priority;

public class AddEditFragment extends Fragment {
    private EditText taskNameInput ;
    private FloatingActionButton addTaskFabButton ;
    private TaskViewModel mTaskViewModel ;

    private Spinner priorityChoiceSpinner;

    public AddEditFragment() {
        // Required empty public constructor
    }

    public static AddEditFragment newInstance() {
        AddEditFragment fragment = new AddEditFragment();
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
        return inflater.inflate(R.layout.fragment_add_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTaskViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(TaskViewModel.class);

        taskNameInput = view.findViewById(R.id.add_edit_fragment_task_name_input);
        addTaskFabButton = view.findViewById(R.id.add_edit_fragment_fab_button_add_task);
        priorityChoiceSpinner = view.findViewById(R.id.add_edit_fragment_priority_choice_spinner);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(requireContext(),R.array.priority_choices,android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priorityChoiceSpinner.setAdapter(spinnerAdapter);


        addTaskFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskName = taskNameInput.getText().toString();
                String taskPriority = priorityChoiceSpinner.getSelectedItem().toString();
                Task task = new Task(taskName);
                task.setPriority(PriorityConverter.fromStringToPriority(taskPriority));
                mTaskViewModel.insert(task);

                NavController navController = Navigation.findNavController(requireActivity(),R.id.nav_host_fragment);
                navController.navigateUp();
            }
        });

    }


}