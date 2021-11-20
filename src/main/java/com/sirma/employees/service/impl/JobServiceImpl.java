/*
 * The JobServiceImpl class process the Input Stream of data,
 * from the uploaded text file.
 * Collect all Jobs(employeeId, ProjectId, DateFrom, DateTo) data,
 * fixes possible NULL values to Date.now() for DateTo property.
 * Return list of Jobs.
 *
 * Version information - v.1.0
 *
 * Date - 20.Nov.2021
 *
 * Copyright notice - none
 */

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
import java.util.List;

@Service
public class JobServiceImpl implements JobService {

    @Override
    public List<Job> readAllJobsFromFile(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        List<Job> jobs = new ArrayList<>();

        String line;
        // read the Input stream by lines
        while ((line = bufferedReader.readLine()) != null) {
            String[] split = line.split(", ");
            if (split[3].equalsIgnoreCase("NULL")) {
                split[3] = String.valueOf(LocalDate.now());
            }

            String employeeId = split[0];
            String projectId = split[1];
            LocalDate dateFrom = DateUtility.parseDate(split[2]);
            LocalDate dateTo = DateUtility.parseDate(split[3]);

            // create the Job, and add to collection List<Job>
            Job job = new Job().setEmployeeId(employeeId).setProjectId(projectId)
                                .setDateFrom(dateFrom).setDateTo(dateTo);
            jobs.add(job);
        }

        return jobs;
    }
}
