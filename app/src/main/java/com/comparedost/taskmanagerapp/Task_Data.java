package com.comparedost.taskmanagerapp;

public class Task_Data {
    public String Taskname;
    public String Taskdescription;
    public String Assignedto;
    public String Taskstatus;

    public Task_Data(String tname, String tdesc,String assi, String sta){

        this.Taskname=tname;
        this.Taskdescription=tdesc;
        this.Assignedto=assi;
        this.Taskstatus=sta;


    }

    public String getTaskname() {
        return Taskname;
    }

    public void setTaskname(String taskname) {
        Taskname = taskname;
    }

    public String getTaskdescription() {
        return Taskdescription;
    }

    public void setTaskdescription(String taskdescription) {
        Taskdescription = taskdescription;
    }

    public String getAssignedto() {
        return Assignedto;
    }

    public void setAssignedto(String assignedto) {
        Assignedto = assignedto;
    }

    public String getTaskstatus() {
        return Taskstatus;
    }

    public void setTaskstatus(String taskstatus) {
        Taskstatus = taskstatus;
    }

}
