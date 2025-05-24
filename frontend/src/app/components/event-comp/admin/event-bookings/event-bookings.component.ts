import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { BookingsService } from '../../../../services/booking/bookings.service';
import { CommonModule } from '@angular/common';
import { EventServiceService } from '../../../../services/event/event-service.service';
import { Events } from '../../../../model/events';

@Component({
  selector: 'app-event-bookings',
  imports: [CommonModule],
  templateUrl: './event-bookings.component.html',
  styleUrl: './event-bookings.component.css'
})
export class EventBookingsComponent {


  bookings: any[] = [];
  loading = false;
  error = false;
  eventId!: number;
  eventName!: string;

  constructor(
    private route: ActivatedRoute,
    private bookingService: BookingsService,
    private eventService : EventServiceService
  ) {}

  ngOnInit(): void {
    this.eventId = Number(this.route.snapshot.paramMap.get('eventId'));
    this.eventService.getEventById(this.eventId).subscribe({
      next: (data) =>{
        this.eventName = data.eventName;
      }
    })
    this.fetchBookings();
  }

  getBookingsByStatus(staus: string):number {
    return this.bookings.filter((booking) => booking.status === staus).length;
  }

  fetchBookings() {
    this.loading = true;
    this.bookingService.getBookingsByEventId(this.eventId).subscribe({
      next: (res: any) => {
        this.bookings = res.bookings || [];
        this.loading = false;
      },
      error: (err) => {
        console.error('Error fetching bookings', err);
        this.error = true;
        this.loading = false;
      }
    });
  }

}
