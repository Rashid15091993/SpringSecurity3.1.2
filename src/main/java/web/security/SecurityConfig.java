package web.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.CharacterEncodingFilter;
import web.security.handler.SuccessUserHandler;
import web.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final SuccessUserHandler loginSuccessHandler;

    public SecurityConfig(UserService userService,
                          SuccessUserHandler loginSuccessHandler){
        this.userService = userService;
        this.loginSuccessHandler = loginSuccessHandler;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //Нам нужно добавить CharacterEncodingFilter перед фильтрами, которые читают свойства запроса в первый раз.
        // Есть securityFilterChain (стоит вторым. после metrica filter) , и мы можем добавить наш фильтр внутри него.
        // Первый фильтр (внутри цепочки безопасности), который читает свойства,
        // - это CsrfFilter, поэтому мы помещаем перед ним CharacterEncodingFilter.
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        http.addFilterBefore(filter, CsrfFilter.class);

        http.formLogin()
//                // указываем страницу с формой логина
////                .loginPage("/login")
                //указываем логику обработки при логине
                .successHandler(loginSuccessHandler)
//                // указываем action с формы логина
//                .loginProcessingUrl("/login")
//                // Указываем параметры логина и пароля с формы логина
//                .usernameParameter("j_username")
//                .passwordParameter("j_password")
                // даем доступ к форме логина всем
                .permitAll();

        http.logout()
                // разрешаем делать логаут всем
                .permitAll()
                // указываем URL логаута
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                // указываем URL при удачном логауте
                //.logoutSuccessUrl("/login?logout")
                .logoutSuccessUrl("/")
                //выключаем кросс-доменную секьюрность (на этапе обучения неважна)
                .and().csrf().disable(); //- попробуйте выяснить сами, что это даёт


        http.authorizeRequests() // делаем страницу регистрации недоступной для авторизированных пользователей

                .antMatchers("/hello/**").permitAll() // доступность всем

                //страница аутентификации доступна всем
                .antMatchers("/login").anonymous()
                // защищенные URL

                //.antMatchers("/user/**").access("hasAnyRole('USER')") // разрешаем входить на /user пользователям с ролью User
                //.antMatchers("/admin/**").access("hasRole('ADMIN')")
                //.antMatchers("/vip/**").access("hasRole('VIP')")

//                .antMatchers("/user/**").hasRole("USER")
//                .antMatchers("/admin/**").hasRole("ADMIN")
//                .antMatchers("/vip/**").hasRole("VIP")

                .antMatchers("/user/**").hasAnyAuthority("ADMIN", "USER")
                .antMatchers("/admin/**").hasAuthority("ADMIN")
                .antMatchers("/vip/**").hasAuthority("VIP")
                .anyRequest().authenticated();

    }

    // Необходимо для шифрования паролей
    // В данном примере не используется, отключен
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
