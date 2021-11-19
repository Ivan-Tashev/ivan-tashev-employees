package com.sirma.employees.model;

import java.time.LocalDate;

public class Job {
    private String employeeId;
    private String projectId;
    private LocalDate dateFrom;
    private LocalDate dateTo;

    public String getEmployeeId() {
        return employeeId;
    }

    public Job setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
        return this;
    }

    public String getProjectId() {
        return projectId;
    }

    public Job setProjectId(String projectId) {
        this.projectId = projectId;
        return this;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public Job setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
        return this;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public Job setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
        return this;
    }
}
