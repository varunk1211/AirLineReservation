package com.VBook.AirLineReservation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class DestinationFlightResponse {
    private String destinationName;
    private String description;
    private String imageUrl;
    private List<Flight> flights;
}