package br.com.houseseeker.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrbanPropertyMediaRepository extends JpaRepository<UrbanPropertyMedia, Integer> {

    @Query("""
            FROM UrbanPropertyMedia urbanPropertyMedia
            JOIN FETCH urbanPropertyMedia.urbanProperty urbanProperty
            JOIN FETCH urbanProperty.provider provider
            WHERE provider = :provider
            """)
    List<UrbanPropertyMedia> findAllByProvider(@Param("provider") Provider provider);

}