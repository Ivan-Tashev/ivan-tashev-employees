package com.sirma.employees.service;

import com.sirma.employees.model.Job;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface JobService {

    List<Job> readAllJobsFromFile(InputStream inputStream) throws IOException;
}
