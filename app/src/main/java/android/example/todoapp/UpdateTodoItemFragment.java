package android.example.todoapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class UpdateTodoItemFragment extends DialogFragment {
    private EditText editItemTitle;
    private TodoItem item;

    public UpdateTodoItemFragment() {
    }

    public static UpdateTodoItemFragment newInstance(String title, TodoItem todoItem) {
        UpdateTodoItemFragment frag = new UpdateTodoItemFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        frag.setTodoItem(todoItem);
        frag.setRetainInstance(true);
        return frag;
    }

    public void setTodoItem(TodoItem item) {
        this.item = item;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_update_todo_item, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editItemTitle = view.findViewById(R.id.updateTextTitle);
        loadItem(item);
        editItemTitle.requestFocus();
        Button updateButton = view.findViewById(R.id.updateButton);
        ImageView closeBtn = view.findViewById(R.id.closeBtn);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateItem(item);
                dismiss();
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        Dialog dialog = getDialog();
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }


    private void loadItem(TodoItem todoItem) {
        editItemTitle.setText(todoItem.getItem());
    }

    private void updateItem(final TodoItem todoItem) {
        final String itemText = editItemTitle.getText().toString().trim();

        if (itemText.isEmpty()) {
            editItemTitle.setError("Item is required!");
            editItemTitle.requestFocus();
            return;
        }

        todoItem.setItem(itemText);
        TodoItemViewModel todoItemViewModel = new ViewModelProvider(this).get(TodoItemViewModel.class);
        todoItemViewModel.update(todoItem);
    }
}