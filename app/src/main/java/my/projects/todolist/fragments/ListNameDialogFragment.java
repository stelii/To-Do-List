package my.projects.todolist.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import my.projects.todolist.R;
import my.projects.todolist.database.TaskViewModel;

import static my.projects.todolist.MainActivity.TAG;


public class ListNameDialogFragment extends DialogFragment {
    private EditText mListNameInput ;
    private TaskViewModel mTaskViewModel ;
    private EditText listNameInput ;
    private View view  ;

    public ListNameDialogFragment() {
        // Required empty public constructor
    }


    public static ListNameDialogFragment newInstance(String param1, String param2) {
        ListNameDialogFragment fragment = new ListNameDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTaskViewModel = new ViewModelProvider(getActivity()).get(TaskViewModel.class);
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        mTaskViewModel = new ViewModelProvider(getActivity()).get(TaskViewModel.class);
//        // Inflate the layout for this fragment
//        view =  inflater.inflate(R.layout.fragment_list_name_dialog, container, false);
//        listNameInput = view.findViewById(R.id.dialog_fragment_name_of_list_name_input);
//        listNameInput.requestFocus();
//        return view ;
//    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    //   View view = getLayoutInflater().inflate(R.layout.fragment_list_name_dialog,null);

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.fragment_list_name_dialog,null);
        listNameInput = view.findViewById(R.id.dialog_fragment_name_of_list_name_input);

        AlertDialog.Builder alertAdialog = new AlertDialog.Builder(requireActivity())
                .setView(view)
                .setMessage("Message")
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTaskViewModel.setListName(listNameInput.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return alertAdialog.create();

    }

}