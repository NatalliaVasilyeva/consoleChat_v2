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

                .antMatchers("/api/agent/**").hasAuthority("AGENT")

                .antMatchers("/api/client/**").hasAuthority("CLIENT")

                .antMatchers("/signUp/**").permitAll()

                .antMatchers("/api/**").authenticated()

                .antMatchers("/static/**").permitAll()

                .anyRequest().authenticated()

                .and()

                .formLogin()

                .usernameParameter("login")

                .defaultSuccessUrl("/")

                .loginPage("/login")

                .permitAll()

                .and()

                .rememberMe()

                .rememberMeParameter("remember-me");


        http.csrf().disable();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("client").password(encoder().encode("clientPass")).roles("CLIENT")
                .and()
                .withUser("agent").password(encoder().encode("agentPass")).roles("AGENT");
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

}