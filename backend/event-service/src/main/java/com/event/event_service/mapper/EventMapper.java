package com.event.event_service.mapper;

import com.event.event_service.dto.EventRequest;
import com.event.event_service.dto.EventResponse;
import com.event.event_service.dto.UpdateRequest;
import com.event.event_service.model.Event;

public class EventMapper {

    public static Event toEntity(EventRequest eventRequest){
        Event event = new Event();
        event.setEventName(eventRequest.getEventName());
        event.setDescription(eventRequest.getDescription());
        event.setLocation(eventRequest.getLocation());
        event.setDate(eventRequest.getDate());
        event.setTime(eventRequest.getTime());
        event.setPrice(eventRequest.getPrice());
        event.setAvailableSeats(eventRequest.getAvailableSeats());
        event.setOrganizername(eventRequest.getOrganizername());
        event.setStatus(eventRequest.getStatus());

        return event;
    }

    public static EventResponse toResponse(Event event){
        EventResponse eventResponse = EventResponse.builder()
                .id(event.getId())
                .eventName(event.getEventName())
                .description(event.getDescription())
                .location(event.getLocation())
                .date(event.getDate())
                .time(event.getTime())
                .price(event.getPrice())
                .availableSeats(event.getAvailableSeats())
                .organizerName(event.getOrganizername())
                .imageUrl(event.getImageUrl())
                .status(event.getStatus())
                .createdAt(event.getCreatedAt())
                .updatedAt(event.getUpdatedAt())
                .build();

        return eventResponse;
    }

    public static void updateEntity(Event event, UpdateRequest updateRequest){

        event.setEventName(updateRequest.getEventName() != null ? updateRequest.getEventName() : event.getEventName());
        event.setDescription(updateRequest.getDescription() != null ? updateRequest.getDescription() : event.getDescription());
        event.setLocation(updateRequest.getLocation() != null ? updateRequest.getLocation() : event.getLocation());
        event.setDate(updateRequest.getDate() != null ? updateRequest.getDate() : event.getDate());
        event.setTime(updateRequest.getTime() != null ? updateRequest.getTime() : event.getTime());
        event.setPrice(updateRequest.getPrice() != null ? updateRequest.getPrice() : event.getPrice());
        event.setAvailableSeats(updateRequest.getAvailableSeats() != null ? updateRequest.getAvailableSeats() : event.getAvailableSeats());
        event.setOrganizername(updateRequest.getOrganizername() != null ? updateRequest.getOrganizername() : event.getOrganizername());
        event.setImageUrl(updateRequest.getImageUrl() != null ? updateRequest.getImageUrl() : event.getImageUrl());
        event.setStatus(updateRequest.getStatus() != null ? updateRequest.getStatus() : event.getStatus());
    }
}
