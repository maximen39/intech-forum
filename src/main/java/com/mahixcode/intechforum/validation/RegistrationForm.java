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
package com.mahixcode.intechforum.validation;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author maximen39
 */
@Getter
@Setter
public class RegistrationForm {

    @NotNull(message = "Enter username")
    @Size(min = 3, max = 24, message = "Username must be between 3 and 24 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username must follow the pattern [A-Za-z0-9_]")
    private String username = "";

    @NotNull(message = "Enter first name")
    @Size(min = 2, max = 60, message = "First name must be between 2 and 60 characters")
    private String firstName = "";

    @NotNull(message = "Enter last name")
    @Size(min = 2, max = 60, message = "Last name must be between 2 and 60 characters")
    private String lastName = "";

    @NotNull(message = "Enter password")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%])(?=\\S+$).{8,}$", message =
            "Password must contain at least one uppercase letter, a lowercase letter," +
                    " a number, and one of the characters !@#$% and minimum 8 characters")
    private String password;

    private String repeatPassword;
}
