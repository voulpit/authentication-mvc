package com.hanul.pis.authentication_mvc.controller;

import com.hanul.pis.authentication_mvc.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
public class UsersController {
    private static final String TOKEN = "Petri eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhbmFAeWFob28uY29tIiwiaWF0IjoxNzI5MDA0OTkwLCJleHAiOjE3Mjk4Njg5OTB9.XTXlqUxfGHBgo44QESdXv62HlMiOe31vhDS2nxZ5zg0qH_Nfbh4XcQV6t5zKr2E2gF3LytgYyslzf1MFJVAeoQ";
    private static final String USER_DETAILS_URL = "http://localhost:8080/authentication/users/{userId}";
    private static final String USER_SIGNUP_URL = "http://localhost:8080/authentication/users";
    private static final String ACTIVATE_ACCOUNT_URL = "http://localhost:8080/authentication/users/email-verification?token={token}";

    @GetMapping(path = "/users/{userId}")
    public ModelAndView getUser(@PathVariable("userId") String userId) { // @RequestParam pt query params

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", TOKEN);
        HttpEntity<User> request = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<User> response = restTemplate.exchange(USER_DETAILS_URL.replace("{userId}", userId), HttpMethod.GET, request, User.class);
        User user = response.getBody();

        ModelAndView modelAndView = new ModelAndView("user"); // => we need to create file user.html
        modelAndView.addObject("userId", userId);
        modelAndView.addObject("firstName", user.getFirstName());
        modelAndView.addObject("lastName", user.getLastName());
        modelAndView.addObject("email", user.getEmail());
        return modelAndView;
    }

    // Reads form data from signup.html form
    @PostMapping(path="/users")
    public ModelAndView signupFormSubmit(@ModelAttribute User user) {
        HttpEntity<User> request = new HttpEntity<>(user);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<User> response = restTemplate.exchange(USER_SIGNUP_URL, HttpMethod.POST, request, User.class);

        List<String> authToken = response.getHeaders().get("Authorization");
        if (authToken != null && !authToken.isEmpty()) {
            String token = authToken.getFirst();
        }
        String userId = response.getBody().getUserId();

        ModelAndView modelAndView = new ModelAndView("signup-result");
        modelAndView.addObject("userId", userId);
        modelAndView.addObject("firstName", user.getFirstName());
        modelAndView.addObject("lastName", user.getLastName());
        modelAndView.addObject("email", user.getEmail());
        return modelAndView;
    }

    /*
    // Read json request body
    @PostMapping(path="/users")
    @ResponseBody // will return a json
    public ResponseEntity createUser(@RequestBody User user) {
        return new ResponseEntity(
                new UserRest(UUID.randomUUID().toString(), user.getFirstName(), user.getLastName(), user.getEmail()),
                HttpStatus.OK);
    }*/

    @GetMapping(path = "/signup")
    public String signupForm() {
        return "signup"; // signup.html
    }

    @GetMapping(path = "/register/{token}")
    public ModelAndView activateAccount(@PathVariable("token") String token) throws IllegalAccessException, NoSuchFieldException { // @RequestParam pt query params
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> result = restTemplate.getForObject(ACTIVATE_ACCOUNT_URL.replace("{token}", token), Map.class);

        String message = result.containsKey("successful") && result.get("successful").equals(true) ? "Cont validat cu succes!" : "Link invalid";

        ModelAndView modelAndView = new ModelAndView("verification");
        modelAndView.addObject("message", message);
        return modelAndView;
    }
}
