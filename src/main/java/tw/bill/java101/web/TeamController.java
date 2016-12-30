package tw.bill.java101.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tw.bill.java101.domain.Team;
import tw.bill.java101.domain.User;
import tw.bill.java101.dto.TeamForm;
import tw.bill.java101.service.TeamService;
import tw.bill.java101.service.UserService;
import tw.bill.java101.service.security.UserDetailsImpl;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class TeamController {
    @Autowired
    private TeamService teamService;

    @Autowired
    private UserService userService;

    @RequestMapping({"/", "/teams"})
	public String listTeams(Model model) {
    	model.addAttribute("teams", teamService.readAll());
        return "team/index";
	}
	
	@RequestMapping(value="/teams/create", method=RequestMethod.GET)
	public String newTeam(Model model){
		model.addAttribute("teamForm", new TeamForm());
		return "team/new";
	}

    @RequestMapping(value="/teams/{id}", method=RequestMethod.GET)
    public String showTeam(@PathVariable long id, Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userService.findByUserName(userDetails.getUsername());

        Team team = teamService.readOne(id);
        model.addAttribute("team", team);
        model.addAttribute("posts", team.getPosts());
        model.addAttribute("isUserInTeam",
                team.getUsers().stream().filter(item -> item.getId()==user.getId()).findAny().isPresent());

        return "team/show";
    }

    @RequestMapping(value="/teams/create", method=RequestMethod.POST)
    public String createTeam(@Valid TeamForm teamForm, BindingResult bindingResult, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if(bindingResult.hasErrors()) {
            return "team/new";
        }

        teamService.create(teamForm, userDetails.getUsername());
        return "redirect:/";
    }

    @RequestMapping(value="/teams/{id}/update", method = RequestMethod.GET)
    public String editTeam(@PathVariable long id, Model model){
        Team team = teamService.readOne(id);
        model.addAttribute("id", id);
        model.addAttribute("teamForm", team);
        return "team/edit";
    }

    @PreAuthorize("@userServiceImpl.canUpdateAndDeleteTeam(principal, #id)")
    @RequestMapping(value="/teams/{id}", method= RequestMethod.PUT)
    public String updateTeam(@PathVariable("id") long id, @Valid TeamForm teamForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "team/edit";
        }

        teamService.update(teamForm);
        return "redirect:/";
    }

    @PreAuthorize("@userServiceImpl.canUpdateAndDeleteTeam(principal, #id)")
	@RequestMapping(value = "/teams/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteTeam(@PathVariable long id) {
        teamService.delete(id);
	}
}
