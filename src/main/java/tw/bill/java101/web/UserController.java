package tw.bill.java101.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import tw.bill.java101.domain.*;
import tw.bill.java101.dto.UserEditForm;
import tw.bill.java101.dto.UserForm;
import tw.bill.java101.service.UserService;
import tw.bill.java101.service.security.UserDetailsImpl;
import tw.bill.java101.validator.UserCreateFormValidator;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by bill33 on 2016/2/14.
 */

@Controller
public class UserController {
    Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private UserCreateFormValidator userCreateFormValidator;

    @InitBinder("userForm")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(userCreateFormValidator);
    }

    @ModelAttribute("allSex")
    public List<Sex> populateSexes() {
        return Arrays.asList(Sex.ALL);
    }

    @ModelAttribute("allDegree")
    public List<Degree> populateDegrees() {
        return Arrays.asList(Degree.ALL);
    }

    @ModelAttribute("allHobby")
    public List<Hobby> populateHobby() {
        return Arrays.asList(Hobby.ALL);
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView getLoginPage(@RequestParam Optional<String> error) {
        return new ModelAndView("/user/login", "error", error);
    }

    @RequestMapping(value="/signup", method=RequestMethod.GET)
    public ModelAndView getSignupPage() {
        return new ModelAndView("/user/signup", "userForm", new UserForm());
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String creatUser(@Valid @ModelAttribute("userForm") UserForm userForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "/user/signup";
        }

        try{
            userService.create(userForm);
        } catch (DataIntegrityViolationException e) {
            bindingResult.reject("mail.exists", "Email already exists");
            return "/user/signup";
        }

        return "redirect:/";
    }

    @RequestMapping(value = "/user/edit", method = RequestMethod.GET)
    public String editUser(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userService.findByUserName(userDetails.getUsername());
        model.addAttribute("editUser", user);

        return "/user/edit";
    }

    @RequestMapping(value = "/user/edit", method = RequestMethod.POST)
    public String updateUser(@Valid @ModelAttribute("editUser") UserEditForm userEditForm, BindingResult bindingResult) {
        log.info("enter...");
        if(bindingResult.hasErrors()) {
            log.info("there has error!!!");
            return "/user/edit";
        }

        userService.update(userEditForm);
        return "redirect:/";
    }

    @RequestMapping(value = "/users/join/{teamId}")
    public ModelAndView joinTeam(@PathVariable long teamId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.joinTeam(userDetails.getUsername(), teamId);
        return new ModelAndView("redirect:/teams/" + teamId);
    }

    @RequestMapping(value = "/users/quit/{teamId}")
    public ModelAndView quitTeam(@PathVariable long teamId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.quitTeam(userDetails.getUsername(), teamId);
        return new ModelAndView("redirect:/teams/" + teamId);
    }

    @RequestMapping("/user/teams")
    public ModelAndView fetchTeams(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userService.findByUserName(userDetails.getUsername());
        List<Team> teams = user.getTeams().stream()
                .sorted((x, y) -> {return x.getPosts().size() - x.getPosts().size();})
                .collect(Collectors.toList());
        return new ModelAndView("/user/team", "teams", teams);
    }

    @RequestMapping("/user/posts")
    public ModelAndView fetchPosts(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userService.findByUserName(userDetails.getUsername());
        List<Post> posts = user.getTeams().stream()
                .map(item -> item.getPosts())
                .flatMap(item -> item.stream())
                .sorted((x, y) -> x.getDateCreated().compareTo(y.getDateCreated()))
                .collect(Collectors.toList());
        return new ModelAndView("/user/post", "posts", posts);
    }
}
