package my.projects.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpToolbar();
    }

    private void setUpToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
//        toolbar.setTitle("");
        setSupportActionBar(toolbar);
    }

//    private void navigateConditionally(){
//        NavController navController = Navigation.findNavController(this,R.id.nav_host_fragment);
//        NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.nav_graph);
//
//        navGraph.setStartDestination(R.id.noListsFragment);
//        navController.setGraph(navGraph);
//    }
}