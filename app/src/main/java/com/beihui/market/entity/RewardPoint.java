package com.beihui.market.entity;


public class RewardPoint {

    private String id;
    private int priority;
    private String taskName;
    private String taskType;
    private int integ;
    private int dayMaxInteg;
    private int status;
    private int flag;
    private String recordId;
    private int sumInteg;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public int getInteg() {
        return integ;
    }

    public void setInteg(int integ) {
        this.integ = integ;
    }

    public int getDayMaxInteg() {
        return dayMaxInteg;
    }

    public void setDayMaxInteg(int dayMaxInteg) {
        this.dayMaxInteg = dayMaxInteg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public int getSumInteg() {
        return sumInteg;
    }

    public void setSumInteg(int sumInteg) {
        this.sumInteg = sumInteg;
    }
}
