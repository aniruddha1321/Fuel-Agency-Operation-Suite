package com.faos.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.faos.model.Cylinder;
import com.faos.model.Supplier;

public interface CylinderRepository extends JpaRepository<Cylinder, String> {

    // Cylinder-related queries
    List<Cylinder> findByStatus(String status);
    Optional<Cylinder> findByIdAndStatus(String id, String status);
    List<Cylinder> findByType(String type);
    List<Cylinder> findByCreatedDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT COUNT(c) FROM Cylinder c WHERE c.status = :status")
    long countByStatus(@Param("status") String status);

    @Modifying
    @Transactional
    @Query("UPDATE Cylinder c SET c.status = :status WHERE c.id = :id")
    void updateCylinderStatusById(@Param("id") String id, @Param("status") String status);

    List<Cylinder> findByRefillDateAfter(LocalDateTime date);
    List<Cylinder> findByTypeAndStatus(String type, String status);
    List<Cylinder> findByWeight(Double weight);
    List<Cylinder> findByWeightAndStatus(Double weight, String status);

    // Supplier-related queries
    @Query("SELECT c FROM Cylinder c WHERE c.supplier.supplierID = :supplierID")
    List<Cylinder> findBySupplierId(@Param("supplierID") String supplierID);

    List<Cylinder> findBySupplier(Supplier supplier);


    @Query(value = "SELECT id FROM cylinder WHERE status = 'AVAILABLE' AND refill_date is not NULL  LIMIT 1", nativeQuery = true)
    Optional<String> findByConType(@Param("type") String type);

    @Modifying
    @Transactional
    @Query(value = "UPDATE cylinder SET status = 'DELIVERED', cylinder_type='FULL' WHERE id = :cylinderId", nativeQuery = true)
    void updateCylinderStatus(@Param("cylinderId") String cylinderId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE cylinder SET bookingId = :bookingId WHERE id = :cylinderId", nativeQuery = true)
    int updateCylinderBookingId(@Param("cylinderId") String cylinderId, @Param("bookingId") Long bookingId);

//    @Modifying
//    @Transactional
//    @Query(value = "UPDATE cylinder SET status = 'AVAILABLE', bookingId = null, cylinder_type='EMPTY' WHERE bookingId = :bookingId", nativeQuery = true)
//    void updateCylinderByBookingId(@Param("bookingId") long bookingId);



//    @Query(value = "SELECT id FROM cylinder WHERE   status = 'AVAILABLE' LIMIT 1", nativeQuery = true)
//    Optional<String> findByConType(String type);
//
//

    @Modifying
    @Transactional
    @Query(value = "UPDATE cylinder SET status = 'AVAILABLE', bookingId = null, cylinder_type='EMPTY', refill_date=NULL  WHERE bookingId = :bookingIds", nativeQuery = true)
    void updateCylinder(long bookingIds);

    @Query(value = "SELECT * FROM CYLINDER WHERE CYLINDER_TYPE='EMPTY' AND STATUS='AVAILABLE'", nativeQuery = true)
    List<Cylinder> findAllEmptyAvailableCylinders();

}
