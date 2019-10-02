/*
 * Copyright (C) 2019 maximen39
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mahixcode.intechforum.controller;

import com.mahixcode.intechforum.entity.ForumUser;
import com.mahixcode.intechforum.model.MessageModel;
import com.mahixcode.intechforum.model.ThreadModel;
import com.mahixcode.intechforum.model.UserModel;
import com.mahixcode.intechforum.service.ForumThreadService;
import com.mahixcode.intechforum.utils.SpringWebUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;
import java.util.function.Function;

/**
 * @author maximen39
 */
@Controller
public class MainController {

    private final ForumThreadService forumThreadService;

    public MainController(ForumThreadService forumThreadService) {
        this.forumThreadService = forumThreadService;
    }

    @GetMapping({"/index", "/"})
    public ModelAndView index(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 8);
        Page<ThreadModel> page = forumThreadService.findThreads(pageable);

        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("pages", page.getTotalPages());
        modelAndView.addObject("threads", page.getContent());
        modelAndView.addObject("currentPage", pageNumber);
        modelAndView.addObject("user", (ForumUser) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal()
        );
        return modelAndView;
    }

    @PostMapping("/thread/create")
    public ModelAndView createThread(@ModelAttribute("name") String name) {
        Optional<ForumUser> optionalForumUser = SpringWebUtils.getUser();
        if (optionalForumUser.isPresent()) {
            ThreadModel threadModel = new ThreadModel()
                    .setAuthor(new UserModel().setId(optionalForumUser.get().getId()))
                    .setName(name);
            long threadId = forumThreadService.saveThread(threadModel);
            return new ModelAndView(String.format("redirect:/thread/%s", threadId));
        }
        return new ModelAndView("redirect:/");
    }

    @GetMapping("/thread/{id}")
    public ModelAndView thread(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                               @PathVariable long id) {
        Optional<ThreadModel> optionalThreadModel = forumThreadService.findThreadById(id);
        Function<ThreadModel, ModelAndView> function = threadModel -> {
            Pageable pageable = PageRequest.of(pageNumber - 1, 6);
            Page<MessageModel> page = forumThreadService.findMessages(pageable);

            ModelAndView modelAndView = new ModelAndView("thread");
            modelAndView.addObject("thread", threadModel);
            modelAndView.addObject("messages", page.getContent());
            modelAndView.addObject("pages", page.getTotalPages());
            modelAndView.addObject("currentPage", pageNumber);
            modelAndView.addObject("user", (ForumUser) SecurityContextHolder
                    .getContext().getAuthentication().getPrincipal()
            );
            return modelAndView;
        };

        return optionalThreadModel.map(function)
                .orElseGet(() -> new ModelAndView("redirect:/"));
    }

    @PostMapping("/thread/{id}")
    public ModelAndView sendMessage(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                                    @PathVariable long id, @ModelAttribute("message") String message) {
        if (!message.trim().isEmpty()) {
            Optional<ForumUser> optionalForumUser = SpringWebUtils.getUser();
            if (optionalForumUser.isPresent()) {
                MessageModel messageModel = new MessageModel()
                        .setAuthor(new UserModel().setId(optionalForumUser.get().getId()))
                        .setThread(new ThreadModel().setId(id))
                        .setMessage(message);
                forumThreadService.saveMessage(messageModel);
            }
        }

        return new ModelAndView(String.format("redirect:/thread/%s?page=%s", id, pageNumber));
    }

    @PostMapping("/thread/delete/{id}")
    public ModelAndView deleteThread(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                                     @PathVariable long id) {
        forumThreadService.deleteThreadById(id);
        return new ModelAndView(String.format("redirect:/?page=%s", pageNumber));
    }

    @PostMapping("/message/delete/{id}")
    public ModelAndView deleteMessage(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                                      @RequestParam(value = "thread") int thread, @PathVariable long id) {
        Optional<ForumUser> optionalForumUser = SpringWebUtils.getUser();
        optionalForumUser.ifPresent(forumUser -> {
            if (forumUser.getAuthorities().stream().anyMatch(grantedAuthority ->
                    grantedAuthority.getAuthority().equals("admin"))) {
                forumThreadService.deleteMessageById(id);
            } else {
                Optional<MessageModel> message = forumThreadService.findMessage(id);
                message.ifPresent(messageModel -> {
                    if (messageModel.getAuthor().getId() == forumUser.getId()) {
                        forumThreadService.deleteMessageById(id);
                    }
                });
            }
        });
        return new ModelAndView(String.format("redirect:/thread/%s?page=%s", thread, pageNumber));
    }
}
