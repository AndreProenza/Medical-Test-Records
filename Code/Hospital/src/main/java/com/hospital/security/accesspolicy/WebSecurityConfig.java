package com.hospital.security.accesspolicy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new CitizenDetailsServiceImplementation();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setPasswordEncoder(bCryptPasswordEncoder);
		authProvider.setUserDetailsService(userDetailsService());
		return authProvider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/home").hasAnyAuthority("Admin", "Doctor", "Nurse", "Porter", "Volunteer",
					"Patient_Assistant", "Clinical_Assistant", "Ward_Clerk", "Patient")
			.antMatchers("/medical/records/**").hasAnyAuthority("Doctor", "Nurse")
			.antMatchers("/my/medical/record").hasAnyAuthority("Admin", "Doctor", "Nurse", "Porter", "Volunteer",
					"Patient_Assistant", "Clinical_Assistant", "Ward_Clerk", "Patient")
			.antMatchers("/patient/medical/record").hasAnyAuthority("Doctor", "Nurse")
			.antMatchers("/patient/register").hasAnyAuthority("Ward_Clerk")
			.antMatchers("/patient/register").hasAnyAuthority("admin/register")
			.anyRequest().authenticated()
			.and()
			.formLogin().loginPage("/login").permitAll()
			.defaultSuccessUrl("/home", true)
			.and()
			.logout().logoutUrl("/logout")
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")).logoutSuccessUrl("/login");
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**", "/static/**", "/CSS/**", "/IMAGES/**", "/JS/**");
	}
	
	
	
//	  "q0y&BGysjT34uNSh" -> "12345678"
//    "&O$Y05e0m5Ac9a%v" -> "14267653"
//    "EK&4$?5%Rdc1?n0&" -> "16732784"
//    "7*5Q%H5R1k9$t5C9" -> "11256472"
//    "d0p7&9w&433jPP1g" -> "12221749"	
}
