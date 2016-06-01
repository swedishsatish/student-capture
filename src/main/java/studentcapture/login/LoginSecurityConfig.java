package studentcapture.login;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.header.writers.frameoptions.AllowFromStrategy;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
 
/**
 * Spring Security Login configuration.
 * @author c13hbd, dv11osi
 */
@Configuration
@EnableWebSecurity
public class LoginSecurityConfig extends WebSecurityConfigurerAdapter{
    
	@Autowired
	private LoginAuthentication loginAuth;
	
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
        
    	http.headers().frameOptions().disable();
    	
    	http.headers().addHeaderWriter(new XFrameOptionsHeaderWriter(new AllowFromStrategy() {
			
			@Override
			public String getAllowFromValue(HttpServletRequest arg0) {
				//return "http://int-nat.cs.umu.se:20003";
				return "https://grytvante:8443";
			}
		}));
    	    	
    	//Global access required for fonts/images/style and login services.
    	http
    	    .authorizeRequests()
    	    	//register page for lti 2
    	     	.antMatchers("/lti/register/**").permitAll()
    	     	//console for h2 database settings
    	     	.antMatchers("/console/**").permitAll()
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
            .logout().logoutUrl("/logout")
                .permitAll()
                .and()
            .csrf().disable(); //CSRF Disabled for now.
    	                        
    }

}
