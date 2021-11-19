package com.sirma.employees.service.impl;

import com.sirma.employees.model.Job;
import com.sirma.employees.model.Project;
import com.sirma.employees.service.ProjectService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Override
    public List<Project> getBestTeamProjects(List<Job> jobsSortedByProjectId) {

        // Key - EmpId_1 + EmpId_2; Value -> HashMap<Key(ProjectId), Value(Days worked)>
        HashMap<String, HashMap<String, Integer>> result = new HashMap<>();

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
                    // compare the periods they work on this same Project, if they match
                    if (matchingPeriod(aDateFrom, aDateTo, bDateFrom, bDateTo)) {
                        String key = currentJob.getEmployeeId() + " " + jobToCompare.getEmployeeId();

                        result.putIfAbsent(key, new HashMap<>());

                        String projectId = currentJob.getProjectId();
                        HashMap<String, Integer> projectIdDaysMap = result.get(key);
                        projectIdDaysMap.putIfAbsent(projectId, 0);

                        Integer currentDays = projectIdDaysMap.get(projectId);
                        Integer newValueOfDays = currentDays + calculateDays(aDateFrom, aDateTo, bDateFrom, bDateTo);
                        projectIdDaysMap.put(projectId, newValueOfDays);
                    }
                }
            }
        }

        // find best Team
        HashMap<String, Integer> teamsDays = new HashMap<>();
        result.forEach((ids, map) -> {
            int totalDays = map.values()
                    .stream()
                    .mapToInt(el -> el)
                    .sum();
            teamsDays.putIfAbsent(ids, 0);
            teamsDays.put(ids, teamsDays.get(ids) + totalDays);
        });

        List<Map.Entry<String, Integer>> teamsOrderedByDaysDesc = teamsDays.entrySet()
                .stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .collect(Collectors.toList());

        String bestTeam = "";
        if (!teamsOrderedByDaysDesc.isEmpty()) {
            bestTeam = teamsOrderedByDaysDesc.get(0).getKey();
        }

        List<Project> bestTeamProjects = new ArrayList<>();
        // print only Best Team - projects / days
        String[] empId1EmpId2 = bestTeam.split(" ");
        result.get(bestTeam)
                .forEach((k, v) -> {
                    Project project = new Project().setEmployeeId1(empId1EmpId2[0]).setEmployeeId2(empId1EmpId2[1])
                            .setProjectId(k).setDays(v);
                    bestTeamProjects.add(project);
                    System.out.println(project);
                });

//        System.out.println("------ TESTING All records printing - TO BE DELETED ! -------");
//        // print all records
//        for (Map.Entry<String, HashMap<String, Long>> entry : result.entrySet()) {
//            String[] allEmpId1EmpId2 = entry.getKey().split(" ");
//            entry.getValue()
//                    .forEach((k, v) -> System.out.println(allEmpId1EmpId2[0] + " | " + allEmpId1EmpId2[1] + " | " + k + " | " + v));
//        }

        return bestTeamProjects;
    }

    private boolean matchingPeriod(LocalDate aDateFrom, LocalDate aDateTo,
                                   LocalDate bDateFrom, LocalDate bDateTo) {
        if (aDateFrom.isAfter(bDateTo) || aDateTo.isBefore(bDateFrom)) {
            return false;
        }
        return true;
    }

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
