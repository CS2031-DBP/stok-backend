package com.example.stokapp.sale.infrastructure;

import com.example.stokapp.sale.domain.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.ZonedDateTime;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    Page<Sale> findAllByOwnerId(Long ownerId, Pageable pageable);
    List<Sale> findByOwnerIdAndCreatedAtBetween(Long ownerId, ZonedDateTime startDate, ZonedDateTime endDate);

    List<Sale> findByOwnerId(Long ownerId);

    @Query("SELECT s FROM Sale s WHERE s.owner.id = :ownerId AND YEAR(s.createdAt) = :year")
    List<Sale> findByOwnerIdAndYear(Long ownerId, int year);
}
