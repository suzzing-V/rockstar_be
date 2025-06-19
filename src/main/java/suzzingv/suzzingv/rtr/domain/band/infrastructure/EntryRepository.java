package suzzingv.suzzingv.rtr.domain.band.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import suzzingv.suzzingv.rtr.domain.band.domain.entity.Entry;

@Repository
public interface EntryRepository extends JpaRepository<Entry, Long> {

}
