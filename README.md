# Hidden Gems Database Application

A Database Application made via Java Swing and SQL for a capstone project where it aims to share hidden gems (non-popular locations) to other users. Travel-themed application features basic CRUD for different information on Users, Travel, Feedback, and Booking. The repo includes both the application and SQL file for the database.

---

## Features

- CRUD for storing information on Users, Travel, Feedback, and Booking
- Graphical User Interface (GUI) via Java Swing
- Working login and registering Page
- Admin and user views for CRUD
- Real-time updating of database via MYSQL

---

## Installation and Running

1. Download files (SQL and Java)

2. Run the SQL script in MYSQL to create the Database

3. Download [Java Connector for SQL](https://dev.mysql.com/downloads/connector/j/)

4. Watch instructions on how to connect the connector to Java. 2 Videos are provided depending on which approach you want ([VSCODE](https://www.youtube.com/watch?v=MtME-ERufu0)) ([Class-Path](https://www.youtube.com/watch?v=8aTpDfsYTNQ))

5. Create <b> db.properties  and add db.url, db.user, db.password in the directory. Fill out each with the database name, user, and password of your local MYSQL </b>.

6. Check if the connection works by running the program. The message will display whether the connection was successful or unsuccessful.

7. Check for login credentials in MYSQL by performing query <b>(SELECT * FROM user)</b>. Name field requires First_Name and Last_Name. Password field requires Password.

```
db.url = "database_path"
db.user = "root"
db.password = "MYSQL_Password"
```
   
8. Implement your MVC with tables, transactions, and reports.

---

## Authors

- DiamondYeah
- jhiroliri0
- anzrro (Aaron Zander Romero)
- joreve

---

## License

This program was developed as a course project for academic purposes.

All rights reserved by the authors and De La Salle University.



