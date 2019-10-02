/*
 * Copyright (C) 2018 maximen39
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
package com.mahixcode.intechforum.config;

import com.mahixcode.intechforum.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import java.net.URLEncoder;

/**
 * @author maximen39
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public WebSecurityConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/register/**", "/auth/**").permitAll();
        http.authorizeRequests().antMatchers("/thread/delete/**").hasAuthority("admin");
        http.authorizeRequests().antMatchers("/**").authenticated();

        http.authorizeRequests().and()
                .exceptionHandling()
                .accessDeniedPage("/auth").and()
                .csrf().and()
                .formLogin()
                .loginProcessingUrl("/auth/action")
                .loginPage("/auth")
                .defaultSuccessUrl("/", true)
                .failureUrl("/auth?error=" + URLEncoder.encode("Incorrect username or password!", "UTF-8")).and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/auth").and()
                .rememberMe()
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(24 * 60 * 60).and()
                .sessionManagement()
                .maximumSessions(1);
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        return new InMemoryTokenRepositoryImpl();
    }
}
