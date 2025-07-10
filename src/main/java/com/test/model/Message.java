package com.test.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String name;

    @Column(name = "timestamp", nullable = false)
    private Long timestamp;

    @Column(name = "data_type", nullable = false, length = 50)
    private String dataType;

    @Column(name = "value", nullable = false)
    private String value;
} 