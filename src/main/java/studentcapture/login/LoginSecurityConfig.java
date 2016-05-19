package studentcapture.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
 
/**
 * Spring Security Login configuration.
 * @author c13hbd, dv11osi
 */
@Configuration
@EnableWebSecurity
public class LoginSecurityConfig extends WebSecurityConfigurerAdapter{
    
	@Autowired
	private LoginAuthentication loginAuth;
	
    //Set username, password and role
    //The role can be used to allow role-specific actions
    //Current roles are "USER" and "ADMIN".
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(loginAuth).eraseCredentials(false);
    }
    
    
    
    //Custom login configuration. Currently redirects to "/login" when not logged in
    //Allows for custom login.html
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http.authorizeRequests().antMatchers("/css/**", "/src/**", "/images/**").permitAll();
    	http.authorizeRequests().antMatchers("/users").permitAll();
    	http.authorizeRequests().antMatchers("/login**").permitAll();
        http
        	.sessionManagement().maximumSessions(1)
        	.and().invalidSessionUrl("/login");
        http
            .authorizeRequests()
                .antMatchers("/**").access("hasRole('USER')")
                .antMatchers("/loggedin").access("hasRole('USER') or hasRole('ADMIN')") //Users and admins can access /loggedin
                .antMatchers("/admin").access("hasRole('ADMIN')") //Admins can access /admin
                .antMatchers("/login").permitAll()
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
                //.successHandler(customLoginHandler) //Custom redirection based on role
                .defaultSuccessUrl("/index", true) //always redirect to "/index"
                .failureUrl("/login?error=loginerror")
                .and()
            .logout()
                .logoutUrl("/logout")
                .permitAll()
                .and()
            .csrf().disable(); //CSRF Disabled for now
    }

}
