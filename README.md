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

### Backend
- No inheritance anymore :(
- When registering company (now employee):
  - Super-User (hard-coded)
  - Only function: Create Employees (Name and Key)
  - Employee gets name and key and registers with those (enters own password)
  - Check whether key and name match the values from Super-User

### Frontend
- Delete Company folder
- Change registration (For employee: Less fields and key field)


## What we have to add

- **Change status of applications**
- Save draft of application / offer
- Birthday of Applicant