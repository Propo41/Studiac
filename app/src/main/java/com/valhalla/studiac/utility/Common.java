package com.valhalla.studiac.utility;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.valhalla.studiac.R;
import com.valhalla.studiac.models.Student;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

/*
 * A utility class that can be used across the entire project
 */
public class Common {
    public static final String PACKAGE_NAME = "com.valhalla.studiac";
    public static final int COUNSELLING_TIME = 7;
    public static final String COURSES = "courses";
    public static final String ROUTINE = "routine";
    public static final String UNAVAILABLE = "null";
    public static final String POST_CATEGORY_1 = "Official";
    public static final String POST_CATEGORY_2 = "Help";
    public static final String POST_CATEGORY_3 = "Advertisement";
    public static final String POST_CATEGORY_4 = "Others";
    public static final String POST_CATEGORY_5 = "All";
    public static final String POST_TOPIC_1 = "Notice";
    public static final String POST_TOPIC_2 = "Seminar";
    public static final String POST_TOPIC_3 = "Job Opportunities";
    public static final String POST_TOPIC_4 = "Club Recruitment";
    public static final String POST_TOPIC_5 = "Workshop";
    public static final String POST_TOPIC_6 = "Giveaway";
    public static final String POST_TOPIC_7 = "Sale Post";
    public static final String POST_TOPIC_8 = "Find Roommates";
    public static final String POST_TOPIC_9 = "Lost Item";
    public static final String POST_TOPIC_10 = "Blood Donation";
    public static final String POST_TOPIC_11 = "Others";

    // drawables for the bulletin board: topic
    public static final int TOPIC_GIVEAWAY = R.drawable.bulletin_ic_giveaway;
    public static final int TOPIC_SALE_POST = R.drawable.bulletin_ic_sale_post;
    public static final int TOPIC_FIND_ROOMMATES = R.drawable.bulletin_ic_roommate;
    public static final int TOPIC_SEMINAR = R.drawable.bulletin_ic_seminar;
    public static final int TOPIC_CLUB_REC = R.drawable.bulletin_ic_club_recruitment;
    public static final int TOPIC_NOTICE = R.drawable.bulletin_ic_notice;
    public static final int TOPIC_WORKSHOPS = R.drawable.bulletin_ic_workshop;
    public static final int TOPIC_JOB_OPP = R.drawable.bulletin_ic_job_opportunities;
    public static final int TOPIC_LOST_ITEM = R.drawable.bulletin_ic_lost_items;
    public static final int TOPIC_BLOOD_DONA = R.drawable.bulletin_ic_blood_donation;
    public static final int TOPIC_OTHERS = R.drawable.bulletin_ic_others;

    public static final String COURSE_TYPE_1 = "Lecture";
    public static final String COURSE_TYPE_2 = "Practical";


    public static String[] GET_DAY_FROM_INDEX = {"Sun", "Mon", "Tues", "Wed", "Thurs", "Fri", "Sat"};
    public static String[] GET_DAY_FROM_INDEX_FULL = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

    private static String STUDENT = "STUDENT";
    public static String TODO = "todoTasks";
    public static HashMap<String, Integer> GET_INDEX_FROM_DAY = new HashMap<String, Integer>() {{
        put("Sunday", 0);
        put("Monday", 1);
        put("Tuesday", 2);
        put("Wednesday", 3);
        put("Thursday", 4);
        put("Friday", 5);
        put("Saturday", 6);

    }};

    public static HashMap<String, Integer> TASK_TAG_COLOURS = new HashMap<String, Integer>() {{
        put("Quiz", R.color.colorQuiz);
        put("Assignment", R.color.colorAssignment);
        put("Mid", R.color.colorMid);
        put("Final", R.color.colorFinal);
        put("Lab Quiz", R.color.colorLabQuiz);
        put("Lab Assignment", R.color.colorLabAssignment);
        put("Presentation", R.color.colorPresentation);
        put("Case Work", R.color.colorCaseWork);
        put("Project", R.color.colorProject);

    }};

    public static int STROKE_WIDTH = 5;
    public static String[] MONTHS = {"Jan", "Feb", "March", "April", "May", "June", "July", "August", "September", "October",
            "November", "December"};


    /*
     * parameters: view - the view on which the stroke is to be added
     *             width - the amount of width of the stroke. Typically: 5
     * typical usage: EditText editText = findViewById(....);
     *                Common.addStroke(editText, 5);
     */
    public static void addStroke(View view, int width) {

        GradientDrawable backgroundGradient = (GradientDrawable) view.getBackground();
        backgroundGradient.setStroke(width, ResourcesCompat.getColor(view.getResources(), R.color.colorInputRequired, null));
    }

    /**
     * @param oldDate a long representing time in ms. Note, long numbers have L at the end, ie 1592717400000L
     * @return a string representing the relative time, ie X min ago, June 10,2020, x days ago etc
     */
    public static String getRelativeTime(long oldDate) {
        Date currentDate = new Date();
        long currentDateLong = currentDate.getTime();
        CharSequence relativeTime = DateUtils
                .getRelativeTimeSpanString(oldDate, currentDateLong, 0L);
        return relativeTime.toString();

    }

    public static void showExceptionPrompt(Context context, String exceptionText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(exceptionText);
        builder.setCancelable(true);

        builder.setPositiveButton(
                "okay",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    public static void setBottomSheetDialogState(Dialog dialog, final String expanded) {

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;
                View bottomSheetInternal = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                if (bottomSheetInternal != null) {
                    if (expanded.equals("EXPANDED")) {
                        BottomSheetBehavior.from(bottomSheetInternal).setState(BottomSheetBehavior.STATE_EXPANDED);
                    } else if (expanded.equals("HALF_EXPANDED")) {
                        BottomSheetBehavior.from(bottomSheetInternal).setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                    }
                } else {
                    throw new NullPointerException("bottom sheet internal error");

                }


            }
        });

    }

/*
    // sets the margin of a particular view
    public static TableRow.LayoutParams setMargin(int layoutWidth, int layoutHeight, int start, int left, int top,
                                                  int right, int bottom) {

        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(layoutWidth, layoutHeight);
        layoutParams.setMarginStart(start);
        layoutParams.setMargins(left, top, right, bottom);
        return layoutParams;

    }*/

    /*   *//*
     * outputs a 24hr format in a proper way, filling in single digits with 0
     * takes input as 24hr format
     *//*
    public static String convertTo24hFormat(String time) {
        SimpleDateFormat timeFormat24 = new SimpleDateFormat("HH:mm");
        Date date = null;
        try {
            date = timeFormat24.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeFormat24.format(date);
    }
*/

    /**
     * returns a string in the 12hr format
     * takes in a time in 24hr format
     * output is in the format hh:mmAM
     */
    public static String convertTo12hFormat(String time) {
        SimpleDateFormat timeFormat24 = new SimpleDateFormat("HH:mm");
        SimpleDateFormat timeFormat12 = new SimpleDateFormat("hh:mma");
        Date date = null;
        try {
            date = timeFormat24.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.i("converted:::: ", timeFormat12.format(date));
        return timeFormat12.format(date);
    }


    /**
     * returns a string representation of the date instance in the format date.month.year
     *
     * @param date the Date instance
     * @return a string representation of the date in the format date.month.year, ie 03.12.20
     */
    public static String convertToDateFormat(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        return dateFormat.format(date);
    }

    /*
     */
    /*
     * used to sort a treeMap by virtue of its values instead of keys in ascending order
     *//*

    public static <K, V extends Comparable<V>> Map<K, V> sortByValues(final Map<K, V> map) {
        Comparator<K> valueComparator = new Comparator<K>() {
            public int compare(K k1, K k2) {
                int compare = map.get(k1).compareTo(map.get(k2));
                if (compare == 0)
                    return 1;
                else
                    return compare;
            }
        };

        Map<K, V> sortedByValues = new TreeMap<K, V>(valueComparator);
        sortedByValues.putAll(map);
        return sortedByValues;
    }
*/


    public static void saveData(Student student, Context context) {
        // instance of shared preferences
        String PROJECT_NAME = "Studiac";
        SharedPreferences sharedPreferences = context.getSharedPreferences(PROJECT_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(student);
        System.out.println(":::: Size of data ::::" + Common.getSizeOfData(json));
        editor.putString("student", json);
        editor.apply();
    }

   /* public static Student loadData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PROJECT_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("student", null);
        if (json == null) {
            return null;
        } else {
            return gson.fromJson(json, Student.class);
        }
    }*/


    /* *//*
     * saves to internal storage in a file.
     * if a student object is saved, it's renamed as: Studiac_STUDENT
     * if a to do task object is saved, it's renamed as: Studiac_TODO_studentEmail, where the studentEmail is used
     * as a tag to identify the student is belongs to in case the device is shared among more than 1 students
     *//*
    public static void saveToFile(Object object, String type, String tag, Context context) throws IOException {
        String json = "null";
        String fileName = "null";
        Gson gson = new Gson();

        if (type.equals(STUDENT)) {
            Student student = (Student) object;
            json = gson.toJson(student);
            fileName = PROJECT_NAME + "_" + type;
        } else if (type.equals(TODO)) {
            TodoTasks todoTasks = (TodoTasks) object;
            json = gson.toJson(todoTasks);
            fileName = PROJECT_NAME + "_" + type + "_" + tag;
        } else {
            System.out.println("invalid parameters. Check type");
        }

        FileOutputStream fileOutputStream = context.openFileOutput(fileName, MODE_PRIVATE);
        fileOutputStream.write(json.getBytes());
        System.out.println("file dir: " + context.getFilesDir() + "/" + PROJECT_NAME + "_ " + type);

        fileOutputStream.close();

    }*/


    /*  *//*
     * loads the required object from file. The tag is used for identifying the To Do Tasks object mapped to
     *  the student it belongs to
     *//*
    public static Object loadFromFile(String type, String tag, Context context) throws IOException {
        FileInputStream fileInputStream;
        if (type.equals(TODO)) {
            fileInputStream = context.openFileInput(PROJECT_NAME + "_" + type + "_" + tag);
        } else {
            fileInputStream = context.openFileInput(PROJECT_NAME + "_" + type);
        }

        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder stringBuilder = new StringBuilder();
        String text;

        while ((text = bufferedReader.readLine()) != null) {
            stringBuilder.append(text).append("\n");
        }

        String json = stringBuilder.toString();
        Gson gson = new Gson();

        if (type.equals(TODO)) {
            return gson.fromJson(json, TodoTasks.class);
        } else if (type.equals(STUDENT)) {
            return gson.fromJson(json, Student.class);
        }
        return null;
    }
*/

    public static long getSizeOfData(String json) {
        assert json != null;
        byte[] byteArray = json.getBytes(StandardCharsets.UTF_8);
        return byteArray.length;
    }


    /*
     * converts dpi to pixels
     * used when creating views programmatically, since the methods take pixels as arguments.
     */
    public static int getEquivalentPx(int dpi) {
        float d = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpi * d); // margin in pixels
    }


    /*
     * returns a string which corresponds to the month + year, for example: 11.2.20
     * will be converted into February 20
     */
    public static String parseDate(Object object) {
        String date = (String) object;
        String[] dateContents = date.split("-");
        for (String a : dateContents)
            Log.i("dateContents: ", a);
        return Common.MONTHS[Integer.parseInt(dateContents[1]) - 1] + " " + dateContents[2];
    }

    /*
     * returns the day of the week given a date in the format dd-mm-yyyy
     * throws ParseException if the input formats is wrong
     */
    public static String convertDateToDay(String date) throws ParseException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", java.util.Locale.ENGLISH);
        Date dateObj = simpleDateFormat.parse(date);
        simpleDateFormat.applyPattern("EEE"); // EEE indicates that the format of the day will contain 3 words
        if (dateObj != null) {
            return simpleDateFormat.format(dateObj);
        } else {
            return null;
        }

    }


    /*
     * animate view and scale them up to their original size
     */
    public static void animateScaleUp(View view, int duration) {
        view.animate().scaleX(1.0f).scaleY(1.0f).alpha(1.0f).setDuration(duration);

    }


    /*
     * animate view and scale them down to 0 size
     */
    public static void animateScaleDown(View view, int duration) {
        view.animate().scaleX(0.0f).scaleY(0.0f).alpha(0.0f).setDuration(duration);
    }
}
