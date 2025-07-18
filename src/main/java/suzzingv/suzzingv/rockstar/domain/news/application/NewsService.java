package suzzingv.suzzingv.rockstar.domain.news.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import suzzingv.suzzingv.rockstar.domain.news.domain.entity.News;
import suzzingv.suzzingv.rockstar.domain.news.domain.enums.NewsType;
import suzzingv.suzzingv.rockstar.domain.news.infrastructure.NewsRepository;
import suzzingv.suzzingv.rockstar.domain.news.presentation.dto.res.NewsResponse;

import java.time.LocalDateTime;

import static suzzingv.suzzingv.rockstar.global.util.DateUtil.toMMDDEHHMM;

@Service
@RequiredArgsConstructor
@Transactional
public class NewsService {

    private final NewsRepository newsRepository;

    public void createNews(Long bandId, Long scheduleId, NewsType newsType, LocalDateTime startDate) {
        String content = toMMDDEHHMM(startDate);
        News news = News.builder()
                .newsType(newsType)
                .scheduleId(scheduleId)
                .bandId(bandId)
                .content(content)
                .build();
        newsRepository.save(news);
        // TODO: 푸시알림
        // TODO: 읽음 데이터 추가
    }

    public void createNews(Long bandId, Long scheduleId, NewsType newsType, LocalDateTime oldStartDate, LocalDateTime newStartDate) {
        String content = toMMDDEHHMM(oldStartDate) + "\n-> " + toMMDDEHHMM(newStartDate);
        News news = News.builder()
                .newsType(newsType)
                .scheduleId(scheduleId)
                .bandId(bandId)
                .title(newsType.getMessage())
                .content(content)
                .build();
        newsRepository.save(news);
    }

    @Transactional(readOnly = true)
    public Page<NewsResponse> getNewsByBand(Long bandId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<News> byBandId = newsRepository.findByBandId(bandId, pageable);
        return byBandId.map(NewsResponse::from);
    }
}
