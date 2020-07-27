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
import android.widget.Button;
import android.widget.EditText;

import my.projects.todolist.R;
import my.projects.todolist.database.TaskViewModel;

import static my.projects.todolist.MainActivity.TAG;


public class ListNameDialogFragment extends DialogFragment {
    private EditText mListNameInput ;
    private TaskViewModel mTaskViewModel ;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mTaskViewModel = new ViewModelProvider(getActivity()).get(TaskViewModel.class);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_name_dialog, container, false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
       View view = getLayoutInflater().inflate(R.layout.fragment_list_name_dialog,null);

       final EditText listNameInput = view.findViewById(R.id.dialog_fragment_name_of_list_name_input);

        return new AlertDialog.Builder(requireContext())
                .setTitle("New list???")
                .setView(view)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "onClick: " + listNameInput.getText().toString());
                        mTaskViewModel.setListName(listNameInput.getText().toString());
                    }
                })
                .create();
    }

}