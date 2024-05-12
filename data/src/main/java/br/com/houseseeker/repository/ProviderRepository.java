package br.com.houseseeker.repository;

import br.com.houseseeker.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Integer>, ProviderExtendedRepository {
}