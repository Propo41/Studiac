package com.example.project.utility.todo;

import java.util.ArrayList;

public class SelfStudy {

    private ArrayList<Task> mTodoTasks;

    public ArrayList<Task> getTodoTasks() {
        return mTodoTasks;
    }

    public void setTodoTasks(ArrayList<Task> todoTasks) {
        mTodoTasks = todoTasks;
    }

    public SelfStudy(ArrayList<Task> todoTasks) {
        mTodoTasks = todoTasks;
    }
}
