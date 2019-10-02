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
package com.mahixcode.intechforum.model;

import com.mahixcode.intechforum.Role;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * @author maximen39
 */

@Getter
@Setter
@Accessors(chain = true)
@Table(name = "user")
@Entity
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "username", length = 24, nullable = false, unique = true)
    private String username;

    @Column(name = "first_name", length = 60, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 60, nullable = false)
    private String lastName;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "role", nullable = false, columnDefinition = "tinyint(1)")
    private Role role;

    @Column(name = "password", nullable = false)
    private String password;
}

