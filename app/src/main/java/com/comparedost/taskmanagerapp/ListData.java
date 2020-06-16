package com.comparedost.taskmanagerapp;

public class ListData {
    public String ProjectName;
    public String Projectdescription;
   // public int ProjectImage;
    public  ListData(String proname,String prodes){
        this.ProjectName=proname;
        this.Projectdescription=prodes;
      //  this.ProjectImage=image;
    }

    public String getProjectName() {
        return ProjectName;
    }

    public String getProjectdescription() {
        return Projectdescription;
    }


    public void setProjectName(String projectName) {
        ProjectName = projectName;
    }

    public void setProjectdescription(String projectdescription) {
        Projectdescription = projectdescription;
    }



}
