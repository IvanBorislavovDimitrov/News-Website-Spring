package app.dtos.privilege_dtos;

public class ChangePrivilegesDto {

    private boolean admin;
    private boolean user;

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isUser() {
        return user;
    }

    public void setUser(boolean user) {
        this.user = user;
    }
}
