# GFOS Innovationsaward
Repository for my (and Yixue04's) work at the GFOS Innovationsaward 2022

## Task
The task is to develop an application management system

## Technologies
- Glassfish Server 5.1.0
- MySQL 8.0.27
- JavaEE 8

## Division of labour
- TJTesla: Backend; Integration of the View with the Database
- Yixue04: Frontend; Design of the Views

## Resource
https://www.gfos.com/gfos/mint-lab/gfos-innovationsaward.html

## What we need to change

### Database
- Delete company ✅
- Fetching independant from company ✅
- Create new POJO/Table: Employee ✅
- Salting of passwords ✅

### Backend
- No inheritance anymore :(
- When registering company (now employee): ✅ **(Untested)**
  - Super-User (hard-coded) ✅
  - Only function: Create Employees (Name and Key) ✅
  - Employee gets name and key and registers with those (enters own password) ✅
  - Check whether key and name match the values from Super-User ✅

### Frontend
- Delete Company folder ✅
- Site for changing password ✅
- Overview of applied offers ✅ (Just need controller)
- Overview of all applications for one offer (in detail view for offer) ✅


## What we have to add

- **Change status of applications:**
  - Change status from employee side
  	 - Bottom of application
  	 - Select one of options (accept, decline, in process)
  - See status from applicant side
  	 - Show List of Applications and results in profile
  	 - Write E-Mail to user with result
- Save draft of application / offer
  - TODO: How presented 
- Additional filter for *applied offers*, *not applied offers* or *favorites*
- Birthday of Applicant: ✅
  - Add in registration form ✅
  - Validation ✅
  - Add in database and bean file ✅