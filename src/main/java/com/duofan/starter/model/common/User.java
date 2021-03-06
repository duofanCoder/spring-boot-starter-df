package com.duofan.starter.model.common;

import com.duofan.starter.model.enums.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Arpit Khandelwal.
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "sys_user",
        indexes = @Index(
                name = "idx_user_username",
                columnList = "username",
                unique = true)
)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date createTime;
    private Date updateTime;

    private String email;
    private String name;

    private String password;

    private String username;

    private String mobileNumber;

    @Enumerated(EnumType.STRING)
    private UserRole role;
}
