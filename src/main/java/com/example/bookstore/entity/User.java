package com.example.bookstore.entity;

import com.example.bookstore.type.UserRole;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@AttributeOverrides(
        @AttributeOverride(name = "createdAt", column = @Column(name = "registered_at"))
)
public class User extends BaseEntity {
    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;

    @Enumerated(EnumType.STRING)
    private UserRole role;
}