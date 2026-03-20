package com.kremozo.urlshortener.controller;

import com.kremozo.urlshortener.service.UrlService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/url")
public class urlController {

    private final UrlService urlService;

    public urlController(UrlService urlService){
        this.urlService=urlService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<?> shortenUrl(@RequestBody Map<String,String> request){
        String originalUrl = request.get("originalUrl");
        if(originalUrl==null||originalUrl.isEmpty()){
            return ResponseEntity.badRequest().body(Map.of("error","originalUrl is required"));
        }
        String shortCode = urlService.shortenUrl(originalUrl);
        String shortUrl = "http://localhost:8080/api/url/"+shortCode;
        return ResponseEntity.ok(Map.of("shortUrl",shortUrl));
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode){
        return urlService.getOriginalUrl(shortCode).map(originalUrl -> {
                    return ResponseEntity.status(HttpStatus.FOUND)
                                         .location(URI.create(originalUrl))
                                         .<Void>build();
                }).orElseGet(() -> {
                    return ResponseEntity.notFound().build();
                });
    } 
}