package br.com.houseseeker.repository;

import br.com.houseseeker.entity.Scanner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScannerRepository extends JpaRepository<Scanner, Integer> {
}