package com.VBook.AirLineReservation.model;

import com.VBook.AirLineReservation.model.Booking;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Passenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    private int age;

    private String passportNumber;

    private String seatNumber;

    @ManyToOne
    @JoinColumn(name="booking_id")
    private Booking booking;
}