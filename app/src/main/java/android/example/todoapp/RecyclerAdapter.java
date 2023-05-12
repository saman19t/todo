package android.example.todoapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    Context context;
    List<TodoItem> todoItems;
    TodoItemViewModel viewModel;

    public RecyclerAdapter(Context context, List<TodoItem> todoItems, TodoItemViewModel model) {
        this.context = context;
        this.todoItems = todoItems;
        this.viewModel = model;
    }


    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.MyViewHolder holder, int position) {
        holder.todoItemCheckBox.setText(todoItems.get(position).getItem().toString());
        holder.todoItemCheckBox.setChecked(todoItems.get(position).getIsSelected());
        if (todoItems.get(position).getIsSelected())
            holder.todoItemCheckBox.setPaintFlags(holder.todoItemCheckBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        else
            holder.todoItemCheckBox.setPaintFlags(holder.todoItemCheckBox.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);

    }

    @Override
    public int getItemCount() {
        if (todoItems != null) {
            return todoItems.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        CheckBox todoItemCheckBox;

        public MyViewHolder(View itemView) {
            super(itemView);
            todoItemCheckBox = (CheckBox) itemView.findViewById(R.id.title);
            todoItemCheckBox.setClickable(false);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            TodoItem item = todoItems.get(getAdapterPosition());
            if (!item.getIsSelected())
                item.setIsSelected(true);
            else
                item.setIsSelected(false);
            viewModel.update(item);
        }

        @Override
        public boolean onLongClick(View view) {
            TodoItem item = todoItems.get(getAdapterPosition());
            FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
            UpdateTodoItemFragment updateTodoItemFragment = UpdateTodoItemFragment.newInstance("Update Todo Item", item);
            updateTodoItemFragment.show(fm, "update_todo_item");
            return true;
        }
    }


}
