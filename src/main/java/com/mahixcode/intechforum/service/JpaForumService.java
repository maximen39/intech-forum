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

import com.mahixcode.intechforum.jpa.MessageDao;
import com.mahixcode.intechforum.jpa.ThreadDao;
import com.mahixcode.intechforum.jpa.UserDao;
import com.mahixcode.intechforum.model.MessageModel;
import com.mahixcode.intechforum.model.ThreadModel;
import com.mahixcode.intechforum.model.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author maximen39
 */
@Service
@Transactional(readOnly = true)
public class JpaForumService implements ForumUserService, ForumThreadService {

    private final UserDao userDao;
    private final ThreadDao threadDao;
    private final MessageDao messageDao;

    public JpaForumService(UserDao userDao, ThreadDao threadDao, MessageDao messageDao) {
        this.userDao = userDao;
        this.threadDao = threadDao;
        this.messageDao = messageDao;
    }

    @Override
    public Optional<UserModel> findUserByName(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public boolean hasUserByName(String username) {
        return userDao.existsByUsername(username);
    }

    @Override
    @Transactional
    public long saveUser(UserModel userModel) {
        return userDao.save(userModel).getId();
    }

    @Override
    public Page<ThreadModel> findThreads(Pageable pageable) {
        return threadDao.findAllSortable(pageable);
    }

    @Override
    public Optional<ThreadModel> findThreadById(long id) {
        return threadDao.findById(id);
    }

    @Override
    @Transactional
    public void deleteThreadById(long id) {
        threadDao.deleteById(id);
    }

    @Override
    @Transactional
    public long saveThread(ThreadModel threadModel) {
        return threadDao.save(threadModel).getId();
    }

    @Override
    public Page<MessageModel> findMessages(Pageable pageable) {
        return messageDao.findAll(pageable);
    }

    @Override
    @Transactional
    public Optional<MessageModel> findMessage(long id) {
        return messageDao.findById(id);
    }

    @Override
    @Transactional
    public void deleteMessageById(long id) {
        messageDao.deleteById(id);
    }

    @Override
    @Transactional
    public long saveMessage(MessageModel messageModel) {
        return messageDao.save(messageModel).getId();
    }
}
