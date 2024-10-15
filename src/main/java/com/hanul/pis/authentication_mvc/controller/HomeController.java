package com.hanul.pis.authentication_mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
public class HomeController {

    @GetMapping("/")
    public ModelAndView homepage() { // also can take as a param a ModelMap or a Model

        ModelAndView modelAndView = new ModelAndView(); // ("home", model);
        modelAndView.setViewName("home");
        modelAndView.addObject("firstName", "Geo");
        modelAndView.addObject("lastName", "Chitibusar");

        // for Model & ModelMap
        //Map<String, String> model = new HashMap<>();
        //model.addAttribute("firstName", "Georgiana");
        //model.addAttribute("lastName", "Chitibus");
        // return "home";

        return modelAndView;
    }
}
