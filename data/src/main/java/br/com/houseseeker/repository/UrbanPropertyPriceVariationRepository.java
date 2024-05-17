package br.com.houseseeker.repository;

import br.com.houseseeker.entity.Provider;
import br.com.houseseeker.entity.UrbanPropertyPriceVariation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrbanPropertyPriceVariationRepository extends JpaRepository<UrbanPropertyPriceVariation, Integer> {

    @Query("""
            FROM UrbanPropertyPriceVariation urbanPropertyPriceVariation
            JOIN FETCH urbanPropertyPriceVariation.urbanProperty urbanProperty
            JOIN FETCH urbanProperty.provider provider
            WHERE provider = :provider
            ORDER BY urbanProperty.id, urbanPropertyPriceVariation.analysisDate
            """)
    List<UrbanPropertyPriceVariation> findAllByProvider(@Param("provider") Provider provider);

    @Modifying
    @Query("""
            DELETE FROM UrbanPropertyPriceVariation urbanPropertyPriceVariation
            WHERE urbanPropertyPriceVariation.urbanProperty IN (
                SELECT urbanProperty
                FROM UrbanProperty urbanProperty
                WHERE urbanProperty.provider = :provider
            )
            """)
    int deleteAllByProvider(@Param("provider") Provider provider);

}