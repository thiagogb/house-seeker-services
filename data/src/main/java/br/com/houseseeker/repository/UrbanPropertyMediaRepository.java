package br.com.houseseeker.repository;

import br.com.houseseeker.entity.Provider;
import br.com.houseseeker.entity.UrbanPropertyMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrbanPropertyMediaRepository extends JpaRepository<UrbanPropertyMedia, Integer>, UrbanPropertyMediaExtendedRepository {

    @Query("""
            FROM UrbanPropertyMedia urbanPropertyMedia
            JOIN FETCH urbanPropertyMedia.urbanProperty urbanProperty
            JOIN FETCH urbanProperty.provider provider
            WHERE provider = :provider
            """)
    List<UrbanPropertyMedia> findAllByProvider(@Param("provider") Provider provider);

}