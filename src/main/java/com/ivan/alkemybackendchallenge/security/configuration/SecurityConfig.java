package com.ivan.alkemybackendchallenge.security.configuration;

import com.ivan.alkemybackendchallenge.security.filter.CustomAuthenticationFilter;
import com.ivan.alkemybackendchallenge.security.filter.CustomAuthorizationFilter;
import com.ivan.alkemybackendchallenge.security.service.JwtService;
import com.ivan.alkemybackendchallenge.security.utility.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsService)
                .passwordEncoder(this.passwordEncoder);
    }

    // The ORDER of the stuff in this method MATTERS.
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter( this.authenticationManagerBean(), this.jwtService );
        // Setting the custom login path.
        customAuthenticationFilter.setFilterProcessesUrl(SecurityConstants.Endpoint.LOGIN);
        // Disabling csrf, needed for session cookie authentication, since a token-based authentication protocol
        // is going to be used instead.
        http.csrf().disable();
        // Setting the state quality as stateless, disabling the use of sessions for security context.
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // Allowing requests to pass through without authentication for certain endpoints.
        http.authorizeRequests().antMatchers(
                SecurityConstants.Endpoint.LOGIN + "/**",
                SecurityConstants.Endpoint.REGISTER + "/**"
            ).permitAll();
        // Allowing requests to pass through only when the authenticated user possesses certain roles/authorities/credentials.
        http.authorizeRequests().antMatchers(
                HttpMethod.GET,
                SecurityConstants.Endpoint.USERS + "/**"
            ).hasAnyAuthority(SecurityConstants.DEFAULT_ADMIN_ROLE);
        // Allowing the rest of the requests not specified above to pass through for any authenticated user.
        // Also blocking every request for unauthenticated users.
        http.authorizeRequests().anyRequest().authenticated();
        // Setting the custom filters.
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore( new CustomAuthorizationFilter(this.jwtService), UsernamePasswordAuthenticationFilter.class );

    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

} // class end.
