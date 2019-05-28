package jwt.template

class User {

    String username
    String loginToken

    static hasOne = [registrationRequest:RegistrationRequest]

    static constraints = {
        username email: true, nullable: false, blank: false, unique: true
        loginToken nullable: true
        registrationRequest nullable: true
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", version=" + version +
                ", username='" + username + '\'' +
                ", loginToken='" + loginToken + '\'' +
                '}';
    }
}
