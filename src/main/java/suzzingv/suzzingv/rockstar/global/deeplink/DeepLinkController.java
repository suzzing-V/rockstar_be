package suzzingv.suzzingv.rockstar.global.deeplink;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class DeepLinkController {

    @GetMapping(
            value = "/.well-known/apple-app-site-association",
            produces = "application/json"
    )
    public ResponseEntity<Resource> serve() throws IOException {
        Resource resource = new ClassPathResource("static/.well-known/apple-app-site-association");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(resource);
    }
}
