package com.VBook.AirLineReservation.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime bookingDate;

    private String bookingStatus;   // CONFIRMED, CANCELLED

    private double totalAmount;

    // Many bookings belong to one user
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    // Many bookings belong to one flight
    @ManyToOne
    @JoinColumn(name = "flight_id")
    private Flight flight;

    // One booking can have multiple passengers
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private List<Passenger> passengers;
}