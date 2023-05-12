package android.example.todoapp;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.Callable;

public class TodoItemRepo {
    private Context context;
    private DAO todoItemDao;
    private LiveData<List<TodoItem>> todoItems;

    public TodoItemRepo(Application application) {
        AppDatabase database = DatabaseClient.getInstance(application).getAppDatabase();
        todoItemDao = database.todoItemDAO();
        todoItems = todoItemDao.getAll();
    }

    public void insert(TodoItem item) {
        new InsertNoteAsyncTask(todoItemDao).execute(item);
    }

    public void update(TodoItem item) {
        new UpdateNoteAsyncTask(todoItemDao).execute(item);
    }

    public void delete(TodoItem item) {
        new DeleteNoteAsyncTask(todoItemDao).execute(item);
    }

    public LiveData<List<TodoItem>> getAllItems() {
        return todoItems;
    }

    private static class InsertNoteAsyncTask extends AsyncTask<TodoItem, Void, Void> {
        private DAO todoItemDao;

        private InsertNoteAsyncTask(DAO todoItemDao) {
            this.todoItemDao = todoItemDao;
        }

        @Override
        protected Void doInBackground(TodoItem... todoItems) {
            todoItemDao.insert(todoItems[0]);
            return null;
        }
    }

    private static class UpdateNoteAsyncTask extends AsyncTask<TodoItem, Void, Void> {
        private DAO todoItemDao;

        private UpdateNoteAsyncTask(DAO todoItemDao) {
            this.todoItemDao = todoItemDao;
        }

        @Override
        protected Void doInBackground(TodoItem... todoItems) {
            todoItemDao.update(todoItems[0]);
            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<TodoItem, Void, Void> {
        private DAO todoItemDao;

        private DeleteNoteAsyncTask(DAO todoItemDao) {
            this.todoItemDao = todoItemDao;
        }

        @Override
        protected Void doInBackground(TodoItem... todoItems) {
            todoItemDao.delete(todoItems[0]);
            return null;
        }
    }
}
