package by.intro.dms.model;

public enum Permission {
    ACCOUNTS_READ("accounts:read"),
    ACCOUNTS_WRITE("accounts:write");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
