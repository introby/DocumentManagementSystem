package by.intro.dms.model;

import by.intro.dms.validation.UniqueUsername;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "user_account")
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long accountId;

    @NotEmpty(message = "Username should not be empty")
    @Size(min = 3, max = 16, message = "Username should be between 3 and 16 characters")
    @UniqueUsername(message = "This username already exists")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Username should have only a-z and A-Z characters")
    @Column(name = "username")
    private String username;

    @NotEmpty(message = "Password should not be empty")
    @Size(min = 3, max = 255, message = "Password should be between 3 and 255 characters")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?!.*\\s).*$",
            message = "Password should have only a-z, A-Z and 0-9 characters. Minimum one digit and one letter")
    @Column(name = "password")
    private String password;

    @NotEmpty(message = "First name should not be empty")
    @Size(min = 1, max = 16, message = "First name should be between 1 and 16 characters")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "First name should have only a-z and A-Z characters")
    @Column(name = "first_name")
    private String firstName;

    @NotEmpty(message = "Last name should not be empty")
    @Size(min = 1, max = 16, message = "Last name should be between 1 and 16 characters")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Last name should have only a-z and A-Z characters")
    @Column(name = "last_name")
    private String lastName;

    @Column(name = "role")
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private Status status;

    @Column(name = "created_at")
    private LocalDate createdAt;

    public UserAccount(String username, String password, String firstName, String lastName, Role role, Status status) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.status = status;
    }

    public UserAccount() {
    }
}
