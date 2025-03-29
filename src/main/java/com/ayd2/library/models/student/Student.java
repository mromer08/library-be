package com.ayd2.library.models.student;

import com.ayd2.library.models.degree.Degree;
import com.ayd2.library.models.user.UserAccount;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserAccount userAccount;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isSanctioned;

    @Column(nullable = false, unique = true)
    private Long carnet;

    @ManyToOne
    @JoinColumn(name = "degree_id")
    private Degree degree;
}
