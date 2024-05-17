package br.com.houseseeker.repository;

import br.com.houseseeker.entity.Provider;
import br.com.houseseeker.entity.UrbanPropertyMeasure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrbanPropertyMeasureRepository extends JpaRepository<UrbanPropertyMeasure, Integer> {

    @Query("""
            FROM UrbanPropertyMeasure urbanPropertyMeasure
            JOIN FETCH urbanPropertyMeasure.urbanProperty urbanProperty
            JOIN FETCH urbanProperty.provider provider
            WHERE provider = :provider
            """)
    List<UrbanPropertyMeasure> findAllByProvider(@Param("provider")Provider provider);

    @Modifying
    @Query("""
            DELETE FROM UrbanPropertyMeasure urbanPropertyMeasure
            WHERE urbanPropertyMeasure.urbanProperty IN (
                SELECT urbanProperty
                FROM UrbanProperty urbanProperty
                WHERE urbanProperty.provider = :provider
            )
            """)
    int deleteAllByProvider(@Param("provider") Provider provider);

}