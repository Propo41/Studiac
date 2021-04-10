Studiac
=============================

Description
------------

A utility application that allow students to keep track of their daily activities, set reminders for their exams or assignments, keep track of the important news and extracurricular activities of their respective universities, such as seminars, club events, etc.

- Target Android version: Marshmallow (V 6.0)

- Project Functionality and Key Features

  * `Tracking your Routine and Courses` - You can simply put down the courses you have taken each semester, along with their credits, timings, instructor information and etc..
  * `To-do tasks` - A to-do task designed specifically for students with the aim of keeping track of their ongoing assessments, assignments, and homework for each course with an option to remind them at due time.
  * `Bulletin Board` - A public bulletin board, visible to every student of their respective universities, that contains posts that are categorized into individual topics such as items to giveaway, sale posts, seminar schedules, club recruitment posts, help-seeking posts, and many more, depending on your needs.
  * `Result tracker` - A utility tool that allows students to keep track of their CGPA as well as calculate their GPA of their semesters depending on their achieved grades. 
  * `Sending personalized messages` - You can easily send texts individually to users who posted announcements on the bulletin board regarding any further queries.

Future Implementations
---------------

- Anonymous Global Chat Group: A group conversation, with one thread per university, where all students can discuss and hang out freely.
- Bus tracker: a tracking service that allows the students to track their university busses. 
- User Status: users can be able to check others in their universities who are online.
- Delete Message Threads: users can delete the message threads by pressing on long click

Screenshots
--------------------------
![image](https://user-images.githubusercontent.com/46298019/114276217-7cacff80-9a47-11eb-8dc4-f35c1cc74fd1.png)

![image](https://user-images.githubusercontent.com/46298019/114276220-82a2e080-9a47-11eb-8505-e7649408e863.png)

![image](https://user-images.githubusercontent.com/46298019/114276226-89c9ee80-9a47-11eb-8783-e87b92cbe09e.png)

![image](https://user-images.githubusercontent.com/46298019/114276236-90f0fc80-9a47-11eb-8968-a1e3328e9f30.png)

![image](https://user-images.githubusercontent.com/46298019/114276247-99493780-9a47-11eb-8c68-e697b7338e3c.png)

![image](https://user-images.githubusercontent.com/46298019/114276250-9fd7af00-9a47-11eb-8bce-d226be38e523.png)



Technical Details
---------------

- Used Firebase Database for the backend service. 
- Used Firebase Cloud Messaging for the push notifications that trigger every time a new post has been added to the bulletin board.
- Message notifications, for the time being, has been used locally. FCM is not used.
- Data for the todo tasks is saved locally. Upon clicking the sync button, the data gets uploaded to the database.
- Since firebase can support up to 10MB of data locally while offline, all other data is saved directly in the database.


Project doc: https://docs.google.com/spreadsheets/d/1A2JqFxCHdygT_PnYNztQmMI-_O3uoOfdiQrATbli3gY/edit?usp=sharing


