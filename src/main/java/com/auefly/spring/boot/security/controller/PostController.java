package com.auefly.spring.boot.security.controller;

import com.auefly.spring.boot.security.entity.Post;
import com.auefly.spring.boot.security.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Controller
public class PostController {
    @Autowired
    PostService postService;

    @GetMapping("/posts")
    String index(Model model,
                 @RequestParam("page") Optional<Integer> page,
                 @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);
        Page<Post> pageContent = postService.findAllPosts(currentPage, pageSize);
        model.addAttribute("page", pageContent);
        return "post/index";
    }

    @GetMapping("/posts/{id}")
    String post(@PathVariable Long id, Model model) {
        Optional<Post> optionalPost = postService.findById(id);

        if (optionalPost.isEmpty() || !optionalPost.get().isStatus()
                || !"post".equals(optionalPost.get().getType())) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(404));
        }

        model.addAttribute("post", optionalPost.get());
        return "post/show";
    }
}
