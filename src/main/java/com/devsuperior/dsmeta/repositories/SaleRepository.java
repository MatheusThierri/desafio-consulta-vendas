package com.devsuperior.dsmeta.repositories;

import com.devsuperior.dsmeta.dto.SaleSummaryProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.devsuperior.dsmeta.entities.Sale;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    @Query("SELECT obj FROM Sale obj " +
           "WHERE (:name IS NULL OR LOWER(obj.seller.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
           "AND obj.date BETWEEN :initDate AND :endDate")
    Page<Sale> searchSales(
            @Param("initDate") LocalDate initDate,
            @Param("endDate") LocalDate endDate,
            @Param("name") String name,
            Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT s2.name AS name, SUM(s.amount) AS amount " +
                                       "FROM tb_sales s " +
                                       "JOIN tb_seller s2 ON s.seller_id = s2.id " +
                                       "WHERE s.date BETWEEN :initDate AND :endDate " +
                                       "GROUP BY s2.name " +
                                       "ORDER BY amount DESC")
    List<SaleSummaryProjection> summary(
            @Param("initDate") LocalDate initDate,
            @Param("endDate") LocalDate endDate
    );

}
