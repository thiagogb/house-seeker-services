package br.com.houseseeker.repository;

import br.com.houseseeker.entity.Provider;
import br.com.houseseeker.entity.UrbanPropertyConvenience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrbanPropertyConvenienceRepository extends JpaRepository<UrbanPropertyConvenience, Integer> {

    @Query("""
            FROM UrbanPropertyConvenience urbanPropertyConvenience
            JOIN FETCH urbanPropertyConvenience.urbanProperty urbanProperty
            JOIN FETCH urbanProperty.provider provider
            WHERE provider = :provider
            """)
    List<UrbanPropertyConvenience> findAllByProvider(@Param("provider") Provider provider);

}