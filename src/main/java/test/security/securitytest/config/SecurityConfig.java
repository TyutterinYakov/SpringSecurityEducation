package test.security.securitytest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import test.security.securitytest.model.Role;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final UserDetailsService userDetailsService;
	
	
	@Autowired
	public SecurityConfig(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService) {
		super();
		this.userDetailsService = userDetailsService;
	}
	

	@Override
	protected void configure(HttpSecurity http) throws Exception { //SPRING CONFIG
		
		http
		.csrf().disable()
		.authorizeRequests()
		.antMatchers("/").permitAll()
		.anyRequest()
		.authenticated()
		.and()
		.formLogin()
		.loginPage("/auth/login").permitAll()
		.defaultSuccessUrl("/auth/success")
		.and()
		.logout()
		.logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout", "POST"))
		.invalidateHttpSession(true)
		.clearAuthentication(true)
		.deleteCookies("JSESSIONID")
		.logoutSuccessUrl("/auth/login");
	}

//	@Bean
//	@Override
//	protected UserDetailsService userDetailsService() { ///CREATE USER
//		
//		return new InMemoryUserDetailsManager(
//			User.builder()
//				.username("admin")
//				.password(passwordEncoder().encode("admin"))
//				.authorities(Role.ADMIN.getAuthorities())
//				.build(),
//			User.builder()
//				.username("user")
//				.password(passwordEncoder().encode("user"))
//				.authorities(Role.USER.getAuthorities())
//				.build()
//				);
//	}
	
	
	
	@Bean
	protected PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12);
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authProvider());
	}


	@Bean
	protected DaoAuthenticationProvider authProvider() {
		DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
		daoProvider.setPasswordEncoder(passwordEncoder());
		daoProvider.setUserDetailsService(userDetailsService);
		return daoProvider;
	}
}