/*
 * The HomeController class serves the web application layer,
 * with HTTP Request Mappings from the Front-End and
 * corresponding return Views, as HTML pages.
 *
 * Version information - v.1.0
 *
 * Date - 20.Nov.2021
 *
 * Copyright notice - none
 */

package com.sirma.employees.web;

import com.sirma.employees.model.Job;
import com.sirma.employees.service.JobService;
import com.sirma.employees.service.ProjectService;
import com.sirma.employees.util.DateUtility;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {
    private final JobService jobService;
    private final ProjectService projectService;

    public HomeController(JobService jobService, ProjectService projectService) {
        this.jobService = jobService;
        this.projectService = projectService;
    }

    @GetMapping
    public String getHomePage(Model model) {
        model.addAttribute("projects", new ArrayList<>())
                .addAttribute("dateFormats", DateUtility.DATE_FORMATS);
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
