package com.excelsior.capstone.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "class_report")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "report_title", nullable = false)
    private String reportTitle;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "generation_date", nullable = false)
    private Date generationDate;

    // Relationship with User entity
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Removed the direct relationship with CollegeClass since User already has it
}
