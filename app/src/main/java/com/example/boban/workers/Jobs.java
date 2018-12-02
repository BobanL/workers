package com.example.boban.workers;

public class Jobs {
    private String jobName;
    private String jobLocation;
    private String jobStatus;
    private String jobCost;

    public Jobs(String jobName, String jobLocation, String jobStatus, String jobCost) {
        this.jobName = jobName;
        this.jobLocation = jobLocation;
        this.jobStatus = jobStatus;
        this.jobCost = jobCost;
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
