package com.example.encrypt.domain;

import com.example.encrypt.config.ColumnEncryptor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.AttributeAccessor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private String password;

    @Convert(converter = ColumnEncryptor.class)
    private String id;

    @Convert(converter = ColumnEncryptor.class)
    private String username;

    @Convert(converter = ColumnEncryptor.class)
    private String phoneNumber;

    @Convert(converter = ColumnEncryptor.class)
    private String address;

}
