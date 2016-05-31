/**
 * Copyright 2014 Unicon (R)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Modified by hed13nkn
 * Source: https://github.com/azeckoski/lti_starter
 */


package studentcapture.ltistarter;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.h2.server.web.WebServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth.provider.OAuthProcessingFilterEntryPoint;
import org.springframework.security.oauth.provider.token.InMemoryProviderTokenServices;
import org.springframework.security.oauth.provider.token.OAuthProviderTokenServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.frameoptions.AllowFromStrategy;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

import studentcapture.ltistarter.lti.LTIConsumerDetailsService;
import studentcapture.ltistarter.lti.LTIDataService;
import studentcapture.ltistarter.lti.LTIOAuthAuthenticationHandler;
import studentcapture.ltistarter.lti.LTIOAuthProviderProcessingFilter;
import studentcapture.ltistarter.oauth.MyOAuthNonceServices;

@EnableAutoConfiguration
@EnableWebSecurity
@Order(1)
public class LTISecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
    private LTIOAuthProviderProcessingFilter ltioAuthProviderProcessingFilter;
    @Autowired
    LTIDataService ltiDataService;
    @Autowired
    LTIConsumerDetailsService oauthConsumerDetailsService;
    @Autowired
    MyOAuthNonceServices oauthNonceServices;
    @Autowired
    LTIOAuthAuthenticationHandler oauthAuthenticationHandler;
    @Autowired
    OAuthProcessingFilterEntryPoint oauthProcessingFilterEntryPoint;
    @Autowired
    OAuthProviderTokenServices oauthProviderTokenServices;

    @PostConstruct
    public void init() {
    	ltioAuthProviderProcessingFilter = new LTIOAuthProviderProcessingFilter(ltiDataService, oauthConsumerDetailsService, oauthNonceServices, oauthProcessingFilterEntryPoint, oauthAuthenticationHandler, oauthProviderTokenServices);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /**/
    	
      	
    	//Allow showing studentcapture inside a frame. Option is not recognized by Chrome. Use sameorigin if both lms
    	//and tool are on same domain.
    	http.headers().frameOptions().disable();
    	
    	http.headers().addHeaderWriter(new XFrameOptionsHeaderWriter(new AllowFromStrategy() {
    			
    		@Override
    		public String getAllowFromValue(HttpServletRequest arg0) {
    				//return "http://int-nat.cs.umu.se:20003";
    			return "https://grytvante:8443";
    		}
    	}));
    	
    	http.requestMatchers().antMatchers("/lti/**", "/lti2/**").and().addFilterBefore(ltioAuthProviderProcessingFilter, UsernamePasswordAuthenticationFilter.class).
    	authorizeRequests().anyRequest().hasRole("USER").and().csrf().disable();    	
    }
    
    @Bean(name = "oauthProviderTokenServices")
    public OAuthProviderTokenServices oauthProviderTokenServices() {
        // NOTE: we don't use the OAuthProviderTokenServices for 0-legged but it cannot be null
        return new InMemoryProviderTokenServices();
    }
    
    /**
     * Allows access to the H2 console at: {server}/console/
     * Enter this as the JDBC URL: jdbc:h2:~/test
     */
    @Bean
    public ServletRegistrationBean h2servletRegistration() {
    	ServletRegistrationBean registration = new ServletRegistrationBean(new WebServlet());
        registration.addUrlMappings("/console/*");
        registration.addInitParameter("webAllowOthers", "true");
        return registration;
    }    
}



