package com.realestate.security;

import com.realestate.security.authentication.ApplicationUserService;
import com.realestate.security.config.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.concurrent.TimeUnit;

import static com.realestate.security.ApplicationUserPermission.REST_ALL;
import static com.realestate.security.ApplicationUserRole.*;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final SecurityConfig securityConfig;
    private final ApplicationUserService applicationUserService;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder,
                                     SecurityConfig securityConfig, ApplicationUserService applicationUserService) {
        this.passwordEncoder = passwordEncoder;
        this.securityConfig = securityConfig;
        this.applicationUserService = applicationUserService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(applicationUserService);
        return daoAuthenticationProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers( "/", "/h2-console/**","/app/v1/all/**", "/facilities", "css/*", "js/*", "/webjars/**").permitAll()
                .antMatchers("/app/v1/address/**", "/app/v1/apartment/**", "/app/v1/house/**", "/clients").hasAnyRole(ADMIN.name(), AGENT.name())
                .antMatchers("/app/v1/basement/**", "/app/v1/garage/**", "/app/v1/storage/**", "/client/**").hasAnyRole(ADMIN.name(), AGENT.name())
                .antMatchers("/agent/**", "/agents").hasAnyRole(ADMIN.name(), CLIENT.name())
                .antMatchers("/realEstateAgent/**").hasRole(ADMIN.name())
                .antMatchers(HttpMethod.DELETE, "/api/**").hasAuthority(REST_ALL.getPermission())
                .antMatchers(HttpMethod.POST, "/api/**").hasAuthority(REST_ALL.getPermission())
                .antMatchers(HttpMethod.PUT, "/api/**").hasAuthority(REST_ALL.getPermission())
                .antMatchers(HttpMethod.GET, "/api/**").hasAnyRole(ADMIN.name(), USER.name())
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .defaultSuccessUrl("/facilities", true)
                .passwordParameter("password")
                .usernameParameter("username")
                .and()
                .rememberMe()
                .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))
                .key(securityConfig.getSecretKey())
                .rememberMeParameter("remember-me")
                .and()
                .logout()
                .logoutUrl("/logout")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "remember-me")
                .logoutSuccessUrl("/login");
    }

}
