package by.intro.dms.service;

import by.intro.dms.model.Role;
import by.intro.dms.model.Status;
import by.intro.dms.model.UserAccount;
import by.intro.dms.repository.UserAccountRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserAccountServiceTest {

    @Autowired
    private UserAccountService userAccountService;

    @MockBean
    private UserAccountRepository userAccountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void saveUserAccount() {

        UserAccount userAccount = new UserAccount(
                "TestAccount", "testpassword1", "TestName", "TestLastName",
                Role.USER, Status.ACTIVE);

        boolean isUserAccountCreated = userAccountService.saveUserAccount(userAccount);

        Assert.assertTrue(isUserAccountCreated);
        Assert.assertTrue(userAccount.getCreatedAt().equals(LocalDate.now()));
        Mockito.verify(userAccountRepository, Mockito.times(1)).save(userAccount);
    }

}