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
import androidx.navigation.NavGraph;
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

import java.util.ArrayList;
import java.util.List;

import my.projects.todolist.R;
import my.projects.todolist.adapters.TaskAdapter;
import my.projects.todolist.database.Task;
import my.projects.todolist.database.TaskViewModel;
import my.projects.todolist.models.Priority;

import static android.content.ContentValues.TAG;

public class HomeFragment extends Fragment implements TaskAdapter.OnCheckboxListener, TaskAdapter.OnItemClickListener {
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
        mTaskAdapter.setOnItemClickListener(this);

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





//                mTaskViewModel.delete(taskDeleted);




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

        mTaskViewModel.update(task);
        return task.isDone();
    }

    @Override
    public void onItemShortClick(Task task) {
        int idToPass = task.getId();
        HomeFragmentDirections.ActionHomeFragmentToAddEditFragment action = HomeFragmentDirections.actionHomeFragmentToAddEditFragment();
        action.setTaskArg(idToPass);

        NavController navController = Navigation.findNavController(requireActivity(),R.id.nav_host_fragment);
        navController.navigate(action);
    }
}