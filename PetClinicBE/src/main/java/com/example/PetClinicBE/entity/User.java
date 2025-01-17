package com.example.PetClinicBE.entity;

import java.util.Set;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @NotBlank(message = "Tên người dùng không được bỏ trống.")
    @Size(max = 100, message = "Tên chủ sở hữu không vượt quá 100 ký tự.")
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

    @Column(name = "address", length = 255, nullable = true)
    private String address;
    
    @NotBlank(message = "Mật khẩu không được bỏ trống.")
    @Size(min = 8, message = "Mật khẩu phải có ít nhất 8 ký tự.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[@#$%^&+=!]).*$",
             message = "Mật khẩu phải chứa ít nhất một số và một ký tự đặc biệt.")
    @Column(name = "password", length = 255, nullable = false)
    private String password;
    
    Set<String> roles;
}
