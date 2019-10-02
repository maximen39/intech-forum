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
package com.mahixcode.intechforum.utils;

import com.mahixcode.intechforum.entity.ForumUser;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * @author maximen39
 */
public class SpringWebUtils {

    public static boolean isAuth() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        return authentication != null
                && !(authentication instanceof AnonymousAuthenticationToken)
                && authentication.isAuthenticated();
    }

    public static Optional<ForumUser> getUser() {
        if (!isAuth()) {
            return Optional.empty();
        }
        return Optional.of((ForumUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }
}
