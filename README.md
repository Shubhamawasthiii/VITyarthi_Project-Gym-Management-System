# Gym Management System (Java Console Application)

A console-based Gym Management System developed using pure Core Java.  
Runs completely in CMD / Terminal, uses file storage, and features auto-increment ID generation.

---

## Features

### 1. Member Management
- Add new member
- View all members
- Auto-generated member IDs
- Data saved in `members.txt`

### 2. Trainer Management
- Add trainer
- View trainers
- Auto-generated trainer IDs
- Data saved in `trainers.txt`

### 3. Plan Management
- Create membership plans
- Set plan prices
- Auto-generated plan IDs
- Data saved in `plans.txt`

### 4. Subscription Management
- Create subscriptions
- Link members to plans and trainers
- Auto-generated subscription IDs
- Data saved in `subs.txt`

---
## Technologies/Tools Used
- Java (Core Java)
- File Handling
- Scanner for user input
- ArrayList for data storage
---
## Project Files

- GymSystem.java (main program file)
- members.txt (stores member data)
- trainers.txt (stores trainer data) 
- plans.txt (stores plan data)
- subs.txt (stores subscription data)
---

## OOP Concepts Used

- Class & Object
- Constructors
- Encapsulation
- File Handling (I/O Streams)
- Auto-ID generator
- Service Layer pattern
---
## Steps to Install & Run the Project
1. Make sure you have Java installed on your computer
2. Download all the project files into one folder
3. Open command prompt in that folder
4. Compile the Java file: `javac GymSystem.java`
5. Run the program: `java GymSystem`

## Instructions for Testing
1. Run the program and try adding a new member
2. Add a trainer and create a plan
3. Create a subscription linking the member to the plan
4. Use "View All Data" to check everything saved correctly
5. Close and reopen the program to verify data loads back

