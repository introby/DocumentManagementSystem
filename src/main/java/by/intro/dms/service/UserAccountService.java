package by.intro.dms.service;

import by.intro.dms.controller.UserAccountController;
import by.intro.dms.exception.ApiRequestException;
import by.intro.dms.model.UserAccount;
import by.intro.dms.model.dto.AuthenticationRequestDto;
import by.intro.dms.repository.UserAccountRepository;
import by.intro.dms.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public UserAccountService(UserAccountRepository userAccountRepository,
                              PasswordEncoder passwordEncoder,
                              AuthenticationManager authenticationManager,
                              JwtTokenProvider jwtTokenProvider) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public UserAccount findById(Long accountId) {
        Optional<UserAccount> uaop = userAccountRepository.findById(accountId);
        return uaop.get();
    }

    public List<UserAccount> findAll() {
        return userAccountRepository.findAll();
    }

    public boolean saveUserAccount(UserAccount userAccount) {

        if (!userAccount.getPassword().equals(UserAccountController.oldPassword)) {
            userAccount.setPassword(passwordEncoder.encode(userAccount.getPassword()));
        }

        userAccount.setCreatedAt(LocalDate.now());
        userAccountRepository.save(userAccount);
        return true;
    }

//    @ExceptionHandler(ApiRequestException.class)
    public String authenticate(AuthenticationRequestDto request) {

        try {
            String login = request.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),
                    request.getPassword()));
            UserAccount user = userAccountRepository.findByUsername(login)
                    .orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
            return jwtTokenProvider.createToken(request.getUsername(), user.getRole().name());

        } catch (AuthenticationException e) {
            throw new ApiRequestException("Authentication error");
        }
    }
}
