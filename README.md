# CCINFOM-Group-1-DBAPP
Repository for Group 1 DB Application. Will store the SQL database and various commits for the application.

<h3> STEPS: </h3>
1. Download files (SQL and Java)

2. Run the SQL script in MYSQL to create the Database

3. Download Java Connector for SQL (https://dev.mysql.com/downloads/connector/j/)

4. Watch instructions on how to connect the connector to Java. 2 Videos are provided depending on which approach you want (VSCODE -> https://www.youtube.com/watch?v=MtME-ERufu0) (Class-Path -> https://www.youtube.com/watch?v=8aTpDfsYTNQ)

5. Create <b> db.properties  and add db.url, db.user, db.password in the directory. Fill out each with the database name, user, and password of your local MYSQL </b>.

6. Check if the connection works by running the program. The message will display whether the connection was successful or unsuccessful.

7. Check for login credentials in MYSQL by performing query <b>(SELECT * FROM user)</b>. Name field requires First_Name and Last_Name. Password field requires Password.
   
8. Implement your MVC with tables, transactions, and reports.

---
<h3> db.properties FORMAT: </h3>

<b> NOTE: All "" have to be filled out by you </b>

db.url = "database_path"

db.user = "root"

db.password = "MYSQL_Password"


---

<h3> LINKS: </h3>

SQL & Java Integration: https://drive.google.com/file/d/1BuI93fJYXsNNm50uqeoPv6Oef86wi4LL/view?fbclid=IwY2xjawN2WVxleHRuA2FlbQIxMABicmlkETFUeTc0RjR3N2RERW5Pd2xrAR5oDcmAQmMjLXAnovzVskXi1gPZhdTSN9maDarMb1OZtv6CPPKtd1KdgumKvw_aem_qFUIsDpR_7QIOWBBDtMLeQ

Java GUI: https://drive.google.com/file/d/1_ugXhPNv0PUkEmpsGuk3RHMAx_7EA8d4/view?fbclid=IwY2xjawN2WVpleHRuA2FlbQIxMABicmlkETFUeTc0RjR3N2RERW5Pd2xrAR5oDcmAQmMjLXAnovzVskXi1gPZhdTSN9maDarMb1OZtv6CPPKtd1KdgumKvw_aem_qFUIsDpR_7QIOWBBDtMLeQ

Java Connector: https://dev.mysql.com/downloads/connector/j/

Connector to VSCODE: https://www.youtube.com/watch?v=MtME-ERufu0

Connector to Class-path: https://www.youtube.com/watch?v=8aTpDfsYTNQ
