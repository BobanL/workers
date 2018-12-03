package com.example.boban.workers;

import android.graphics.Bitmap;
import android.util.Base64;

import java.util.ArrayList;

public class Jobs {
    private String jobName;
    private String jobDescription;
    private ArrayList<String> jobImages;
    private String jobStreet;
    private String jobCity;
    private int jobZip;
    private String jobSubmitter;
    private long jobPostedDate;
    private String jobStatus;
    private String jobCost;

    public Jobs(String jobName, String jobDescription, ArrayList<String> jobImages, String jobStreet, String jobCity, int jobZip, String jobSubmitter, long jobPostedDate) {
        this.jobName = jobName;
        this.jobDescription = jobDescription;
        this.jobImages = jobImages;
        this.jobStreet = jobStreet;
        this.jobCity = jobCity;
        this.jobZip = jobZip;
        this.jobSubmitter = jobSubmitter;
        this.jobPostedDate = jobPostedDate;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public ArrayList<String> getJobImages() {
        return jobImages;
    }

    public void setJobImages(ArrayList<String> jobImages) {
        this.jobImages = jobImages;
    }

    public String getJobStreet() {
        return jobStreet;
    }

    public void setJobStreet(String jobStreet) {
        this.jobStreet = jobStreet;
    }

    public String getJobCity() {
        return jobCity;
    }

    public void setJobCity(String jobCity) {
        this.jobCity = jobCity;
    }

    public int getJobZip() {
        return jobZip;
    }

    public void setJobZip(int jobZip) {
        this.jobZip = jobZip;
    }

    public String getJobSubmitter() {
        return jobSubmitter;
    }

    public void setJobSubmitter(String jobSubmitter) {
        this.jobSubmitter = jobSubmitter;
    }

    public long getJobPostedDate() {
        return jobPostedDate;
    }

    public void setJobPostedDate(long jobPostedDate) {
        this.jobPostedDate = jobPostedDate;
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

    public void setJobCost(String jobCost) {
        this.jobCost = jobCost;
    }
}
