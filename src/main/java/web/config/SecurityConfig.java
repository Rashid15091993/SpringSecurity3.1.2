package web.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import web.config.handler.SuccessUserHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private final SuccessUserHandler successUserHandler;
	private final UserDetailsService myUserDetailsService;

	public SecurityConfig(UserDetailsService myUserDetailsService, SuccessUserHandler successUserHandler) {
		this.myUserDetailsService = myUserDetailsService;
		this.successUserHandler = successUserHandler;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/admin/**").access("hasRole('ADMIN')")
				.antMatchers("/user/**").access("hasAnyRole('USER')")
				.and().formLogin()  //login configuration
				.loginPage("/customLogin.jsp")
				.loginProcessingUrl("/appLogin")
				.usernameParameter("username")
				.passwordParameter("password")
				.successHandler(successUserHandler)
				.defaultSuccessUrl("/user")
				.and().logout()  //logout configuration
				.logoutUrl("/appLogout")
				.logoutSuccessUrl("/customLogin.jsp")
				.and().exceptionHandling() //exception handling configuration
				.accessDeniedPage("/error");// Spring сам подставит свою логин форму

	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder;
	}
}