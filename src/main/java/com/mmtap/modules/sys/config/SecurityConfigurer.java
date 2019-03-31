package com.mmtap.modules.sys.config;

import com.mmtap.modules.sys.config.security.AuthenticationFailureHandler;
import com.mmtap.modules.sys.config.security.AuthenticationSuccessHandler;
import com.mmtap.modules.sys.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

/**
 * @author mmtap.com
 * @date 2019/1/12
 **/
@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {

    private UserRepository userRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/app/**", "/plugins/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .successHandler(new AuthenticationSuccessHandler()).failureHandler(new AuthenticationFailureHandler())
                .loginProcessingUrl("/login")
                .loginPage("/login").permitAll()
                .and()
                .headers()
                .addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
                .and()
                .csrf().disable().cors();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("[" + username + "]用户不存在"));
    }

    /**
     * 默认使用 bcrypt 加密
     *
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
