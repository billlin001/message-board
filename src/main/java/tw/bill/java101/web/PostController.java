package tw.bill.java101.web;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tw.bill.java101.domain.Post;
import tw.bill.java101.domain.User;
import tw.bill.java101.dto.PostForm;
import tw.bill.java101.service.PostService;
import tw.bill.java101.service.security.UserDetailsImpl;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;

/**
 * Created by Bill Lin on 2016/2/9.
 */

@Controller
@RequestMapping("/teams/{teamId}/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String newPost(@PathVariable("teamId") long teamId, Model model) {
        model.addAttribute("teamId", teamId);
        model.addAttribute("postForm", new PostForm());
        return "post/new";
    }

    @RequestMapping(value = "/{postId}/update", method = RequestMethod.GET)
    public String editPost(@PathVariable("teamId") long teamId,
                           @PathVariable("postId") long postId,
                           Model model) {
        Post post = postService.readOne(postId);
        model.addAttribute("postForm", post);
        model.addAttribute("teamId", teamId);
        model.addAttribute("postId", postId);
        return "post/edit";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createPost(@PathVariable("teamId") long teamId,
                             @Valid PostForm postForm, BindingResult bindingResult,
                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if(bindingResult.hasErrors()) {
            return "post/new";
        }

        Post post = postService.create(teamId, postForm, userDetails.getUsername());
        return "redirect:/teams/" + teamId;
    }

    @PreAuthorize("@userServiceImpl.canUpdateAndDeletePost(principal, #postId)")
    @RequestMapping(value = "/{postId}", method = RequestMethod.PUT)
    public String updatePost(@PathVariable("teamId") long teamId, @PathVariable("postId") long postId,
                             @Valid PostForm postForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "post/edit";
        }
        postService.update(postForm);
        return "redirect:/teams/" + teamId;
    }

    @PreAuthorize("@userServiceImpl.canUpdateAndDeletePost(principal, #postId)")
    @RequestMapping(value = "/{postId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable("teamId") long teamId, @PathVariable("postId") long postId) {
        postService.delete(postId);
    }
}
