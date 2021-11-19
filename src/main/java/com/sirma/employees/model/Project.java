package com.sirma.employees.model;

public class Project {
    private String employeeId1;
    private String employeeId2;
    private String projectId;
    private long days;

    public String getEmployeeId1() {
        return employeeId1;
    }

    public Project setEmployeeId1(String employeeId1) {
        this.employeeId1 = employeeId1;
        return this;
    }

    public String getEmployeeId2() {
        return employeeId2;
    }

    public Project setEmployeeId2(String employeeId2) {
        this.employeeId2 = employeeId2;
        return this;
    }

    public String getProjectId() {
        return projectId;
    }

    public Project setProjectId(String projectId) {
        this.projectId = projectId;
        return this;
    }

    public long getDays() {
        return days;
    }

    public Project setDays(long days) {
        this.days = days;
        return this;
    }

    @Override
    public String toString() {
        return employeeId1 + " | " + employeeId2 + " | " +
                projectId + " | " + days;
    }
}
