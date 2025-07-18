package suzzingv.suzzingv.rockstar.domain.news.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import suzzingv.suzzingv.rockstar.domain.news.domain.enums.NewsType;

import java.time.LocalDateTime;

@Entity
@Table(name = "news_TB")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long bandId;

    private Long scheduleId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NewsType newsType;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    private News(Long bandId, Long scheduleId, String content, NewsType newsType, String title) {
        this.bandId = bandId;
        this.scheduleId = scheduleId;
        this.content = content;
        this.newsType = newsType;
        this.title = title;
    }
}
