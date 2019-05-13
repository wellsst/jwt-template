package jwt.template

class User {

    String username
    String loginRequestId
    Date loginRequestedOn
    String token

    static constraints = {
        username email: true, nullable: false, blank: false, unique: true
        token nullable: true
        loginRequestId nullable: true
        loginRequestedOn nullable: true
    }
}
