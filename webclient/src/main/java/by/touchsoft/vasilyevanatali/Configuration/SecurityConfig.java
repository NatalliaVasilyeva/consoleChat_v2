package by.touchsoft.vasilyevanatali.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * @author Natali
 * Class SucurityConfig
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    /**
     * Configure root for different users to has pages
     *
     * @param http - http request
     * @throws Exception IO
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http

                .authorizeRequests()

                .antMatchers("/api/register/agent{name}", "/api/register/client{name}").permitAll()

                .antMatchers("/signUp/**").permitAll()

                .antMatchers("/api/**").permitAll()

                .antMatchers("/static/**").permitAll()

                .anyRequest().authenticated()

                .and()

                .formLogin()

                .usernameParameter("login")

                .defaultSuccessUrl("/web")

                .loginPage("/login")

                .permitAll()

                .and()

                .rememberMe()

                .rememberMeParameter("remember-me");


        http.csrf().disable();

    }


    /**
     *
     * @param auth - client or agent for authentication
     * @throws Exception - exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("client").password(encoder().encode("clientPass")).roles("CLIENT")
                .and()
                .withUser("agent").password(encoder().encode("agentPass")).roles("AGENT");
    }


    /**
     * Password encoder
     * @return Password
     */
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

}