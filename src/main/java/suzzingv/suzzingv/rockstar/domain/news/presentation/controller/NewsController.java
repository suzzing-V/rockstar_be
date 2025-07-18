package suzzingv.suzzingv.rockstar.domain.news.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import suzzingv.suzzingv.rockstar.domain.news.application.NewsService;
import suzzingv.suzzingv.rockstar.domain.news.presentation.dto.res.NewsResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/news")
public class NewsController {

    private final NewsService newsService;

    @GetMapping("/band/{bandId}")
    public ResponseEntity<Page<NewsResponse>> getNewsByBand(@PathVariable Long bandId, @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "20") int size) {
        Page<NewsResponse> responses = newsService.getNewsByBand(bandId, page, size);
        return ResponseEntity.ok(responses);
    }
}
