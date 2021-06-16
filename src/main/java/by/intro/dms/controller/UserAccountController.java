package by.intro.dms.controller;

import by.intro.dms.model.Status;
import by.intro.dms.model.UserAccount;
import by.intro.dms.repository.UserAccountRepository;
import by.intro.dms.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
public class UserAccountController {

    public static String oldPassword = null;
    public static String oldUsername = null;

    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private UserAccountRepository userAccountRepository;

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('accounts:write')")
    public String redirectToAdmin(UserAccount userAccount) {
        return "admin";
    }

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('accounts:write')")
    public String findAll(Model model) {
        List<UserAccount> userAccountList = userAccountService.findAll();
        model.addAttribute("userAccountList", userAccountList);
        return "user/user";
    }

    @GetMapping("/user/new")
    @PreAuthorize("hasAuthority('accounts:write')")
    public String createUserForm(UserAccount userAccount) {
        return "user/new";
    }

    @PostMapping("/user/new")
    @PreAuthorize("hasAuthority('accounts:write')")
    public String createUserAccount(@Valid UserAccount userAccount, BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            return "user/new";

        userAccountService.saveUserAccount(userAccount);
        return "redirect:/user";
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("hasAuthority('accounts:write')")
    public String viewAccount(@PathVariable("id") Long id, Model model) {
        UserAccount userAccount = userAccountService.findById(id);
        model.addAttribute("userAccount", userAccount);
        return "user/view";
    }

    @GetMapping("/user/{id}/edit")
    @PreAuthorize("hasAuthority('accounts:write')")
    public String editUserForm(@PathVariable("id") Long id, Model model) {
        UserAccount userAccount = userAccountService.findById(id);
        oldPassword = userAccount.getPassword();
        oldUsername = userAccount.getUsername();
        model.addAttribute("userAccount", userAccount);
        return "user/edit";
    }

    @PostMapping("/user/{id}/edit")
    @PreAuthorize("hasAuthority('accounts:write')")
    public String editUser(@Valid UserAccount userAccount, BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            return "user/edit";

        userAccountService.saveUserAccount(userAccount);
        return "redirect:/user";
    }

    @PostMapping("/user/{id}/lock")
    @PreAuthorize("hasAuthority('accounts:write')")
    public String lockUnlockUser(@PathVariable("id") Long id) {
        UserAccount userAccount = userAccountService.findById(id);
        if (userAccount.getStatus().equals(Status.ACTIVE)) {
            userAccount.setStatus(Status.INACTIVE);
        } else {
            userAccount.setStatus(Status.ACTIVE);
        }
        userAccountService.saveUserAccount(userAccount);
        return "redirect:/user/{id}";
    }

}
