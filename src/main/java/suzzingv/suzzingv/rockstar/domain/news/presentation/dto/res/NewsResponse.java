package suzzingv.suzzingv.rockstar.domain.news.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;
import suzzingv.suzzingv.rockstar.domain.news.domain.entity.News;

import static suzzingv.suzzingv.rockstar.global.util.DateUtil.toMMDDHHMM;

@Getter
@Builder
public class NewsResponse {

    private Long scheduleId;

    private String newsType;

    private String content;

    private String createdDateTime;

    public static NewsResponse from(News news) {
        return NewsResponse.builder()
                .scheduleId(news.getScheduleId())
                .newsType(news.getNewsType().getMessage())
                .content(news.getContent())
                .createdDateTime(toMMDDHHMM(news.getCreatedAt()))
                .build();
    }
}
