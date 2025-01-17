package com.example.PetClinicBE.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;

@Data
@Getter
@Setter
public class MedicalRecordDTO {
	@Max(value = 10, message = "Số thứ tự khám không được lớn hơn 10.")
    private Integer sequenceNumber;

	@NotNull(message = "Thời gian lịch khám không được để trống.")
    private LocalDateTime appointmentTime;

    @NotBlank(message = "Tên người dùng không được bỏ trống.")
    @Size(max = 100, message = "Tên chủ sở hữu không vượt quá 100 ký tự.")
    private String nameOwner;

    @NotBlank(message = "Số điện thoại không được bỏ trống.")
    @Pattern(regexp = "^[0-9]{10}$", message = "Số điện thoại phải chứa 10 chữ số.")
    private String phoneOwner;

    @Size(max = 50, message = "Tên thú cưng không vượt quá 50 ký tự.")
    private String namePet;
    
    @Column(name = "age", nullable = true)
    private String age;

    @Size(max = 50, message = "Tên loài không vượt quá 50 ký tự.")
    private String species;

    @Size(max = 50, message = "Tên giống loài không vượt quá 50 ký tự.")
    @Column(name = "breed", length = 50, nullable = true)
    private String breed;
    
    @Size(max = 255, message = "Mô tả không vượt quá 255 ký tự.")
    @Column(name = "description", length = 255, nullable = true)
    private String description;

    @Size(max = 100, message = "Tên bác sĩ thú y không vượt quá 100 ký tự.")
    private String nameVeterinarian;

    @Pattern(regexp = "^[0-9]{10}$", message = "Số điện thoại phải chứa 10 chữ số.")
    private String phoneVeterinarian;

    private String diagnosis;
    
    private String treatment;

    private String status;
}
