import { Component } from '@angular/core';
import { Events } from '../../model/events';
import { EventServiceService } from '../../services/event-service.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-events',
  imports: [CommonModule],
  templateUrl: './events.component.html',
  styleUrl: './events.component.css'
})
export class EventsComponent {

  events:Events[] = [];
  loading=true;
  error = false;

  constructor(private eventService:EventServiceService) {};

  ngOnInit(): void {
    this.fetchEvents();
  }

  fetchEvents(): void {
    this.eventService.getEvents().subscribe({
      next: (data) => {
        this.events = data.events.map(event => ({
          ...event,
          fullDateTime: new Date(`${event.date}T${event.time}`)
        }));
        this.loading = false;
      },
      error: (error) => {
        console.error('Error fetching events:', error);
        this.error = true;
        this.loading = false;
      }
    });
  }

}
