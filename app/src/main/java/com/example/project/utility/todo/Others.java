package com.example.project.utility.todo;

import java.util.ArrayList;

public class Others {
    private ArrayList<Task> mTodoTasks;

    public ArrayList<Task> getTodoTasks() {
        return mTodoTasks;
    }

    public void setTodoTasks(ArrayList<Task> todoTasks) {
        mTodoTasks = todoTasks;
    }

    public Others(ArrayList<Task> todoTasks) {
        mTodoTasks = todoTasks;
    }


}
