package my.projects.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ToggleButton;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private NavigationView navView;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.main_activity_drawer_layout);
        navView = findViewById(R.id.main_activity_nav_view);

        setUpToolbar();

        handleNavViewMenu();
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_navigation_drawer,R.string.close_navigation_drawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void handleNavViewMenu(){
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_view_menu_home :
                        navigateToHomeFragment();
                        return true ;
                    default: return false ;
                }
            }
        });
    }

    private void navigateToHomeFragment(){
        NavController navController = Navigation.findNavController(this,R.id.nav_host_fragment);
        navController.popBackStack(R.id.homeFragment,false);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(navView)) drawerLayout.closeDrawer(navView);
        else super.onBackPressed();
    }

    //    private void navigateConditionally(){
//        NavController navController = Navigation.findNavController(this,R.id.nav_host_fragment);
//        NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.nav_graph);
//
//        navGraph.setStartDestination(R.id.noListsFragment);
//        navController.setGraph(navGraph);
//    }
}