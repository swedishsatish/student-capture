package studentcapture.login;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
 
/**
 * Spring Security Login configuration.
 * @author c13hbd, dv11osi
 */
@Configuration
@EnableWebSecurity
public class LoginSecurityConfig extends WebSecurityConfigurerAdapter{
    
	@Autowired
	private LoginAuthentication loginAuth;
	
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    /**
	 * Configures the authentication manager for login page.
	 * Uses the custom made LoginAuthentication.java 
	 * <p> 
	 * @see LoginAuthentication
	 * @param auth
	 * @throws Exception
	 */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)  {
        auth.authenticationProvider(loginAuth).eraseCredentials(false);
    }
    
    /**
     * Configure access to different resources.
     * Sets default login page and handles access for different users. 
     * 
     * <p>
     * CSRF provides additional security,
     * CSRF is disabled for now because of compatibility issues.
     * <p>
     * @param http, a https request from a potential user.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //Global access required for fonts/images/style and login services.
    	http
    	    .authorizeRequests()
        	    .antMatchers("/css/**", "/src/**", "/images/**").permitAll()
        	    .antMatchers("/login**").permitAll()
        	    .antMatchers("/register").permitAll()
        	    .antMatchers("/lostPassword").permitAll()
        	    .antMatchers("/changePassword").permitAll();
    	
        //Add session management.
    	http
        	.sessionManagement().maximumSessions(1)
        	.and().invalidSessionUrl("/login");
        
    	//Configure spring security login accessibility.
    	http
            .authorizeRequests()
                .antMatchers("/**").access("hasRole('USER')")
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
                .defaultSuccessUrl("/index", true)
                .failureUrl("/login?error=loginerror")
                .and()
            .logout().addLogoutHandler(new LogoutHandler() {
				
				@Override
				public void logout(HttpServletRequest arg0, HttpServletResponse arg1,
						Authentication arg2) {
					FileWriter fw;
					try {
						fw = new FileWriter("stresstest.txt", true);
						BufferedWriter bw = new BufferedWriter(fw);
				    	PrintWriter out = new PrintWriter(bw);
				    	out.append("LOGOUT EVENT:::" + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME) + " | Username " + arg0.getSession().getAttribute("username") + "\n");
				    	out.close();
					} catch (IOException e) {
						System.out.println("ERROR WHEN WRITING TO FILE");
						e.printStackTrace();
					}
				}
			});
                http.logout().logoutUrl("/logout")
                .permitAll()
                .and()
            .csrf().disable(); //CSRF Disabled for now.
    	                        
    }

}
