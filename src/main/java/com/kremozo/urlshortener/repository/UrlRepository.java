package com.kremozo.urlshortener.repository;

import com.kremozo.urlshortener.model.UrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<UrlEntity,Long>{
    Optional<UrlEntity> findByShortCode(String shortCode);
}
