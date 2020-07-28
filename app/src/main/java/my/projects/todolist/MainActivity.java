package my.projects.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

import my.projects.todolist.database.TaskViewModel;
import my.projects.todolist.database.TasksList;
import my.projects.todolist.fragments.HomeFragment;
import my.projects.todolist.fragments.ListNameDialogFragment;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "Main activity";

    private NavigationView navView;
    private DrawerLayout drawerLayout;
    private TaskViewModel taskViewModel;

    private Toolbar toolbar ;

    private Fragment mFragmentToSet ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);


        drawerLayout = findViewById(R.id.main_activity_drawer_layout);
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                if(mFragmentToSet != null){
                    navigateToHomeFragment();
                    mFragmentToSet = null ;
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        navView = findViewById(R.id.main_activity_nav_view);

        setUpToolbar();

        handleNavViewMenu();

        setUpNavViewItems();

        taskViewModel.getListName().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.d(TAG, "onChanged: " + "ceva ceva boss");
                taskViewModel.insertList(new TasksList(s));
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent){
        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);
        }
    }

    private void setUpToolbar() {
        toolbar = findViewById(R.id.toolbar);
//        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_navigation_drawer, R.string.close_navigation_drawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void setUpNavViewItems(){
        taskViewModel.getAllLists().observe(this, new Observer<List<TasksList>>() {
            @Override
            public void onChanged(List<TasksList> tasksLists) {
                for(TasksList t : tasksLists){
                    addMenuItemsNavMenuDrawer(t);
                }
            }
        });
    }


    //TODO : sterge din lista , actualizeaza din lista
    //TODO : adauga,sterge,actualizeaza din pagina de adaugare/editare
    //TODO : cand se intra in aplicatie, daca exista vreo lista creata, acea lista va fii afisata
    private void addMenuItemsNavMenuDrawer(final TasksList list){
        Menu menu = navView.getMenu();
        if(menu.findItem(list.getId()) == null){
            MenuItem menuItem = menu.add(R.id.nav_view_group_lists,list.getId(),5,list.getName());
            menuItem.setIcon(R.drawable.ic_round_format_list_bulleted_24);
            menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            drawerLayout.closeDrawer(navView);
                        }
                    },200);
                    //navigateToHomeFragment();
                    long listId = item.getItemId();
                    TasksList currentList = taskViewModel.getList(listId);
                    taskViewModel.setCurrentList(currentList);
                    toolbar.setTitle(currentList.getName());
                    return true ;
                }
            });
        }
    }

    private void handleNavViewMenu() {
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_view_menu_home:
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                drawerLayout.closeDrawer(navView);
                            }
                        },200);

                        navigateToHomeFragment();
                        return true;

                    case R.id.nav_view_menu_completed_tasks :
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                drawerLayout.closeDrawer(navView);
                            }
                        },200);
                            navigateToFragment(R.id.action_homeFragment_to_completedTasksFragment);
                            return true ;


                    case R.id.nav_view_menu_add_new_list :
                        //citesc numele listei dintr-un dialogFragment
                        navigateToDialogFragment();
//                        NavHostFragment navHostFragment = (NavHostFragment)getSupportFragmentManager().getPrimaryNavigationFragment();
//                        FragmentManager fragmentManager = navHostFragment.getChildFragmentManager();
//
//                        Fragment fragment = fragmentManager.getPrimaryNavigationFragment();
//
//                        if(fragment instanceof HomeFragment){
//                            ((HomeFragment) fragment).displayDialogFragment();
//                        }
                        return true ;

                    default:
                        return false;
                }
            }
        });
    }



    private void navigateToDialogFragment(){
        ListNameDialogFragment newListDialog = new ListNameDialogFragment();
        newListDialog.show(getSupportFragmentManager(),"new_list_dialog");
    }

    private void navigateToHomeFragment() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.popBackStack(R.id.homeFragment, false);
    }

    private void navigateToFragment(int actionId){
        NavController navController = Navigation.findNavController(this,R.id.nav_host_fragment);
        navController.navigate(actionId);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(navView)) drawerLayout.closeDrawer(navView);
        else super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_menu, menu);
        Log.d(TAG, "onCreateOptionsMenu: " + "from activity???");

        MenuItem menuItem = menu.findItem(R.id.toolbar_menu_search_button);
        SearchView searchView = (SearchView)menuItem.getActionView();
        searchView.setQueryHint("Search an item");

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        NavHostFragment navHostFragment = (NavHostFragment)getSupportFragmentManager().getPrimaryNavigationFragment();
        FragmentManager fragmentManager = navHostFragment.getChildFragmentManager();

        Fragment fragment = fragmentManager.getPrimaryNavigationFragment();
        if(fragment instanceof HomeFragment){
            fragment.onPrepareOptionsMenu(menu);
        }

        return false;
    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
////        menu.findItem(R.id.toolbar_menu_save_button).setVisible(false);
//        invalidateOptionsMenu();
//        return super.onPrepareOptionsMenu(menu);
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_menu_delete_all:
                taskViewModel.deleteAll();
                return false;

            default:
                return false;
        }
    }

    //    private void navigateConditionally(){
//        NavController navController = Navigation.findNavController(this,R.id.nav_host_fragment);
//        NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.nav_graph);
//
//        navGraph.setStartDestination(R.id.noListsFragment);
//        navController.setGraph(navGraph);
//    }
}