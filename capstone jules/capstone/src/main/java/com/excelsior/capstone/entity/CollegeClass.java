package com.excelsior.capstone.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "college_class")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollegeClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "class_name", nullable = false)
    private String className;

    @Column(name = "credits", nullable = false)
    private int credits;

    @Column(name = "instructor", nullable = false)
    private String instructor;

    @Column(name = "start_date", nullable = false)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    @Column(name = "class_code", nullable = false, unique = true)
    private String classCode;

    // User relationship
    @ManyToOne(fetch = FetchType.LAZY) // Set to LAZY to optimize loading
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude // Prevent recursive toString issues
    private User user;
}
