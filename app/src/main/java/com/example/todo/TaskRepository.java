package com.example.todo;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
import android.os.AsyncTask;

public class TaskRepository {
    private TaskDao taskDao;
    private LiveData<List<Task>> allTasks;

    public TaskRepository(Application application) {
        TaskDatabase database = TaskDatabase.getInstance(application);
        taskDao = database.taskDao();
        allTasks = taskDao.getAllTasks();
    }

    public void insert(Task task) {
        new InsertTaskAsync(taskDao).execute(task);
    }

    public void update(Task task) {
        new UpdateTaskAsync(taskDao).execute(task);
    }

    public void delete(Task task) {
        new DeleteTaskAsync(taskDao).execute(task);
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    private static class InsertTaskAsync extends AsyncTask<Task, Void, Void> {
        private TaskDao taskDao;

        private InsertTaskAsync(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.insert(tasks[0]);
            return null;
        }
    }

    private static class UpdateTaskAsync extends AsyncTask<Task, Void, Void> {
        private TaskDao taskDao;

        private UpdateTaskAsync(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.update(tasks[0]);
            return null;
        }
    }

    private static class DeleteTaskAsync extends AsyncTask<Task, Void, Void> {
        private TaskDao taskDao;

        private DeleteTaskAsync(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.delete(tasks[0]);
            return null;
        }
    }
}

