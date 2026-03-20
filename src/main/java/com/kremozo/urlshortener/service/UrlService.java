package com.kremozo.urlshortener.service;

import org.springframework.stereotype.Service;
import com.kremozo.urlshortener.model.UrlEntity;
import com.kremozo.urlshortener.repository.UrlRepository;
import java.util.Optional;

@Service
public class UrlService {
    private static final String BASE62_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final UrlRepository urlRepository;
    
    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    //Actions
    private String encodeBase62(Long id){
        StringBuilder shortCodeBuilder = new StringBuilder();

        if(id==0){
            return String.valueOf(BASE62_CHARACTERS.charAt(0));
        }
        while(id>0){
            int remainder = (int)(id%62);
            shortCodeBuilder.append(BASE62_CHARACTERS.charAt(remainder));
            id/=62;
        }
        return shortCodeBuilder.reverse().toString();
    }
    public String shortenUrl(String originalUrl){
        // 1. Save with a temporary placeholder to force the database to generate an ID
        String tempPlaceholder = "TEMP-" + System.nanoTime();
        UrlEntity urlEntity = new UrlEntity(originalUrl, tempPlaceholder);
        urlEntity = urlRepository.save(urlEntity); 

        String shortCode = encodeBase62(urlEntity.getId());

        urlEntity.setShortCode(shortCode);
        urlRepository.save(urlEntity);

        return shortCode;
    }
    public Optional<String> getOriginalUrl(String shortCode){
        return urlRepository.findByShortCode(shortCode).map(entity -> entity.getOriginalUrl());
    }
}
