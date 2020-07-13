package my.projects.todolist.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Date;

import my.projects.todolist.R;
import my.projects.todolist.database.Task;
import my.projects.todolist.database.TaskViewModel;
import my.projects.todolist.database.converters.PriorityConverter;

import static android.content.ContentValues.TAG;

public class AddEditFragment extends Fragment {
    private EditText taskNameInput ;
    private ImageView datePicker ;
    private EditText datePickerDisplay ;

    private int mYear, mMonth, mDay , mHour, mMinute ;


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
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTaskViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(TaskViewModel.class);

        taskNameInput = view.findViewById(R.id.add_edit_fragment_task_name_input);
        datePicker = view.findViewById(R.id.add_edit_fragment_task_date_input);
        datePickerDisplay = view.findViewById(R.id.add_edit_fragment_task_date_display);
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDatePickerDialog();
            }
        });
        addTaskFabButton = view.findViewById(R.id.add_edit_fragment_fab_button_add_task);
        priorityChoiceSpinner = view.findViewById(R.id.add_edit_fragment_priority_choice_spinner);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(requireContext(),R.array.priority_choices,android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priorityChoiceSpinner.setAdapter(spinnerAdapter);

        assert getArguments() != null;
        if(getArguments().getInt("taskArg") != -1){
            Log.d(TAG, "onViewCreated: " + "ARGUMENTS: " + getArguments().toString());
            AddEditFragmentArgs args = AddEditFragmentArgs.fromBundle(getArguments());
            int idPassed = args.getTaskArg();
            Task task = mTaskViewModel.getTask(idPassed);

            taskNameInput.setText(task.getName());
            priorityChoiceSpinner.setSelection(task.getPriority().getValue());
        }


        addTaskFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(taskNameInput.getText().toString().isEmpty()){
                    Toast.makeText(requireContext(), "Please insert a name for this task", Toast.LENGTH_SHORT).show();
                    return;
                }
                String taskName = taskNameInput.getText().toString();
                String taskPriority = priorityChoiceSpinner.getSelectedItem().toString();

                if(getArguments().getInt("taskArg") != -1){
                    AddEditFragmentArgs args = AddEditFragmentArgs.fromBundle(getArguments());
                    Task task = mTaskViewModel.getTask(args.getTaskArg());

                    task.setName(taskName);
                    task.setPriority(PriorityConverter.fromStringToPriority(taskPriority));
                    mTaskViewModel.update(task);
                }else{
                    Task task = new Task(taskName,PriorityConverter.fromStringToPriority(taskPriority));
//                    Task task = new Task.TaskBuilder()
//                            .setName(taskName)
//                            .setPriority(PriorityConverter.fromStringToPriority(taskPriority))
//                            .createTask();
                    mTaskViewModel.insert(task);
                }
                hideKeyboard(view);
                NavController navController = Navigation.findNavController(requireActivity(),R.id.nav_host_fragment);
                navController.navigate(R.id.action_addEditFragment_to_homeFragment);
            }
        });

    }

    private void setDatePickerDialog(){
        final Calendar myCalendar = Calendar.getInstance();
        mYear = myCalendar.get(Calendar.YEAR);
        mMonth = myCalendar.get(Calendar.MONTH);
        mDay = myCalendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                datePickerDisplay.setText(dayOfMonth + "/" + (month+1) + "/" + year);
            }
        },mYear,mMonth,mDay);

        datePickerDialog.show();
    }

    private void hideKeyboard(View view){
        Context context = view.getContext();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
    }



}