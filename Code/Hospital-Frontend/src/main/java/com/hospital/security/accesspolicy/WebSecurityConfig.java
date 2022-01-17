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
			.antMatchers("/admin/register").hasAnyAuthority("Admin")
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
	
//Joana Ferreira -> Admin
//16478937 -> Q9$88twJBd1%3G0!
	
//Filipe Santos -> Doctor
//17593826 -> e$C18d!2$&*SzX5L
	
//Eliana Joana -> Nurse
//17584937 -> K@69f6&&LdqoX!za
	
//Henrique Jota -> Porter
//17564920 -> 7u$%4n&B90%8U!6!
	
//Madalena Afonso -> Volunteer
//17564532 -> *Mj0T**p*?Z!yv0u

//Alexandre Pinto -> Patient_Assistant
//14789078 -> I&u5lhmt*nO$f?!1

//Mariana Rita -> Clinical_Assistant
//18509738 -> p$h4M$*x*cHk10q@
	
//Hélder Costa -> Ward_Clerk
//15378965 -> V322!!P25KM4&f6b
	
//Amanda Júlio -> Patient
//17645234 -> pB0K!*vF85!0&@60 

}
