package com.sirma.employees.web;

import com.sirma.employees.model.Job;
import com.sirma.employees.service.JobService;
import com.sirma.employees.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 100
)
public class HomeController {
    private final JobService jobService;
    private final ProjectService projectService;

    public HomeController(JobService jobService, ProjectService projectService) {
        this.jobService = jobService;
        this.projectService = projectService;
    }

    @GetMapping
    public String getHomePage(Model model) {
        model.addAttribute("projects", new ArrayList<>());
        return "index";
    }


    @PostMapping
    public String getHomeWithBestTeam(Model model, HttpServletRequest request) throws ServletException, IOException {

        Part filePart = request.getPart("file");
        List<Job> jobs = jobService.readAllJobsFromFile(filePart.getInputStream());

        model.addAttribute("projects", projectService.getBestTeamProjects(jobs));
        return "index";
    }
}
