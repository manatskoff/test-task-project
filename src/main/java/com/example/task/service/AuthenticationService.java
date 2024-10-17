package com.example.task.service;

import com.example.task.dto.JwtAuthenticationResponse;
import com.example.task.dto.SignInRequest;
import com.example.task.dto.SignUpRequest;
import com.example.task.entities.*;
import com.example.task.repository.EmailDataRepository;
import com.example.task.repository.PhoneDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailDataRepository emailDataRepository;
    private final PhoneDataRepository phoneDataRepository;

    /**
     * Регистрация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    public JwtAuthenticationResponse signUp(SignUpRequest request) {

        var user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.ROLE_USER);

        var account = new Account();
        account.setBalance(request.getInitialBalance());
        account.setInitialBalance(request.getInitialBalance());
        user.setAccount(account);

        var phoneData = new PhoneData();
        phoneData.setPhone(request.getPhone());
        phoneData.setUser(user);

        var mailData = new EmailData();
        mailData.setEmail(request.getEmail());
        mailData.setUser(user);

        user.setPhoneData(List.of(phoneData));
        user.setEmailData(List.of(mailData));

        userService.create(user);

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }

    /**
     * Аутентификация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    public JwtAuthenticationResponse signIn(SignInRequest request) {

        String username = null;

        var optionalEmailData = emailDataRepository.findByEmail(request.getPhoneOrEmail());
        if (optionalEmailData.isPresent())
            username = optionalEmailData.get().getUser().getUsername();

        if (username == null) {
            var optionalPhoneData = phoneDataRepository.findByPhone(request.getPhoneOrEmail());
            if (optionalPhoneData.isPresent())
                username = optionalPhoneData.get().getUser().getUsername();
        }


        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                username,
                request.getPassword()
        ));

        var user = userService
                .userDetailsService()
                .loadUserByUsername(username);

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }
}