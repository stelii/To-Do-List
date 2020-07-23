package my.projects.todolist.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ToolbarWidgetWrapper;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Date;

import my.projects.todolist.MainActivity;
import my.projects.todolist.R;
import my.projects.todolist.database.Task;
import my.projects.todolist.database.TaskViewModel;
import my.projects.todolist.database.converters.PriorityConverter;

import static android.content.ContentValues.TAG;

public class AddEditFragment extends Fragment {
    private EditText taskNameInput ;
    private ImageView datePicker, timePicker ;
    private EditText datePickerDisplay, timePickerDisplay ;

    private int mYear, mMonth, mDay , mHour, mMinute ;
    private Date dueDate = null;

    private DatePicker mDatePicker ;
    private TimePicker mTimePicker ;

//    private FloatingActionButton addTaskFabButton ;
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

//        ((MainActivity) getActivity()).switchToolbar(R.layout.toolbar_layout_add_edit_fragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //raporteaza faptul ca acest fragment vrea sa participe la popularea elementelor meniului de toolbar primind un apel la metodele care au legatura cu meniul
        //ex : onPrepareOptionsMenu(Menu menu)
        setHasOptionsMenu(true);
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
        timePicker = view.findViewById(R.id.add_edit_fragment_task_time_input);
        timePickerDisplay = view.findViewById(R.id.add_edit_fragment_task_time_display);

        timePicker.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                setTimePickerDialog();
            }
        });
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDatePickerDialog();
            }
        });
//        addTaskFabButton = view.findViewById(R.id.add_edit_fragment_fab_button_add_task);
        priorityChoiceSpinner = view.findViewById(R.id.add_edit_fragment_priority_choice_spinner);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(requireContext(),R.array.priority_choices,android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priorityChoiceSpinner.setAdapter(spinnerAdapter);

        assert getArguments() != null;
        if(getArguments().getInt("taskArg") != -1){
            AddEditFragmentArgs args = AddEditFragmentArgs.fromBundle(getArguments());
            int idPassed = args.getTaskArg();
            Task task = mTaskViewModel.getTask(idPassed);

            taskNameInput.setText(task.getName());
            priorityChoiceSpinner.setSelection(task.getPriority().getValue());
        }




        view.findViewById(R.id.add_edit_fragment_task_date_remove).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                datePickerDisplay.setText("");
                mDatePicker = null ;
            }
        });

        view.findViewById(R.id.add_edit_fragment_task_time_remove).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                timePickerDisplay.setText("");
                mTimePicker = null ;
            }
        });

    }

    private void saveItem(){
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
            task.setDate(getDateFromPickers(mDatePicker,mTimePicker));
            mTaskViewModel.update(task);
        }else{
            Task task = new Task(taskName,PriorityConverter.fromStringToPriority(taskPriority));
            task.setDate(getDateFromPickers(mDatePicker,mTimePicker));
//                    Task task = new Task.TaskBuilder()
//                            .setName(taskName)
//                            .setPriority(PriorityConverter.fromStringToPriority(taskPriority))
//                            .createTask();
            mTaskViewModel.insert(task);
        }
        NavController navController = Navigation.findNavController(requireActivity(),R.id.nav_host_fragment);
        navController.navigate(R.id.action_addEditFragment_to_homeFragment);
        hideKeyboard(getView());

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
                mDatePicker = view ;

            }
        },mYear,mMonth,mDay);

        datePickerDialog.show();
    }

    private void setTimePickerDialog(){
        final Calendar calendar = Calendar.getInstance();

        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);

        //launch timepicker dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                timePickerDisplay.setText(hourOfDay + ":" + minute);
                mTimePicker = view ;

            }
        },mHour,mMinute,true);

        timePickerDialog.show();
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        menu.findItem(R.id.toolbar_menu_delete_all).setVisible(false);
        menu.findItem(R.id.toolbar_menu_search_button).setVisible(false);
        menu.findItem(R.id.toolbar_menu_save_button).setVisible(true);
    }

    private Date getDateFromPickers(DatePicker datePicker, TimePicker timePicker){
        Calendar calendar = Calendar.getInstance();

        int day;
        int month;
        int year;
        int hour;
        int minute;

        if(datePicker != null){
             day = datePicker.getDayOfMonth();
             month = datePicker.getMonth();
             year = datePicker.getYear();

             calendar.set(year,month,day);
        }
        if(timePicker != null){
            hour = timePicker.getHour();
            minute = timePicker.getMinute();
            calendar.set(Calendar.HOUR_OF_DAY,hour);
            calendar.set(Calendar.MINUTE,minute);
        }
        return calendar.getTime();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.toolbar_menu_save_button :
                saveItem();
                return true ;
            default : return super.onOptionsItemSelected(item);

        }
    }

    private void hideKeyboard(View view){
        if(view != null){
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }


}