package suzzingv.suzzingv.rockstar.domain.news.infrastructure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import suzzingv.suzzingv.rockstar.domain.news.domain.entity.News;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    Page<News> findByBandId(Long bandId, Pageable pageable);
}
