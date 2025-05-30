package com.example.quicklink;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/url")
@CrossOrigin
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<UrlDto> shortenUrl(@Valid @RequestBody UrlDto urlDto) {
        UrlDto response = urlService.shortenUrl(urlDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode, HttpServletResponse response) {
        Url url = urlService.getUrl(shortCode);

        if (url == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "URL not found");
        }

        urlService.incrementClickCount(shortCode);

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(url.getOriginalUrl()))
                .build();
    }

    @GetMapping("/info/{shortCode}")
    public ResponseEntity<Url> getUrlInfo(@PathVariable String shortCode) {
        Url url = urlService.getUrl(shortCode);
        if (url == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(url);
    }

    @GetMapping("/info/all")
    public ResponseEntity<List<Url>> getAllUrlInfo() {
        List<Url> url = urlService.getAllUrl();
        if (url == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(url);
    }

}
