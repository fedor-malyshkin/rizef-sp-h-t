package com.rize.test.model.storage;

import com.rize.test.model.rest.AbstractArtistReqRes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "artists")
public class ArtistEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "created_at", updatable = false, nullable = false, columnDefinition = "timestamp with time zone")
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "timestamp with time zone")
    @UpdateTimestamp
    private Instant updatedAt;


    @Column(name = "first_name",  nullable = false)
    String firstName;
    @Column(name = "middle_name",  nullable = true)
    String middleName;
    @Column(name = "last_name",  nullable = false)
    String lastName;
    @Column(name = "category_id",  nullable = false)
    int categoryId;
    @Column(name = "birthday",  nullable = false)
    LocalDate birthday;
    @Column(name = "email",  nullable = false)
    String email;
    @Column(name = "notes",  nullable = true)
    String notes;
}
