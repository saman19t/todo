package android.example.todoapp;

import android.app.Application;
import android.content.Context;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

public class TodoItemViewModel extends AndroidViewModel {
    private TodoItemRepo repo;
    private LiveData<List<TodoItem>> todoItemsList;

    public TodoItemViewModel(Application application) {
        super(application);
        repo = new TodoItemRepo(application);
        todoItemsList = repo.getAllItems();
    }

    public LiveData<List<TodoItem>> getTodoItems(Context context) {
        return todoItemsList;
    }

    public void insert(TodoItem item) {
        repo.insert(item);
    }

    public void update(TodoItem item) {
        repo.update(item);
    }

    public void delete(TodoItem item) {
        repo.delete(item);
    }
}
