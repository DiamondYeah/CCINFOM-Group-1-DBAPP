DROP DATABASE IF EXISTS HiddenGemsDB;

CREATE DATABASE HiddenGemsDB;
USE HiddenGemsDB;

CREATE TABLE Points_Tier (

	Tier_ID INT AUTO_INCREMENT,
	Tier_Name VARCHAR(50) NOT NULL,
	Min_Points INT NOT NULL,
	Max_Points INT NOT NULL,
        
	CONSTRAINT Points_Tier_pk PRIMARY KEY (Tier_ID)
    
);


CREATE TABLE User (
		
	User_ID INT AUTO_INCREMENT,
	First_Name VARCHAR(50) NOT NULL,
	Last_Name VARCHAR(50) NOT NULL,
	Nationality VARCHAR(50),
	Points INT DEFAULT 0,
	Tier_ID INT,
	Password VARCHAR(255) NOT NULL,
    Is_Admin BOOLEAN DEFAULT FALSE,
        
	CONSTRAINT User_pk PRIMARY KEY (User_ID),
	CONSTRAINT User_Tier_fk FOREIGN KEY (Tier_ID) REFERENCES Points_Tier(Tier_ID)
        ON DELETE SET NULL
        ON UPDATE CASCADE
    
);


CREATE TABLE User_Phone (

	Phone_ID INT AUTO_INCREMENT,
	User_ID INT NOT NULL,
	Phone_Number VARCHAR(20) NOT NULL,
        
	CONSTRAINT User_Phone_pk PRIMARY KEY (Phone_ID),
	CONSTRAINT User_Phone_User_fk FOREIGN KEY (User_ID) REFERENCES User(User_ID)
        ON DELETE CASCADE
        ON UPDATE CASCADE
    
);


CREATE TABLE User_Email (

	Email_ID INT AUTO_INCREMENT,
	User_ID INT NOT NULL,
	Email VARCHAR(100) NOT NULL,
        
	CONSTRAINT User_Email_pk PRIMARY KEY (Email_ID),
	CONSTRAINT User_Email_User_fk FOREIGN KEY (User_ID) REFERENCES User(User_ID)
        ON DELETE CASCADE
        ON UPDATE CASCADE
        
);


CREATE TABLE Country (

	country_id INT AUTO_INCREMENT PRIMARY KEY,
	country_name VARCHAR(50) NOT NULL
        
);


CREATE TABLE Region (

	region_id INT AUTO_INCREMENT PRIMARY KEY,
	region_name VARCHAR(50) NOT NULL,
	country_id INT NOT NULL,
        
	FOREIGN KEY (country_id) REFERENCES Country(country_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
        
);


CREATE TABLE City (

	city_id INT AUTO_INCREMENT PRIMARY KEY,
	city_name VARCHAR(50) NOT NULL,
	region_id INT NOT NULL,
        
	FOREIGN KEY (region_id) REFERENCES Region(region_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
        
);


CREATE TABLE Travel_Spot (

	location_id INT AUTO_INCREMENT PRIMARY KEY,
	User_id INT NOT NULL,
	area VARCHAR(100) NOT NULL,
	availability ENUM('Available', 'Unavailable') DEFAULT 'Available',
	date_shared DATE NOT NULL,
	city_id INT NOT NULL,
	is_recommended BOOLEAN DEFAULT FALSE,
        
	FOREIGN KEY (city_id) REFERENCES City(city_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
	FOREIGN KEY (user_id) REFERENCES User(user_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
        
);


CREATE TABLE Category (

	category_id INT AUTO_INCREMENT PRIMARY KEY,
	category_name VARCHAR(100) NOT NULL
        
);


CREATE TABLE TS_Category (

	location_id INT NOT NULL,
	category_id INT NOT NULL,
        
	PRIMARY KEY (location_id, category_id),
	FOREIGN KEY (location_id) REFERENCES Travel_Spot(location_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
	FOREIGN KEY (category_id) REFERENCES Category(category_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
        
);


CREATE TABLE Reaction(

	ReactionType_ID 	INT AUTO_INCREMENT NOT NULL,
	Reaction_Name		VARCHAR(50) NOT NULL,
    
    CONSTRAINT Reaction_pk PRIMARY KEY (ReactionType_ID)
        
);


CREATE TABLE User_Feedback(

	Review_ID 			INT AUTO_INCREMENT NOT NULL,
    User_ID 			INT NOT NULL,
    Location_ID 		INT NOT NULL,
    Rating 		        DECIMAL(2, 1) CHECK(Rating BETWEEN 1 AND 5),
    is_recommendation   BOOLEAN DEFAULT FALSE,
    Reaction_Count 		INT DEFAULT 0,
    Comment_Count 		INT DEFAULT 0,
    Review_Date 		DATETIME DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT User_Feedback_pk PRIMARY KEY (Review_ID),
    CONSTRAINT User_Feedback_UserID_fk FOREIGN KEY (User_ID) REFERENCES User (User_ID)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT User_Feedback_LocationID_fk FOREIGN KEY (Location_ID) REFERENCES Travel_Spot (Location_ID)
        ON DELETE CASCADE
        ON UPDATE CASCADE

);


CREATE TABLE User_Reaction(

    Reaction_ID 		INT AUTO_INCREMENT NOT NULL,
    Review_ID 			INT NOT NULL,
    User_ID 			INT NOT NULL,
    ReactionType_ID		INT NOT NULL,
    Reaction_Date 		DATETIME DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT User_Reaction_pk PRIMARY KEY (Reaction_ID),
    CONSTRAINT User_Reaction_ReviewID_fk FOREIGN KEY (Review_ID) REFERENCES User_Feedback (Review_ID)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT User_Reaction_UserID_fk FOREIGN KEY (User_ID) REFERENCES User (User_ID)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT User_Reaction_ReactionTypeID_fk FOREIGN KEY (ReactionType_ID) REFERENCES Reaction (ReactionType_ID)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT Unique_User_Reaction UNIQUE (Reaction_ID, Review_ID, User_ID)
                
);


CREATE TABLE Booking(
    Booking_ID INT AUTO_INCREMENT PRIMARY KEY,
    Location_ID INT NOT NULL,
    Status VARCHAR(50) NOT NULL,
    Start_date DATE NOT NULL,
    End_date DATE NOT NULL,
    Booking_Dates VARCHAR(100) AS (CONCAT(Start_date, ' to ', End_date)) STORED,
    Tax DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    Gem_price DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    Price DECIMAL(10,2) AS (Tax + Gem_price) STORED,
    Organizer_ID INT NOT NULL,
    Current_Capacity INT NOT NULL DEFAULT 0,
    Max_Capacity INT NOT NULL,
    
    CONSTRAINT fk_booking_location
        FOREIGN KEY (Location_ID)
        REFERENCES travel_spot(Location_ID)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    
    CONSTRAINT fk_booking_organizer
        FOREIGN KEY (Organizer_ID)
        REFERENCES User(User_ID)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);


CREATE TABLE User_Booking (
	User_Booking_ID INT AUTO_INCREMENT PRIMARY KEY,
	Booking_ID INT NOT NULL,
	User_ID INT NOT NULL,
	Role VARCHAR(50) NOT NULL,
	CONSTRAINT fk_userbooking_booking
		FOREIGN KEY (Booking_ID)
		REFERENCES booking(Booking_ID)
		ON DELETE CASCADE
		ON UPDATE CASCADE,

	CONSTRAINT fk_userbooking_user
		FOREIGN KEY (User_ID)
		REFERENCES User(User_ID)
		ON DELETE CASCADE
		ON UPDATE CASCADE
);