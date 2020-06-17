package com.test.Bishal_todo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> implements
        Filterable {
    private List<Todo> todoList;
    private List<Todo> todoFilteredDataList;
    private ToDoViewModel toDoViewModel;
    private Context context;


    public TodoAdapter(List<Todo> todoList,
                       ToDoViewModel toDoViewModel) {
        this.todoList = todoList;
        this.toDoViewModel = toDoViewModel;
        this.todoFilteredDataList = todoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_pending, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Todo todo = todoFilteredDataList.get(position);
        holder.mTodo.setText("On " + todo.getDate() + "\n" + todo.getTodo());
        if (todo.getStatus().equals("Pending")) {
            holder.mCheckBox.setChecked(false);
        } else if (todo.getStatus().equals("Done")) {
            holder.mCheckBox.setChecked(true);
            holder.txt.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        }
        holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Todo todo1 = new Todo();
                todo1.setId(todo.getId());
                todo1.setTodo(todo.getTodo());
                todo1.setDate(todo.getDate());
                todo1.setPriority(todo.getPriority());
                todo1.setStatus("Done");
                toDoViewModel.insert(todo1);
                holder.txt.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                Toast.makeText(context, "Task completed", Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
            }
        });
        holder.mDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete todo")
                        .setMessage("Are you sure you want to delete this todo from the list?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                toDoViewModel.delete(todo);
                                Toast.makeText(context, "Todo item deleted", Toast.LENGTH_SHORT).show();
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            }
        });
        holder.mChip.setText(todo.getPriority());
        if (todo.getPriority().equals(context.getString(R.string.priority_low))) {
            holder.mChip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorLow)));
        } else if (todo.getPriority().equals(context.getString(R.string.priority_medium))) {
            holder.mChip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorMedium)));
        } else if (todo.getPriority().equals(context.getString(R.string.priority_high))) {
            holder.mChip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorHigh)));
        }
        holder.mEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateToDoActivity.class);
                intent.putExtra("todo", todo);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return todoFilteredDataList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString().toLowerCase();
                if (charString.isEmpty()) {
                    todoFilteredDataList = todoList;
                } else {
                    List<Todo> filteredList = new ArrayList<>();
                    for (Todo row : todoList) {
                        if (row.getTodo().toLowerCase().contains(charString) ||
                            row.getPriority().toLowerCase().contains(charString)) {
                            filteredList.add(row);
                        }
                    }
                    todoFilteredDataList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = todoFilteredDataList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                todoFilteredDataList = (ArrayList<Todo>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CheckBox mCheckBox;
        public TextView mTodo;
        public AppCompatImageButton mDeleteBtn, mEditBtn;
        public Chip mChip;
        public TextView txt;

        public ViewHolder(View itemView) {
            super(itemView);
            this.mCheckBox = itemView.findViewById(R.id.item_checkbox_pending);
            this.mTodo = itemView.findViewById(R.id.item_tv_todo);
            mDeleteBtn = itemView.findViewById(R.id.btn_delete);
            mEditBtn = itemView.findViewById(R.id.btn_edit);
            mChip = itemView.findViewById(R.id.chip_priority);
            txt=itemView.findViewById(R.id.item_tv_todo);
        }
    }

}