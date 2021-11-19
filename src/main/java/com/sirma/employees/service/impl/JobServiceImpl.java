package com.sirma.employees.service.impl;

import com.sirma.employees.model.Job;
import com.sirma.employees.service.JobService;
import com.sirma.employees.util.DateUtility;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {

    @Override
    public List<Job> readAllJobsFromFile(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        List<Job> jobs = new ArrayList<>();

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            String[] split = line.split(", ");
            if (split[3].equals("NULL")) {
                split[3] = String.valueOf(LocalDate.now());
            }

            LocalDate dateFrom = DateUtility.parseDate(split[2]);
            LocalDate dateTo = DateUtility.parseDate(split[3]);

            Job job = new Job().setEmployeeId(split[0]).setProjectId(split[1])
                    .setDateFrom(dateFrom)
                    .setDateTo(dateTo);
            jobs.add(job);
        }

        // order Jobs by ProjectId, and then by EmployeeId
        // ProjectId is the "link" between EmployeeId and Time Frame
        return jobs.stream()
                .sorted(Comparator.comparing(Job::getProjectId))
                .sorted(Comparator.comparing(Job::getEmployeeId))
                .collect(Collectors.toList());
    }
}
