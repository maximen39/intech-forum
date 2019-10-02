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
package com.mahixcode.intechforum.service;

import com.mahixcode.intechforum.entity.ForumUser;
import com.mahixcode.intechforum.model.UserModel;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

/**
 * @author maximen39
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final ForumUserService forumUserService;

    public UserDetailsServiceImpl(ForumUserService forumUserService) {
        this.forumUserService = forumUserService;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<UserModel> userModelOptional = forumUserService.findUserByName(s);
        if (!userModelOptional.isPresent()) {
            throw new UsernameNotFoundException("User not found!");
        }

        return detailsFromModel(userModelOptional.get());
    }

    private UserDetails detailsFromModel(UserModel userModel) {
        return new ForumUser(
                userModel.getUsername(),
                userModel.getPassword(),
                Collections.singleton(userModel.getRole()),
                userModel.getId()
        );
    }
}
