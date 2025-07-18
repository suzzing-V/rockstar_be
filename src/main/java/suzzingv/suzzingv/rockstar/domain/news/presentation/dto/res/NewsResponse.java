package suzzingv.suzzingv.rockstar.domain.news.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;
import suzzingv.suzzingv.rockstar.domain.news.domain.entity.News;
import suzzingv.suzzingv.rockstar.domain.news.domain.enums.NewsType;

import static suzzingv.suzzingv.rockstar.global.util.DateUtil.toMMDDHHMM;

@Getter
@Builder
public class NewsResponse {

    private Long scheduleId;

    private NewsType newsType;

    private String title;

    private String content;

    private String createdDateTime;

    public static NewsResponse from(News news) {
        return NewsResponse.builder()
                .scheduleId(news.getScheduleId())
                .newsType(news.getNewsType())
                .title(news.getTitle())
                .content(news.getContent())
                .createdDateTime(toMMDDHHMM(news.getCreatedAt()))
                .build();
    }
}
