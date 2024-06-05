package com.example.todo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Window;

public class MainActivity extends AppCompatActivity {
    private TaskViewModel taskViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final TaskAdapter adapter = new TaskAdapter();
        recyclerView.setAdapter(adapter);


//ViewModelProviders.of(this).get(TaskViewModel.class);
        taskViewModel =new ViewModelProvider(this).get(TaskViewModel.class);
        taskViewModel.getAllTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                adapter.setTasks(tasks);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle adding new task
                openDialog(null);
            }
        });
    }

    private void openDialog(final Task task) {
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_task, null);
        final EditText etTask = dialogView.findViewById(R.id.etTask);
        if (task != null) {
            etTask.setText(task.getTask());
        }

        new AlertDialog.Builder(this)
                .setTitle(task == null ? "Add Task" : "Edit Task")
                .setView(dialogView)
                .setPositiveButton(task == null ? "Add" : "Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String taskText = etTask.getText().toString().trim();
                        if (taskText.isEmpty()) {
                            Toast.makeText(MainActivity.this, "Task cannot be empty", Toast.LENGTH_SHORT).show();
                        } else {
                            if (task == null) {
                                taskViewModel.insert(new Task(taskText));
                            } else {
                                task.setTask(taskText);
                                taskViewModel.update(task);
                            }
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {
        private List<Task> tasks;

        @NonNull
        @Override
        public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo, parent, false);
            return new TaskHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
            Task task = tasks.get(position);
            holder.tvTask.setText(task.getTask());
            holder.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDialog(task);
                }
            });
            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    taskViewModel.delete(task);
                }
            });
        }

        @Override
        public int getItemCount() {
            return tasks != null ? tasks.size() : 0;
        }

        void setTasks(List<Task> tasks) {
            this.tasks = tasks;
            notifyDataSetChanged();
        }

        class TaskHolder extends RecyclerView.ViewHolder {
            private TextView tvTask;
            private ImageButton btnEdit, btnDelete;

            TaskHolder(@NonNull View itemView) {
                super(itemView);
                tvTask = itemView.findViewById(R.id.tvTask);
                btnEdit = itemView.findViewById(R.id.btnEdit);
                btnDelete = itemView.findViewById(R.id.btnDelete);
            }
        }
    }
}