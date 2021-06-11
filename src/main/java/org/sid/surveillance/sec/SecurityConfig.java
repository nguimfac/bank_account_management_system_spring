package org.sid.surveillance.sec;

import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation. *;
import org.springframework.security.config.annotation.authentication.builders. *;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration. *;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.Md4PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import sun.security.provider.MD5;

import javax.sql.DataSource;
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
       auth.inMemoryAuthentication().withUser("admin").password("{noop}1234").roles("ADMIN","USER");
        auth.inMemoryAuthentication().withUser("user").password("{noop}1234").roles("USER");

    }






    @Override
    protected void configure(HttpSecurity http) throws Exception {
       http.formLogin().loginPage("/login");
       http.authorizeRequests().antMatchers("/operations","/consulterCompte","/client","/newCompte").hasRole("USER");
       http.authorizeRequests().antMatchers("/saveOperation").hasRole("ADMIN");
       http.exceptionHandling().accessDeniedPage("/403");

    }
}
