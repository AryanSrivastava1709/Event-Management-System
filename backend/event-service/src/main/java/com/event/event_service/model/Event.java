package com.event.event_service.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "events")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String eventName;
    
    private String description;

    private String location;

    private LocalDate date;

    private LocalTime time;

    private double price;

    private int availableSeats;

    private String organizername;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    /*
     * JPA Lifecycle Callbacks
     * These methods are called by JPA when the entity is persisted or updated.
     * @PrePersist is called before the entity is persisted to the database.
     * @PreUpdate is called before the entity is updated in the database.
     */
    
    @PrePersist
    private void onCreate(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    private void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }


}
