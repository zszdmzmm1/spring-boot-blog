package com.auefly.spring.boot.security.controller;

import com.auefly.spring.boot.security.entity.Post;
import com.auefly.spring.boot.security.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Controller
public class ResourceController {
    @Autowired
    PostService postService;

    @GetMapping("/resources")
    String index(@RequestParam("page") Optional<Integer> page,
                 @RequestParam("size") Optional<Integer> size,
                 Model model) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);
        Page<Post> optionalPost = postService.findAllResources(currentPage, pageSize);
        model.addAttribute("page", optionalPost);
        return "resource/index";
    }

    @GetMapping("/resources/{id}")
    String show(@PathVariable Long id, Model model) {
        Optional<Post> optionalPost = postService.findById(id);

        if (optionalPost.isEmpty() || !optionalPost.get().isStatus()
                || !"resource".equals(optionalPost.get().getType())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        model.addAttribute("post", optionalPost.get());
        return "resource/show";
    }
}
