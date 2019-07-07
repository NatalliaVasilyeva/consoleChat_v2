package by.touchsoft.vasilyevanatali.Configuration;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


/**
 * @author Natali
 * Class SucurityConfig
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    /**
     * Configure root for different users to has pages
     * @param http - http request
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http

                .authorizeRequests()

                .antMatchers("/api/agent/**").hasAuthority("AGENT")

                .antMatchers("/api/client/**").hasAuthority("CLIENT")

                .antMatchers("/signUp/**").permitAll()

                .antMatchers("api/").authenticated()

                .antMatchers("/css/**").permitAll()

                .anyRequest().authenticated()

                .and()

                .formLogin()

                .usernameParameter("login")

                .defaultSuccessUrl("/api/")

                .loginPage("/login")

                .permitAll()

                .and()

                .rememberMe()

                .rememberMeParameter("remember-me");


        http.csrf().disable();

    }


}