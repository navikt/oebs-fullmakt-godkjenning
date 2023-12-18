package no.nav.oebs.fullmakt.db.repository;

import no.nav.oebs.fullmakt.db.entity.FullmaktEntity;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(propagation = Propagation.REQUIRES_NEW)
public interface FullmaktRepository
        extends JpaRepository<FullmaktEntity, Long> {
}


