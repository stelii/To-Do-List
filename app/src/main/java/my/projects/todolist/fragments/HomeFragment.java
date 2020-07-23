package my.projects.todolist.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import my.projects.todolist.R;
import my.projects.todolist.adapters.TaskAdapter;
import my.projects.todolist.database.Task;
import my.projects.todolist.database.TaskViewModel;
import my.projects.todolist.database.converters.PriorityConverter;
import my.projects.todolist.models.Priority;

import static android.content.ContentValues.TAG;

public class HomeFragment extends Fragment implements TaskAdapter.OnCheckboxListener, TaskAdapter.OnItemClickListener {
    private TaskViewModel mTaskViewModel ;
    private TaskAdapter mTaskAdapter ;
    private RecyclerView mTaskList ;

    private FloatingActionButton mAddNewTaskFabBtn ;

    private EditText mQuickTaskName ;
    private ImageView mQuickAddBtn ;

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
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAddNewTaskFabBtn = view.findViewById(R.id.home_fragment_fab_button_add);
        mQuickTaskName = view.findViewById(R.id.home_fragment_task_name_quick_input);
        mQuickTaskName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(TAG, "beforeTextChanged: " + "???");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged: " + "???");
                mQuickAddBtn.setVisibility(View.VISIBLE);
                mAddNewTaskFabBtn.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "afterTextChanged: " + "???");
                if(s.toString().isEmpty()) {
                    mAddNewTaskFabBtn.setVisibility(View.VISIBLE);
                    mQuickAddBtn.setVisibility(View.INVISIBLE);
                }
            }
        });
        mQuickAddBtn =  view.findViewById(R.id.home_fragment_button_quick_add);
        mQuickAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskName = mQuickTaskName.getText().toString();
                mTaskViewModel.insert(new Task(taskName, PriorityConverter.fromStringToPriority("Low")));
                mQuickTaskName.setText("");
                hideKeyboard(v);

            }
        });

        mTaskAdapter = new TaskAdapter();
        mTaskList = view.findViewById(R.id.home_fragment_item_list_recyclerview);
        mTaskList.setLayoutManager(new LinearLayoutManager(getContext()));
        mTaskList.setAdapter(mTaskAdapter);

        mTaskAdapter.setCheckboxListener(this);
        mTaskAdapter.setOnItemClickListener(this);


        mTaskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
//        mTaskViewModel =
//                new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
//                        .getInstance(getActivity().getApplication())).get(TaskViewModel.class);

        mTaskViewModel.setFilter("");

        mTaskViewModel.getTasks().observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                mTaskAdapter.submitList(tasks);
            }
        });

        mAddNewTaskFabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireActivity(),R.id.nav_host_fragment);
                navController.navigate(R.id.action_homeFragment_to_addEditFragment);
            }
        });


        enableSwipeToDeleteAndUndo(mTaskList);

    }

    private static void hideKeyboard(View view){
        Context context = view.getContext();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
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
                //iau pozitia elementului care a fost inlaturat din lista
                final int adapterPosition = viewHolder.getAdapterPosition();
                //iau elementul de la pozitia respectiva
                final Task taskDeleted = mTaskAdapter.getItemAt(adapterPosition);

                //creez o lista temporara pentru a stoca toate elementele listei pana in acest moment
                final List<Task> temporaryTaks = new ArrayList<>(mTaskAdapter.getTasks());

                //creez un snackbar care primeste ca parametru recyclerview, mesajul si durata acestuia
                Snackbar snackbar = Snackbar.make(mTaskList,"ITEM REMOVED",Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            //setez o actiune in momentul in care se apasa "UNDO"
                            @Override
                            public void onClick(View v) {
                                //daca se apasa "UNDO", adica utilizatorul vrea ca acel element sa fie adaugat inapoi

                                //se adauga elementul la pozitia respectiva
                                temporaryTaks.add(adapterPosition,taskDeleted);
                                //notificam adaptorul ca s-a inserat un element pe pozitia "adapterPosition"
                                mTaskAdapter.notifyItemInserted(adapterPosition);

                                //facem ca recyclerview sa scroleze pana la pozitia respectiva
                                mTaskList.scrollToPosition(adapterPosition);


                            }
                        }).addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {

                            @Override
                            public void onDismissed(Snackbar transientBottomBar, int event) {
                                //metoda onDismissed este apelata in momentul in care bara de snackbar a fost respinsa

                                //daca bara nu a fost inalturata "in mod natural" (adica dupa ce s-a scurs durata acestuia)
                                //inseamna ca snackbarul NU a fost inalturat de catre utilizator
                                if(event != DISMISS_EVENT_ACTION){
                                    //DISMISS_EVENT_ACTION = denota faptul ca s-a petrecut un eveniment de tip click (adica click de la utlizator)
                                    Log.d(TAG, "onDismissed: " + "SnackBar not dismissed by click event");
                                    //inseamna ca snackbarul a fost inalturat fara ca utilizatorul sa fii apasat pe "UNDO"

                                    mTaskViewModel.delete(taskDeleted);
                                }
                            }
                        });
                //afisam snackbar
                snackbar.show();

                //lucrurile astea se intampla inainte ca snackbar sa apara
                //se sterge elementul de la pozitia "adapterPosition"
                temporaryTaks.remove(adapterPosition);
                //trimitem aceasta lista cu un element mai putin pentru ca recyclerview sa-si dea refresh
                mTaskAdapter.submitList(temporaryTaks);



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

        mTaskViewModel.update(task);
        return task.isDone();
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        Log.d(TAG, "onCreateOptionsMenu: " + "?>>>????");
        super.onCreateOptionsMenu(menu,inflater);
//        MenuItem menuItem = menu.findItem(R.id.toolbar_menu_search_button);
//        SearchView searchView = (SearchView)menuItem.getActionView();
//
//        SearchManager searchManager =
//                (SearchManager) requireActivity().getSystemService(Context.SEARCH_SERVICE);
//
//        searchView.setSearchableInfo(
//                searchManager.getSearchableInfo(requireActivity().getComponentName()));
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                mTaskAdapter.getFilter().filter(newText);
//                return true;
//            }
//        });

    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem menuItem = menu.findItem(R.id.toolbar_menu_search_button);
        SearchView searchView = (SearchView)menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "onQueryTextChange: " + ">???");
                mTaskViewModel.setFilter(newText);
//                mTaskViewModel.oMetoda();
                return true;
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: " + "hello from start");
    }

    //    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.toolbar_menu_search_button :
//                return true ;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onItemShortClick(Task task) {
        int idToPass = task.getId();
        HomeFragmentDirections.ActionHomeFragmentToAddEditFragment action = HomeFragmentDirections.actionHomeFragmentToAddEditFragment();
        action.setTaskArg(idToPass);

        NavController navController = Navigation.findNavController(requireActivity(),R.id.nav_host_fragment);
        navController.navigate(action);
    }
}