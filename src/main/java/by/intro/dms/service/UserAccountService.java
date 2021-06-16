package by.intro.dms.service;

import by.intro.dms.controller.UserAccountController;
import by.intro.dms.model.UserAccount;
import by.intro.dms.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserAccountService {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserAccountService() {
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
}
