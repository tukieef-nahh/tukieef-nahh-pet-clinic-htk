package com.example.PetClinicBE.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.example.PetClinicBE.entity.MedicalRecord;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {

    @Query("SELECT m FROM MedicalRecord m WHERE m.sequenceNumber = :sequenceNumber AND DATE(m.appointmentTime) = DATE(:appointmentDate)")
    Optional<MedicalRecord> findBySequenceNumberAndDate(@Param("sequenceNumber") Integer sequenceNumber, @Param("appointmentDate") LocalDateTime appointmentDate);

    @Query("SELECT COUNT(m) FROM MedicalRecord m WHERE DATE(m.appointmentTime) = DATE(:appointmentDate)")
    long countByDate(@Param("appointmentDate") LocalDateTime appointmentDate);
    
    // Đếm số lượng hồ sơ trong ngày với điều kiện trạng thái không phải CANCELED_BY_USER và CANCELED_BY_ADMIN
    long countByAppointmentTimeAndStatusNotIn(LocalDate appointmentTime, List<MedicalRecord.AppointmentStatus> excludedStatuses);

    // Tìm số thứ tự lớn nhất trong ngày
    @Query("SELECT MAX(m.sequenceNumber) FROM MedicalRecord m WHERE DATE(m.appointmentTime) = DATE(:appointmentDate)")
    Optional<Integer> findMaxSequenceNumberByDate(@Param("appointmentDate") LocalDate appointmentDate);

    // Truy vấn các hồ sơ không ở trạng thái COMPLETED
    Page<MedicalRecord> findByStatusNot(MedicalRecord.AppointmentStatus status, Pageable pageable);
    
    // Truy vấn các hồ sơ có trạng thái COMPLETED
    Page<MedicalRecord> findByStatus(MedicalRecord.AppointmentStatus status, Pageable pageable);
    
    // Truy vấn tất cả hồ sơ của người dùng theo userId
    Page<MedicalRecord> findAllByPhoneOwner(Pageable pageable, String phoneOwner);
    
    @Query("SELECT m FROM MedicalRecord m WHERE m.status = 'WAITING'")
    Page<MedicalRecord> findByStatusWaiting(Pageable pageable);
    
    // Tìm hồ sơ theo số thứ tự và ngày
    Optional<MedicalRecord> findBySequenceNumberAndAppointmentTime(int sequenceNumber, LocalDateTime appointmentTime);
    
    @Query("SELECT m FROM MedicalRecord m " +
            "WHERE (LOWER(m.nameOwner) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(m.phoneOwner) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(m.nameVeterinarian) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(m.phoneVeterinarian) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND m.status = 'COMPLETED'")
     Page<MedicalRecord> searchCompletedRecords(@Param("keyword") String keyword, Pageable pageable);

     @Query("SELECT m FROM MedicalRecord m " +
            "WHERE (LOWER(m.nameOwner) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(m.phoneOwner) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND m.status <> 'COMPLETED'")
     Page<MedicalRecord> searchNonCompletedRecords(@Param("keyword") String keyword, Pageable pageable);

}