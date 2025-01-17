package com.example.PetClinicBE.entity;

import java.util.Set;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="veterinarians")
public class Veterinarian {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @NotBlank(message = "Tên bác sĩ thú y không được bỏ trống.")
    @Size(max = 100, message = "Tên bác sĩ thú y không vượt quá 100 ký tự.")
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @NotBlank(message = "Số điện thoại không được bỏ trống.")
    @Pattern(regexp = "^[0-9]{10}$", message = "Số điện thoại phải chứa 10 chữ số.")
    @Column(name = "phone", length = 10, nullable = false)
    private String phone;

    @NotBlank(message = "Email không được bỏ trống.")
    @Size(max = 100, message = "Email không vượt quá 100 ký tự.")
    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;

    @Size(max = 100, message = "Chuyên môn không vượt quá 100 ký tự.")
    @Column(name = "specialization", length = 100, nullable = true)
    private String specialization;
    
    @NotBlank(message = "Mật khẩu không được bỏ trống.")
    @Size(min = 8, message = "Mật khẩu phải có ít nhất 8 ký tự.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[@#$%^&+=!]).*$",
             message = "Mật khẩu phải chứa ít nhất một số và một ký tự đặc biệt.")
    @Column(name = "password", length = 255, nullable = false)
    private String password;
    
    Set<String> roles;
}