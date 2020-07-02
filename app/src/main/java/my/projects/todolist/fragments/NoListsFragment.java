package my.projects.todolist.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import my.projects.todolist.R;

public class NoListsFragment extends Fragment {


    public NoListsFragment() {
        // Required empty public constructor
    }


    public static NoListsFragment newInstance(String param1, String param2) {
        NoListsFragment fragment = new NoListsFragment();
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
        return inflater.inflate(R.layout.fragment_no_lists, container, false);
    }
}