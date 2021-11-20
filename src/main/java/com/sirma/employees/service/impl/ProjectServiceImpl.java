/*
 * The ProjectServiceImpl accept as input argument List<Job>,
 * orderedByProjectId, then by EmployeeId.
 * Iterate over each element from the collection, comparing it
 * with the Dates of the next element.
 * The matching Periods are kept in a HashMap with key - Team(EmpId1 EmpId2),
 * and value - nested HashMap with key - ProjectId and value - Worked days.
 *
 * Then sort all Teams total Work days in Descending order, and find the
 * Best Team or Teams(if the topmost have same amount of days).
 *
 * The found Best Team is
 *
 * Version information - v.1.0
 *
 * Date - 20.Nov.2021
 *
 * Copyright notice - none
 */

package com.sirma.employees.service.impl;

import com.sirma.employees.model.Job;
import com.sirma.employees.model.Project;
import com.sirma.employees.service.ProjectService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Override
    public List<Project> getBestTeamProjects(List<Job> jobs) {
        // order the list of Jobs
        List<Job> jobsSortedByProjectId = listOrderByProjectIdThenEmployeeId(jobs);

        // Key - "EmpId_1 EmpId_2"; Value -> HashMap<ProjectId, Sum Days worked>
        HashMap<String, HashMap<String, Integer>> teamsData = new HashMap<>();

        // Nested loops -> O(n^2) - quadratic complexity - can be optimized ?!
        for (int i = 0; i < jobsSortedByProjectId.size(); i++) {
            Job currentJob = jobsSortedByProjectId.get(i);

            for (int j = i + 1; j < jobsSortedByProjectId.size(); j++) {
                Job jobToCompare = jobsSortedByProjectId.get(j);

                // if next Job HAVE SAME projectId and IS NOT the current(first) Employee
                if (currentJob.getProjectId().equals(jobToCompare.getProjectId())
                        && !currentJob.getEmployeeId().equals(jobToCompare.getEmployeeId())) {

                    LocalDate aDateFrom = currentJob.getDateFrom();
                    LocalDate aDateTo = currentJob.getDateTo();
                    LocalDate bDateFrom = jobToCompare.getDateFrom();
                    LocalDate bDateTo = jobToCompare.getDateTo();

                    // compare the periods both Employees work on this same Project, if they match
                    if (matchingPeriod(aDateFrom, aDateTo, bDateFrom, bDateTo)) {
                        String key = currentJob.getEmployeeId() + " " + jobToCompare.getEmployeeId();

                        teamsData.putIfAbsent(key, new HashMap<>());

                        String projectId = currentJob.getProjectId();
                        HashMap<String, Integer> projectIdDaysMap = teamsData.get(key);
                        projectIdDaysMap.putIfAbsent(projectId, 0);

                        Integer currentDays = projectIdDaysMap.get(projectId);
                        Integer newValueOfDays = currentDays + calculateDays(aDateFrom, aDateTo, bDateFrom, bDateTo);
                        projectIdDaysMap.put(projectId, newValueOfDays);
                    }
                }
            }
        }

        // find best Team, with most Work days
        HashMap<String, Integer> teamsDays = new HashMap<>();
        teamsData.forEach((ids, map) -> {
            int totalDays = map.values()
                    .stream()
                    .mapToInt(el -> el)
                    .sum();
            teamsDays.putIfAbsent(ids, 0);
            teamsDays.put(ids, teamsDays.get(ids) + totalDays);
        });

        List<Map.Entry<String, Integer>> teamsOrderedByTotalDaysDesc = teamsDays.entrySet()
                .stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .collect(Collectors.toList());

        String bestTeam = "";
        if (!teamsOrderedByTotalDaysDesc.isEmpty()) {
            bestTeam = teamsOrderedByTotalDaysDesc.get(0).getKey();
        }

        List<Project> bestTeamProjects = new ArrayList<>();
        // output only Best Team - projects / days
        String[] empId1EmpId2 = bestTeam.split(" ");
        teamsData.get(bestTeam)
                .forEach((k, v) -> {
                    Project project = new Project().setEmployeeId1(empId1EmpId2[0]).setEmployeeId2(empId1EmpId2[1])
                            .setProjectId(k).setDays(v);
                    bestTeamProjects.add(project);
                    System.out.println(project);
                });

        return bestTeamProjects;
    }

    // method for sorting List<Job> by ProjectId, then EmployeeID
    private List<Job> listOrderByProjectIdThenEmployeeId(List<Job> jobs) {
        return jobs.stream()
                .sorted(Comparator.comparing(Job::getProjectId))
                .sorted(Comparator.comparing(Job::getEmployeeId))
                .collect(Collectors.toList());
    }

    // method for approval of matching periods between Dates
    private boolean matchingPeriod(LocalDate aDateFrom, LocalDate aDateTo,
                                   LocalDate bDateFrom, LocalDate bDateTo) {
        return !aDateFrom.isAfter(bDateTo) && !aDateTo.isBefore(bDateFrom);
    }

    // method for calculating days of matching periods
    private int calculateDays(LocalDate aDateFrom, LocalDate aDateTo,
                               LocalDate bDateFrom, LocalDate bDateTo) {
    // ChronoUnit.DAYS.between(param1, param2) - return the difference from data(incl.) to date(excl.)
    // this is the reason for starting match period of days with 1.
        int days = 1;
        if (aDateFrom.isBefore(bDateFrom)) {
            if (aDateTo.isBefore(bDateTo)) {
                days += (int) ChronoUnit.DAYS.between(bDateFrom, aDateTo);
            } else { // aDateTo.isAfterOrEqual(bDateFrom)
                days += (int) ChronoUnit.DAYS.between(bDateFrom, bDateTo);
            }
        } else { // aDateFrom.isAfterOrEqual(bDateFrom)
            if (aDateTo.isBefore(bDateTo)) {
                days += (int) ChronoUnit.DAYS.between(aDateFrom, aDateTo);
            } else { // aDateTo.isAfterOrEqual(bDateFrom)
                days += (int) ChronoUnit.DAYS.between(aDateFrom, bDateTo);
            }
        }
        return days;
    }
}
