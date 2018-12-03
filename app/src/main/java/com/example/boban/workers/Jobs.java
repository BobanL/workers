package com.example.boban.workers;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Jobs {
    private String jobName;
    private String jobLocation;
    private String jobStatus;
    private String jobCost;
    private String jobDescription;
    private ArrayList<Bitmap> jobImages;

    public Jobs(String jobName, String jobLocation, String jobDescription) {
        this.jobName = jobName;
        this.jobLocation = jobLocation;
        this.jobDescription = jobDescription;
    }

    public void setJobCost(String jobCost) {
        this.jobCost = jobCost;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(String jobLocation) {
        this.jobLocation = jobLocation;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getJobCost() {
        return jobCost;
    }

    public void setjobCost(String jobCost) {
        this.jobCost = jobCost;
    }
}
