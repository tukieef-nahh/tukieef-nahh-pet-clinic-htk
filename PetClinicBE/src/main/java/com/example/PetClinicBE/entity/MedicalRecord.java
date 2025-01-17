package com.example.PetClinicBE.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="medical_records")
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;
    
    @Max(value = 10, message = "Số thứ tự khám không được lớn hơn 10.")
    @Column(name = "sequenceNumber", nullable = true)
    private Integer sequenceNumber;
    
    @NotNull(message = "Thời gian lịch khám không được để trống.")
    @Column(name = "appointmentTime", nullable = false)
    private LocalDateTime appointmentTime;

    @NotBlank(message = "Tên người dùng không được bỏ trống.")
    @Size(max = 100, message = "Tên chủ sở hữu không vượt quá 100 ký tự.")
    @Column(name = "nameOwner", length = 100, nullable = false)
    private String nameOwner;

    @NotBlank(message = "Số điện thoại không được bỏ trống.")
    @Pattern(regexp = "^[0-9]{10}$", message = "Số điện thoại phải chứa 10 chữ số.")
    @Column(name = "phoneOwner", length = 10, nullable = false)
    private String phoneOwner;
    
    @Size(max = 100, message = "Tên bác sĩ thú y không vượt quá 100 ký tự.")
    @Column(name = "nameVeterinarian", length = 100, nullable = true)
    private String nameVeterinarian;
    
    @Pattern(regexp = "^[0-9]{10}$", message = "Số điện thoại phải chứa 10 chữ số.")
    @Column(name = "phoneVeterinarian", length = 10, nullable = true)
    private String phoneVeterinarian;
    
    @Size(max = 50, message = "Tên thú cưng không vượt quá 50 ký tự.")
    @Column(name = "namePet", length = 50, nullable = true)
    private String namePet;
    
    @Column(name = "age", nullable = true)
    private String age;
    
    @Size(max = 50, message = "Tên loài không vượt quá 50 ký tự.")
    @Column(name = "species", length = 50, nullable = true)
    private String species;

    @Size(max = 50, message = "Tên giống loài không vượt quá 50 ký tự.")
    @Column(name = "breed", length = 50, nullable = true)
    private String breed;
    
    @Size(max = 255, message = "Mô tả không vượt quá 255 ký tự.")
    @Column(name = "description", length = 255, nullable = true)
    private String description;

    @Column(name = "diagnosis", nullable = true) 
    private String diagnosis;

    @Column(name = "treatment", nullable = true)
    private String treatment;

    @NotNull(message = "Thời gian tạo không được bỏ trống.")
    @Column(name = "createdAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @NotNull(message = "Thời gian cập nhật không được bỏ trống.")
    @Column(name = "updatedAt", nullable = false)
    private LocalDateTime updatedAt;
    
    @NotNull(message = "Trạng thái không được bỏ trống.")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AppointmentStatus status = AppointmentStatus.WAITING;

    @PrePersist
    private void onCreate() {
        if (appointmentTime != null && appointmentTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Thời gian lịch khám không được trước ngày hiện tại.");
        }
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    private void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum AppointmentStatus {
        WAITING, COMPLETED, CANCELED_BY_USER, CANCELED_BY_ADMIN
    }
}
