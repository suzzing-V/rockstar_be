package suzzingv.suzzingv.rockstar.domain.news.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import suzzingv.suzzingv.rockstar.domain.news.application.NewsService;
import suzzingv.suzzingv.rockstar.domain.news.presentation.dto.res.NewsResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/news")
public class NewsController {

    private final NewsService newsService;

    @GetMapping("/band/{bandId}")
    public ResponseEntity<List<NewsResponse>> getNewsByBand(@PathVariable Long bandId) {
        List<NewsResponse> responses = newsService.getNewsByBand(bandId);
        return ResponseEntity.ok(responses);
    }
}
