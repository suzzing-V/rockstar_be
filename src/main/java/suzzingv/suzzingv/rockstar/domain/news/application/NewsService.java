package suzzingv.suzzingv.rockstar.domain.news.application;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import suzzingv.suzzingv.rockstar.domain.news.domain.entity.News;
import suzzingv.suzzingv.rockstar.domain.news.domain.enums.NewsType;
import suzzingv.suzzingv.rockstar.domain.news.infrastructure.NewsRepository;
import suzzingv.suzzingv.rockstar.domain.news.presentation.dto.res.NewsResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

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
        String content = toMMDDEHHMM(oldStartDate) + " -> " + toMMDDEHHMM(newStartDate);
        News news = News.builder()
                .newsType(newsType)
                .scheduleId(scheduleId)
                .bandId(bandId)
                .content(content)
                .build();
        newsRepository.save(news);
    }

    @Transactional(readOnly = true)
    public List<NewsResponse> getNewsByBand(Long bandId) {
        List<News> byBandId = newsRepository.findByBandId(bandId);
        return byBandId.stream().map(NewsResponse::from).toList();
    }
}
