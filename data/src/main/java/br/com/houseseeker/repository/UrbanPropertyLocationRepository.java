package br.com.houseseeker.repository;

import br.com.houseseeker.entity.Provider;
import br.com.houseseeker.entity.UrbanPropertyLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrbanPropertyLocationRepository extends JpaRepository<UrbanPropertyLocation, Integer> {

    @Query("""
            FROM UrbanPropertyLocation urbanPropertyLocation
            JOIN FETCH urbanPropertyLocation.urbanProperty urbanProperty
            JOIN FETCH urbanProperty.provider provider
            WHERE provider = :provider
            """)
    List<UrbanPropertyLocation> findAllByProvider(@Param("provider") Provider provider);

    @Modifying
    @Query("""
            DELETE FROM UrbanPropertyLocation urbanPropertyLocation
            WHERE urbanPropertyLocation.urbanProperty IN (
                SELECT urbanProperty
                FROM UrbanProperty urbanProperty
                WHERE urbanProperty.provider = :provider
            )
            """)
    int deleteAllByProvider(@Param("provider") Provider provider);

}