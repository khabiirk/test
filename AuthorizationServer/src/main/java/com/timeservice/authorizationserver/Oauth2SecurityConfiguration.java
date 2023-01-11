/**
 * Original source: http://websystique.com/spring-security/secure-spring-rest-api-using-oauth2/
 */

package com.timeservice.authorizationserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenStoreUserApprovalHandler;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

/**
 * Security configuration class where the endpoint /login is exposed
 * and where the resource owners are configured.
 */
@Configuration
@EnableWebSecurity
public class Oauth2SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired
  private ClientDetailsService clientDetailsService;

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Function that configures the resource owners for in memory authentication.
   *
   * @param auth AuthenticationManagerBuilder object to manage in memory authentication.
   */
  @Autowired
  public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {

    auth.inMemoryAuthentication()
        .withUser("max")
        .password(passwordEncoder().encode("abc123"))
        .roles("ADMIN", "TIMEUSER")
        .and()
        .withUser("moritz")
        .password(passwordEncoder().encode("abc123"))
        .roles("TIMEUSER");
  }


  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable()
        .authorizeRequests()
        .antMatchers("/").permitAll()
        .antMatchers("/login").permitAll()
        .anyRequest().authenticated()
        .and()
        .formLogin().permitAll()
        .and()
        .logout()
        .permitAll()


    //http.csrf().disable()

    ;

  }

  @Override
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }


  @Bean
  public TokenStore tokenStore() {
    return new InMemoryTokenStore();
  }

  /**
   * Method that store approval decisions.
   *
   * @param tokenStore OAuth token
   * @return a TokenStoreUserApprovalHandler object that remembers approval decisions.
   */
  @Bean
  @Autowired
  public TokenStoreUserApprovalHandler userApprovalHandler(TokenStore tokenStore) {
    TokenStoreUserApprovalHandler handler = new TokenStoreUserApprovalHandler();
    handler.setTokenStore(tokenStore);
    handler.setRequestFactory(new DefaultOAuth2RequestFactory(clientDetailsService));
    handler.setClientDetailsService(clientDetailsService);
    return handler;
  }

  @Bean
  @Autowired
  public ApprovalStore approvalStore(TokenStore tokenStore) throws Exception {
    TokenApprovalStore store = new TokenApprovalStore();
    store.setTokenStore(tokenStore);
    return store;
  }

}
