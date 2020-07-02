package com.valhalla.studiac.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.valhalla.studiac.R;
import com.valhalla.studiac.adapters.CustomDeserializer;
import com.valhalla.studiac.database.Firebase;
import com.valhalla.studiac.fragments.todo.CurrentTasksFragment;
import com.valhalla.studiac.fragments.todo.CurrentWeekFragment;
import com.valhalla.studiac.fragments.todo.UpcomingFragment;
import com.valhalla.studiac.holders.HeaderItem;
import com.valhalla.studiac.holders.ListItem;
import com.valhalla.studiac.models.Course;

import com.valhalla.studiac.models.Task;
import com.valhalla.studiac.models.TasksUtil;
import com.valhalla.studiac.models.TodoTasks;
import com.valhalla.studiac.toolbars.NavigationToolbarWhite;
import com.valhalla.studiac.utility.Common;
import com.valhalla.studiac.utility.CustomPair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class TodoTaskActivity extends NavigationToolbarWhite {

    private int mCurrentSelectedItemBottomNav;
    private ArrayList<Course> mCourses;
    private TodoTasks mTodoTasks;
    private DatabaseReference mDatabaseReference;
    private FirebaseUser mUser;
    private final String TAG = "FIREBASE";
    private final String FILENAME_TODO_TASKS = "todoTasks";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContent(R.layout.activity_todotask);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (isUserAuthenticated(mUser)) {
            //  deleteFile();
            super.startLoadingAnimation();
            importCourses(); // after importing courses, load tasks from file and setup bottom sheet dialog
        }
    }

    private void deleteFile() {
        File dir = getFilesDir();
        File file = new File(dir, FILENAME_TODO_TASKS + mUser.getUid());
        boolean deleted = file.delete();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // overriding it here to set the Sync option visible
        getMenuInflater().inflate(R.menu.options_menu_todo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        mDatabaseReference = Firebase.getDatabaseReference().
                child(Firebase.USERS).
                child(mUser.getUid()).
                child(Firebase.TODO);

        if (item.getItemId() == R.id.more_sync_id) {
            // sync button clicked
            Toast.makeText(this, "Syncing...", Toast.LENGTH_SHORT).show();
            // [START] sync_with_database
            syncCurrentTasks();
            syncCurrentWeek();
            syncUpcoming();
            // [END] sync_with_database
            return true;
        } else if (item.getItemId() == R.id.more_delete_id) {
            mTodoTasks = new TodoTasks("deleteAll");
            // deleting all tasks from database as well
            mDatabaseReference.setValue(mTodoTasks);
            if (mCurrentSelectedItemBottomNav == R.id.nav_btm_current) {
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.todo_fragment_container);
                if (currentFragment instanceof CurrentTasksFragment) {
                    FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();
                    fragTransaction.detach(currentFragment);
                    fragTransaction.attach(currentFragment);
                    fragTransaction.commit();
                }
            }
            Toast.makeText(this, "All Tasks Deleted!", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void syncCurrentTasks() {
        mDatabaseReference.
                child(Firebase.TODO_CURRENT_TASKS).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<ListItem> imported = parseData(dataSnapshot);
                        // if size of imported content is greater than local content, append to the
                        // local list starting from index: size of local content

                        // [START] sync_start
                        int localLength = mTodoTasks.getCurrentTasks().size();
                        if (imported.size() > localLength) {
                            mTodoTasks.getCurrentTasks().addAll(localLength, imported.subList(localLength, imported.size()));
                        }
                        // [END] sync_start

                        //upload the new data to the database
                        mDatabaseReference.child(Firebase.TODO_CURRENT_TASKS).setValue(mTodoTasks.getCurrentTasks()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // iff the currently selected tab is CurrentTasks, refresh the fragment
                                // by detaching the fragment and reattaching it
                                if (mCurrentSelectedItemBottomNav == R.id.nav_btm_current) {
                                    Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.todo_fragment_container);
                                    if (currentFragment instanceof CurrentTasksFragment) {
                                        FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();
                                        fragTransaction.detach(currentFragment);
                                        fragTransaction.attach(currentFragment);
                                        fragTransaction.commit();
                                    }
                                    Toast.makeText(TodoTaskActivity.this, "Sync Complete", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, e.getMessage());
                                Toast.makeText(TodoTaskActivity.this, "Sync Failed", Toast.LENGTH_SHORT).show();

                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(TAG, databaseError.getDetails());
                        Toast.makeText(TodoTaskActivity.this, "Sync Failed", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void syncCurrentWeek() {
        mDatabaseReference.
                child(Firebase.TODO_CURRENT_WEEK).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<TasksUtil> importedCurrentWeek = new ArrayList<>();
                        for (DataSnapshot index : dataSnapshot.getChildren()) {
                            // imports the taskUtil instance at the given index
                            TasksUtil importedTaskUtil = new TasksUtil();
                            for (DataSnapshot data : index.getChildren()) {
                                if (data.getKey().equals("name")) {
                                    importedTaskUtil.setName(data.getValue(String.class));
                                } else if (data.getKey().equals("visited")) {
                                    GenericTypeIndicator<HashMap<String, CustomPair>> t = new GenericTypeIndicator<HashMap<String, CustomPair>>() {
                                    };
                                    importedTaskUtil.setVisited(data.getValue(t));
                                } else {
                                    ArrayList<ListItem> importedTodoTasks = parseData(data);
                                    importedTaskUtil.setTodoTasks(importedTodoTasks);
                                }
                            }

                            importedCurrentWeek.add(importedTaskUtil);

                        }

                        //[START] sync_start
                        int i = 0;
                        ArrayList<ListItem> importedTodoTasks;
                        for (TasksUtil tasksUtil : importedCurrentWeek) {
                            ArrayList<ListItem> localTodoTasks = mTodoTasks.getCurrentWeek().get(i).getTodoTasks();
                            int localLength = localTodoTasks.size();
                            // if size of imported content is greater than local content, append to the
                            // local list starting from index: size of local content
                            // [START] sync_start
                            importedTodoTasks = tasksUtil.getTodoTasks();
                            // importedTodoTasks is null only when there are no tasks added
                            // *only firebase makes objects null when they have no value assigned.
                            if (importedTodoTasks != null) {
                                if (importedTodoTasks.size() > localLength) {
                                    // sync the list of todoTasks
                                    localTodoTasks.addAll(localLength,
                                            importedTodoTasks.subList(localLength, importedTodoTasks.size()));

                                    // sync the visited hashMap
                                    mTodoTasks.getCurrentWeek().get(i).setVisited(syncTaskUtil(mTodoTasks.getCurrentWeek().get(i).getTodoTasks()));
                                }
                            }

                            i++;
                        }
                        // [END] sync_start

                        // upload it to db
                        mDatabaseReference.child(Firebase.TODO_CURRENT_WEEK).setValue(mTodoTasks.getCurrentWeek()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // iff the currently selected tab is CurrentTasks, refresh the fragment
                                // by detaching the fragment and reattaching it
                                if (mCurrentSelectedItemBottomNav == R.id.nav_btm_week) {
                                    Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.todo_fragment_container);
                                    if (currentFragment instanceof UpcomingFragment) {
                                        FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();
                                        fragTransaction.detach(currentFragment);
                                        fragTransaction.attach(currentFragment);
                                        fragTransaction.commit();
                                    }
                                    Toast.makeText(TodoTaskActivity.this, "Sync Complete", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                e.printStackTrace();
                                Toast.makeText(TodoTaskActivity.this, "Sync Failed", Toast.LENGTH_SHORT).show();

                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(TAG, databaseError.getMessage());
                        Toast.makeText(TodoTaskActivity.this, "Sync Failed", Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private void syncUpcoming() {
        mDatabaseReference.
                child(Firebase.TODO_UPCOMING).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        TasksUtil importedUpcoming = new TasksUtil();

                        ArrayList<ListItem> importedTodoTasks = new ArrayList<>();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            if (Objects.equals(data.getKey(), "name")) {
                                importedUpcoming.setName(data.getValue(String.class));
                            } else if (data.getKey().equals("visited")) {
                                GenericTypeIndicator<HashMap<String, CustomPair>> t = new GenericTypeIndicator<HashMap<String, CustomPair>>() {
                                };
                                importedUpcoming.setVisited(data.getValue(t));
                            } else {
                                importedTodoTasks = parseData(data);
                                importedUpcoming.setTodoTasks(importedTodoTasks);
                            }
                        }


                        // [START] sync_start
                        ArrayList<ListItem> localTodoTasks = mTodoTasks.getUpcoming().getTodoTasks();
                        int localLength = localTodoTasks.size();
                        // if size of imported content is greater than local content, append to the
                        // local list starting from index: size of local content
                        // [START] sync_start
                        if (importedTodoTasks.size() > localLength) {
                            // sync the list of todoTasks
                            localTodoTasks.addAll(localLength,
                                    importedTodoTasks.subList(localLength, importedTodoTasks.size()));

                            mTodoTasks.getUpcoming().setVisited(syncTaskUtil(mTodoTasks.getUpcoming().getTodoTasks()));

                        }
                        // [END] sync_start

                        mDatabaseReference.child(Firebase.TODO_UPCOMING).setValue(mTodoTasks.getUpcoming()).
                                addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // iff the currently selected tab is CurrentTasks, refresh the fragment
                                        // by detaching the fragment and reattaching it
                                        if (mCurrentSelectedItemBottomNav == R.id.nav_btm_upcoming) {
                                            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.todo_fragment_container);
                                            if (currentFragment instanceof UpcomingFragment) {
                                                FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();
                                                fragTransaction.detach(currentFragment);
                                                fragTransaction.attach(currentFragment);
                                                fragTransaction.commit();
                                            }
                                            Toast.makeText(TodoTaskActivity.this, "Sync Complete", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, e.getMessage());
                                Toast.makeText(TodoTaskActivity.this, "Sync Failed", Toast.LENGTH_SHORT).show();

                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(TAG, databaseError.getDetails());
                        Toast.makeText(TodoTaskActivity.this, "Sync Failed", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    /**
     * iterates through the given list and finds the number of headers and the number of
     * task items associated with each header and compiles them into an instance of HashMap
     *
     * @param: ArrayList<ListItem> A List of ListItem containing HeaderItems and TaskItems
     * @returns: HashMap<String, CustomPair>
     */
    private HashMap<String, CustomPair> syncTaskUtil(ArrayList<ListItem> listItems) {
        // loop through the list, and update the values of mVisited
        HashMap<String, CustomPair> visited = new HashMap<>();
        int count = 0;
        int headerIndex = 0;
        String header = "";
        boolean found = false;
        // sync the visited hash map
        for (int i = 0; i < listItems.size(); i++) {
            // if its a header, track down the header name, its index
            // else, count the number of task items it has.
            if (listItems.get(i) instanceof HeaderItem) {
                if (found) {
                    visited.put(header, new CustomPair(headerIndex, count));
                    //resetting the values
                    count = 0;
                    headerIndex = count + headerIndex + 1;
                    header = ((HeaderItem) listItems.get(i)).getHeader();
                } else {
                    header = ((HeaderItem) listItems.get(i)).getHeader();
                    headerIndex = i;
                    found = true;
                }
            } else {
                count++;
            }
        }
        // adding the last header to the list
        visited.put(header, new CustomPair(headerIndex, count));

        return visited;
    }


    /**
     * retrieves polymorphic class ListItem by manually checking their types
     * and adds them to the list depending on their class type
     */
    private ArrayList<ListItem> parseData(DataSnapshot dataSnapshot) {
        ArrayList<ListItem> list = new ArrayList<>();

        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
            ListItem listItem = dataSnapshot1.getValue(ListItem.class);
            assert listItem != null;
            if (listItem.getTaskType().equals("HeaderItem")) {
                list.add(dataSnapshot1.getValue(HeaderItem.class));
            } else {
                list.add(dataSnapshot1.getValue(Task.class));
            }
        }

        return list;
    }


    /*
     * saves the instance of TodoTasks to the local storage with filename FILENAME_TODO_TASKS + UID
     * @param todoTasks the instance of TodoTasks
     * @returns: an instance of TodoTasks that's saved in the local storage with file name FILENAME_TODO_TASKS
     */
    private void saveTodoTasks(TodoTasks todoTasks, String uid) {
        String json;
        String fileName;

        Gson gson = new Gson();
        json = gson.toJson(todoTasks);
        System.out.println("saved: " + json);
        fileName = FILENAME_TODO_TASKS + uid;
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = openFileOutput(fileName, MODE_PRIVATE);
            fileOutputStream.write(json.getBytes());
            System.out.println("file dir: " + getFilesDir() + "/");
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public boolean isFileExists(String filename) {
        File file = getFileStreamPath(filename);
        if (file == null || !file.exists()) {
            return false;
        }
        return true;
    }

    /*
     * loads the instance of TodoTasks from local storage
     * @returns: an instance of TodoTasks that's saved in the local storage with file name FILENAME_TODO_TASKS
     */
    private TodoTasks loadTodoTasks(String uid) {
        if (isFileExists(FILENAME_TODO_TASKS + uid)) {
            FileInputStream fileInputStream;
            try {
                // open file with the given filename
                fileInputStream = openFileInput(FILENAME_TODO_TASKS + uid);

                // import the texts from the file and convert it to a string
                // [PARSE] START
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder();
                String text;
                while ((text = bufferedReader.readLine()) != null) {
                    stringBuilder.append(text);
                }
                String json = stringBuilder.toString();
                // [PARSE] END
                System.out.println("load: " + json);


                // using jsonObject to individually access the member variables in the json string
                JSONObject jsonObject = new JSONObject(json);

                String currentJson = jsonObject.getJSONArray("mCurrentTasks").toString();
                String weekJson = jsonObject.getJSONArray("mCurrentWeek").toString();
                String upcomingJson = jsonObject.getJSONObject("mUpcoming").toString();

                ArrayList<ListItem> current = parseListOfTasksJson(currentJson);
                ArrayList<TasksUtil> week = parseWeekJson(weekJson);
                TasksUtil upcoming = parseUpcomingJson(upcomingJson);

                Toast.makeText(this, "Load Success", Toast.LENGTH_SHORT).show();

                return new TodoTasks(current, week, upcoming);

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return new TodoTasks("init");

        }


    }

    /*
     * converts json to a list of ListItems using a custom deserializer
     */
    private ArrayList<ListItem> parseListOfTasksJson(String json) {

        // de-serializing using the custom deserializer
        CustomDeserializer deserializer = new CustomDeserializer("type");
        deserializer.registerBarnType("Task", Task.class);
        deserializer.registerBarnType("HeaderItem", HeaderItem.class);
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ListItem.class, deserializer)
                .create();

     /*
     @bug: using this method of de-serializing causes a bug. Some of the Task items, changes their task type
      to ListItem.
        // [START] load_list
        Type typeList = new TypeToken<ArrayList<ListItem>>() {
        }.getType();
        // custom deserialization using RuntimeTypeAdapterFactory class
        RuntimeTypeAdapterFactory<ListItem> adapter = RuntimeTypeAdapterFactory.of(ListItem.class, "type")
                .registerSubtype(HeaderItem.class)
                .registerSubtype(Task.class);
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(adapter).create();
        // [END] load_list*/

        return gson.fromJson(json, new TypeToken<ArrayList<ListItem>>() {
        }.getType());

    }

    private ArrayList<TasksUtil> parseWeekJson(String json) throws JSONException {
        //Since, we need to load ArrayList<TaskUtil>, we are using a JSONArray
        JSONArray jsonArray = new JSONArray(json);
        ArrayList<TasksUtil> tasksUtils = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            // creating instance of json object for the particular index.
            // each index contains an instance of TaskUtil
            JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
            // using jsonObject to individually access the member variables in the json string
            String nameJson = jsonObject.getString("mName");
            String listJson = jsonObject.getJSONArray("mTodoTasks").toString();
            String mapJson = jsonObject.getJSONObject("mVisited").toString();

            TasksUtil parsedObj = deserializeItems(nameJson, listJson, mapJson);
            tasksUtils.add(parsedObj);
            // System.out.println(parsedObj);

        }
        return tasksUtils;

    }

    private TasksUtil parseUpcomingJson(String json) throws JSONException {
        // using jsonObject to individually access the member variables in the json string
        JSONObject jsonObject = new JSONObject(json);

        String nameJson = jsonObject.getString("mName");
        String listJson = jsonObject.getJSONArray("mTodoTasks").toString();
        String mapJson = jsonObject.getJSONObject("mVisited").toString();
        return deserializeItems(nameJson, listJson, mapJson);

    }

    @Override
    protected void onPause() {
        super.onPause();
        saveTodoTasks(mTodoTasks, mUser.getUid());
        Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show();
    }

    /*
    // for debug
    private ArrayList<ListItem> getList() {
        ArrayList<ListItem> tasks = new ArrayList<>();
        tasks.add(new HeaderItem("header"));
        tasks.add(new Task("des1", "add1"));
        tasks.add(new Task("des2", "add2"));
        tasks.add(new Task("des3", "add3"));
        tasks.add(new Task("des4", "add4"));
        tasks.add(new Task("des5", "add5"));
        tasks.add(new Task("des6", "add6"));

        return tasks;
    }

    // for debug
    private ArrayList<TasksUtil> getListTaskUtil() {
        ArrayList<TasksUtil> listTasksUtil = new ArrayList<>();
     *//*   listTasksUtil.add(getTaskUtil("Sun"));
        listTasksUtil.add(getTaskUtil("Mon"));
        listTasksUtil.add(getTaskUtil("Tues"));
        listTasksUtil.add(new TasksUtil("Wed"));*//*
        listTasksUtil.add(new TasksUtil("sun"));
        listTasksUtil.add(new TasksUtil("mon"));
        listTasksUtil.add(new TasksUtil("tues"));

        return listTasksUtil;

    }

    // for debug
    private TasksUtil getTaskUtil() {

        TasksUtil tasksUtil = new TasksUtil("humpty");
        tasksUtil.getTodoTasks().add(new HeaderItem("header1"));
        tasksUtil.getTodoTasks().add(new Task("task1", "desc1"));
        tasksUtil.getTodoTasks().add(new HeaderItem("header2"));
        return tasksUtil;

    }*/

    /*
     * de-serializes the json strings passed and returns a taskUtil instance
     * the de-serialization is made using a custom deserializer in order to properly
     * de-serialize the json strings.
     * takes parameters list and visited which are json strings
     */
    private TasksUtil deserializeItems(String name, String list, String visited) {
        // deserialize the JSON strings
        Gson gson = new Gson();
        // [START] load_hash_map
        Type typeMap = new TypeToken<HashMap<String, CustomPair>>() {
        }.getType();
        HashMap<String, CustomPair> visitedMap = gson.fromJson(visited, typeMap);
        // [END] load_hash_map

        // [START] load_list
    /*    Type typeList = new TypeToken<ArrayList<ListItem>>() {
        }.getType();
        // custom deserialization using RuntimeTypeAdapterFactory class
        RuntimeTypeAdapterFactory<ListItem> adapter = RuntimeTypeAdapterFactory.of(ListItem.class, "type")
                .registerSubtype(HeaderItem.class)
                .registerSubtype(Task.class);
        gson = new GsonBuilder().registerTypeAdapterFactory(adapter).create();
        ArrayList<ListItem> listItems = gson.fromJson(list, typeList);*/
        ArrayList<ListItem> listItems = parseListOfTasksJson(list);
        // [END] load_list

        return new TasksUtil(name, listItems, visitedMap);

    }


    /*
     * imports the list of courses from db.
     * asynchronous method
     * after importing the courses, tasks are imported from file and bottom nav is set up
     *
     */
    private void importCourses() {
        Firebase.getDatabaseReference().
                child(Firebase.USERS).
                child(mUser.getUid()).
                child(Common.COURSES).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mCourses = new ArrayList<>();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            mCourses.add(data.getValue(Course.class));
                        }

                        //  loads the instance of TodoTasks from local storage
                        mTodoTasks = loadTodoTasks(mUser.getUid());
                        TodoTaskActivity.super.stopLoadingAnimation();

                        setupBottomNav();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.i("DB ERROR", databaseError.getDetails());
                    }

                });
    }


    private void setupBottomNav() {

        // the default fragment that is open initially
        getSupportFragmentManager().beginTransaction().replace(R.id.todo_fragment_container,
                new CurrentTasksFragment(mTodoTasks.getCurrentTasks(), mCourses)).commit();

        mCurrentSelectedItemBottomNav = R.id.nav_btm_current;
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selected = new CurrentTasksFragment(mTodoTasks.getCurrentTasks(), mCourses);
                if (mCurrentSelectedItemBottomNav != item.getItemId()) {
                    switch (item.getItemId()) {
                        case R.id.nav_btm_current:
                            Log.println(Log.DEBUG, "todoActivity", "current tasks tab selected");
                            selected = new CurrentTasksFragment(mTodoTasks.getCurrentTasks(), mCourses);
                            mCurrentSelectedItemBottomNav = item.getItemId();
                            break;
                        case R.id.nav_btm_week:
                            Log.println(Log.DEBUG, "todoActivity", "current week tab selected");
                            selected = new CurrentWeekFragment(mTodoTasks.getCurrentWeek(), mTodoTasks.getCurrentTasks(), mCourses);
                            mCurrentSelectedItemBottomNav = item.getItemId();
                            break;
                        case R.id.nav_btm_upcoming:
                            Log.println(Log.DEBUG, "todoActivity", "upcoming tab selected");
                            selected = new UpcomingFragment(mTodoTasks.getUpcoming(), mTodoTasks.getCurrentTasks(), mCourses);
                            mCurrentSelectedItemBottomNav = item.getItemId();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.todo_fragment_container,
                            selected).commit();
                }

                return true;
            }
        });


    }

}
