package com.app.miniIns.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import static com.app.miniIns.security.SecurityConstants.*;

@Configuration
@EnableWebSecurity
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {


    @Autowired
    private MyAuthenticationProvider authProvider;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.addFilter(new JWTAuthenticationFilter(authProvider))
//                .httpBasic();
//        http.addFilter(new JWTAuthenticationFilter(authProvider)).authorizeRequests()
//                .antMatchers("/").permitAll()
//             .antMatchers("/register").permitAll()
//                .anyRequest().authenticated();

        http.csrf().disable().authorizeRequests()
                //.antMatchers("/css/**", "/js/**", "/images/**", "/static/**", "/**/favicon.ico").permitAll()
                .antMatchers(HttpMethod.POST, "/login","/register").permitAll()
                //.antMatchers("/rest/*").permitAll()
                //.antMatchers("/").permitAll()
                .anyRequest().authenticated()
        .and()
        .addFilter(new JWTAuthenticationFilter(authProvider));
    }



//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests().antMatchers(HOME_URL, SIGN_UP_URL).permitAll()
//
//                .and()
//
//                .authorizeRequests().antMatchers("/{username}/**", "/{username}").authenticated()
//
//                .and()
//
//                .formLogin()
//                .loginPage(LOGIN_URL)
//                .permitAll()
//                .and()
//
//                .addFilter(new JWTAuthenticationFilter(new MyAuthenticationProvider()));
////
////                .addFilter(new JWTAuthorizationFilter(authenticationManager()))
////                // this disables session creation on Spring Security
////                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//    }



//    @Override
//    public void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
//    }

//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
//        return source;
//    }
}
