package jwt.template

import org.apache.commons.lang.RandomStringUtils

class RegistrationRequest {
    String requestId  // a UUID that will be sent as part of the email to the user

    /* Could use the challengeId to strengthen the process, it's a kind of one time pin
    when the user makes the registration request they are shown this generated string,
    then they are challenged for it when they click on the link in the email
    */
    String challengeId
    transient String displayChallengeId
    Date dateCreated

    static belongsTo = [user: User]

    static constraints = {
        challengeId nullable: true, blank: false
    }

    static String generateChallengeId(int nrChars) {
        // todo: could move the count to config
        RandomStringUtils.random(nrChars, true, true)
    }

    def beforeInsert() {
        requestId = UUID.randomUUID().toString()
        // Originally I thought it a good idea to hash this but really...not required?
        /*println "challengeId: ${challengeId}"
        if (challengeId) {
            challengeId = challengeId.encodeAsSHA256()
            log.info "Updated challengeId: ${challengeId}"
        }*/
    }


    @Override
    public String toString() {
        return "RegistrationRequest{" +
                "id=" + id +
                ", version=" + version +
                ", user=" + user +
                ", requestId='" + requestId + '\'' +
                ", challengeId='" + challengeId + '\'' +
                ", dateCreated=" + dateCreated +
                '}';
    }
}
