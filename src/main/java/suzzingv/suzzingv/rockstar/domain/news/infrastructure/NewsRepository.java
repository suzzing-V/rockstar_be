package suzzingv.suzzingv.rockstar.domain.news.infrastructure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import suzzingv.suzzingv.rockstar.domain.news.domain.entity.News;

public interface NewsRepository extends JpaRepository<News, Long> {
    Page<News> findByBandId(Long bandId, Pageable pageable);

    void deleteByBandId(Long bandId);
}
