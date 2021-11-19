package com.sirma.employees.service;

import com.sirma.employees.model.Job;
import com.sirma.employees.model.Project;

import java.util.List;

public interface ProjectService {
    List<Project> getBestTeamProjects(List<Job> jobs);
}
