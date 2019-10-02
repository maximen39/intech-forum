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

import com.mahixcode.intechforum.Role;
import com.mahixcode.intechforum.model.UserModel;
import com.mahixcode.intechforum.service.ForumUserService;
import com.mahixcode.intechforum.validation.RegistrationForm;
import com.mahixcode.intechforum.validation.RegistrationFormValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

/**
 * @author maximen39
 */
@Controller
public class AuthController {

    private final ForumUserService forumUserService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(ForumUserService forumUserService, PasswordEncoder passwordEncoder) {
        this.forumUserService = forumUserService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/auth")
    public ModelAndView auth(@RequestParam(value = "error", required = false) String error) {
        ModelAndView model = new ModelAndView("auth");
        if (error != null) {
            model.addObject("error", error);
        }
        return model;
    }

    @GetMapping("/register")
    public ModelAndView register() {
        return new ModelAndView("register", "registrationForm", new RegistrationForm());
    }

    @PostMapping("/register")
    public ModelAndView registerAction(@Valid RegistrationForm form, BindingResult bindingResult) {
        ModelAndView model = new ModelAndView("register");
        model.addObject("registrationForm", form);

        if (bindingResult.hasErrors()) {
            model.addAllObjects(bindingResult.getModel());
            return model;
        }

        if (this.forumUserService.hasUserByName(form.getUsername())) {
            bindingResult.rejectValue("username", "", "User with this username already exists");
            model.addAllObjects(bindingResult.getModel());
            return model;
        }

        String encodedPassword = passwordEncoder.encode(form.getPassword());

        UserModel userModel = new UserModel()
                .setUsername(form.getUsername())
                .setFirstName(form.getFirstName())
                .setLastName(form.getLastName())
                .setRole(Role.USER)
                .setPassword(encodedPassword);

        this.forumUserService.saveUser(userModel);

        return new ModelAndView("redirect:login");
    }

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.addValidators(new RegistrationFormValidator());
    }
}
