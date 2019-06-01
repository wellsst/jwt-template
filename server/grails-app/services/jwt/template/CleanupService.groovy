package jwt.template

import grails.gorm.transactions.Transactional
import groovy.time.TimeCategory
import org.springframework.scheduling.annotation.Scheduled

@Transactional
class CleanupService extends BaseService {

    boolean lazyInit = false

    // final String cleanupDelaySeconds = getAppConfigValue('cleanupDelaySeconds', 15)
    // https://issues.apache.org/jira/browse/GROOVY-3278
    // TODO: 5 minutes, need to move to config, can do this: "${cleanupDelaySeconds}000" under groovy 3.x
    @Scheduled(fixedDelay = 300000L, initialDelay = 30000L)
    void executeEveryXX() {
        log.info "Removing old registration requests..."
        List<RegistrationRequest> candidatesToDelete
        use (TimeCategory) {
            Date deleteBeforeDate = new Date() - 5.minutes
            candidatesToDelete = RegistrationRequest.findAllByDateCreatedLessThan(deleteBeforeDate)
        }
        log.info("Removing ${candidatesToDelete.size()} Registration Requests")
        RegistrationRequest.deleteAll(candidatesToDelete)
    }
}
