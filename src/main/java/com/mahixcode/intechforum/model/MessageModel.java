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

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Calendar;

/**
 * @author maximen39
 */
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "message")
@Entity
public class MessageModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JoinColumn(name = "author_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private UserModel author;

    @JoinColumn(name = "thread_id", nullable = false)
    @ManyToOne
    private ThreadModel thread;

    @Column(name = "message", length = 1000, nullable = false)
    private String message;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Calendar createdAt;
}
