package android.example.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private FloatingActionButton addTodoItemBtn;
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.todo_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ImageView openDrawerBtn = (ImageView) findViewById(R.id.drawerOpenBtn);
        openDrawerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer = findViewById(R.id.drawer_layout);
                drawer.open();
            }
        });

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        addTodoItemBtn = findViewById(R.id.add_btn);
        addTodoItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDialog();
            }
        });

        TodoItemViewModel model = new ViewModelProvider(this).get(TodoItemViewModel.class);
        LiveData<List<TodoItem>> todoItemList = model.getTodoItems(getApplicationContext());
        todoItemList.observe(this, todoItems -> {
            recyclerAdapter = new RecyclerAdapter(MainActivity.this, todoItems, model);
            recyclerView.setAdapter(recyclerAdapter);
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                TodoItem item = todoItemList.getValue().get(viewHolder.getAdapterPosition());
                TodoItemViewModel todoItemViewModel = new ViewModelProvider(MainActivity.this).get(TodoItemViewModel.class);
                todoItemViewModel.delete(item);
                Snackbar.make(recyclerView, item.getItem() + " removed!", Snackbar.LENGTH_LONG).show();
            }
        }).attachToRecyclerView(recyclerView);

    }

    private void showAddDialog() {
        FragmentManager fm = getSupportFragmentManager();
        AddTodoItemFragment addTodoItemFragment = AddTodoItemFragment.newInstance("Add Todo Item");
        addTodoItemFragment.show(fm, "add_todo_item");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "Hey check out my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        } else if (id == R.id.nav_contact) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 101);
                return false;
            }

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel: 9813647989"));
            startActivity(callIntent);
        }
        navigationView.getMenu().getItem(0).setChecked(true);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}