package br.com.houseseeker.repository;

import br.com.houseseeker.entity.Provider;
import br.com.houseseeker.entity.UrbanProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrbanPropertyRepository extends JpaRepository<UrbanProperty, Integer>, UrbanPropertyExtendedRepository {

    @Query("""
            FROM UrbanProperty urbanProperty
            JOIN FETCH urbanProperty.provider provider
            WHERE provider = :provider
            """)
    List<UrbanProperty> findAllByProvider(@Param("provider") Provider provider);

    @Modifying
    int deleteAllByProvider(@Param("provider") Provider provider);

}