import { Component } from '@angular/core';
import { Events } from '../../../../model/events';
import { EventServiceService } from '../../../../services/event/event-service.service';
import { ToastrService } from 'ngx-toastr';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule, NgModel } from '@angular/forms';
import { BookingsService } from '../../../../services/booking/bookings.service';
import { BookingRequest } from '../../../../model/booking';
import { AuthServiceService } from '../../../../services/auth/auth-service.service';

@Component({
  selector: 'app-event-detail',
  imports: [CommonModule, FormsModule],
  templateUrl: './event-detail.component.html',
  styleUrl: './event-detail.component.css',
})
export class EventDetailComponent {
  seatsBooked: number = 1;
  loading:boolean = false;

  incrementSeats() {
    if (this.seatsBooked < this.event.availableSeats) {
      this.seatsBooked++;
    } else {
      this.toastr.error('No more seats available');
    }
  }

  decrementSeats() {
    if (this.seatsBooked > 1) {
      this.seatsBooked--;
    } else {
      this.toastr.info('Seats booked cannot be less than 1');
    }
  }

  handleBookNow(){
    const userId = localStorage.getItem('userId')!;
    const booking:BookingRequest = {
      eventId: this.event.id,
      userId: Number.parseInt(userId),
      seatsBooked: this.seatsBooked,
    }
    this.loading = true;
    this.bookingService.createBooking(booking).subscribe({
      
      next:()=>{
        setTimeout(()=>{
          this.toastr.success('Booking successful');
          this.router.navigate(['/events']);
          this.loading = false;
        },2000)
      },
      error:(err)=>{
        this.toastr.error(err.error.error || 'Booking failed');
        this.loading = false;
      }
    })
  }

  event: Events = {
    id: 0,
    eventName: '',
    description: '',
    location: '',
    date: '',
    time: '',
    price: 0,
    availableSeats: 0,
    organizerName: '',
    imageUrl: '',
    status: '',
    createdAt: '',
    updatedAt: '',
  };

  constructor(
    private eventService: EventServiceService,
    private toastr: ToastrService,
    private route: ActivatedRoute,
    private router: Router,
    private bookingService: BookingsService,
    private authService: AuthServiceService
  ) {}

  ngOnInit(): void {
    const id: number = +this.route.snapshot.paramMap.get('id')!;
    this.getEventById(id);
  }

  getEventById(id: number): void {
    this.eventService.getEventById(id).subscribe({
      next: (data) => {
        this.event = data;
        const date: string = `${data.date}T${data.time}`;
        this.event.fullDateTime = new Date(date);
        this.loading = false;
      },
      error: (err) => {
        setTimeout(() => {
          this.router.navigate(['/events']);
          this.loading = false;
          this.toastr.error(err.error.error || 'Failed to fetch event details');
        }, 1000);
      },
    });
  }
}
