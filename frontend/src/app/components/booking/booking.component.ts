import { Component, NgZone } from '@angular/core';
import { BookingResponse } from '../../model/booking';
import { BookingsService } from '../../services/booking/bookings.service';
import { ToastrService } from 'ngx-toastr';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { EventServiceService } from '../../services/event/event-service.service';

declare var Razorpay: any;

@Component({
  selector: 'app-booking',
  standalone: true,
  imports: [CommonModule,FormsModule],
  templateUrl: './booking.component.html',
  styleUrl: './booking.component.css',
})
export class BookingComponent {

  bookings: BookingResponse[] = [];
  loading: boolean = false;
  payingBookingId: number | null = null;

  updateSeats:number = 1;
  toggle:boolean = false;

  constructor(
    private bookingService: BookingsService,
    private toastr: ToastrService,
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.fetchBookings();
  }

  fetchBookings(): void {
    this.loading = true;
    const userId = Number.parseInt(localStorage.getItem('userId')!);
    this.bookingService.getBookingsById(userId).subscribe({
      next: (data) => {
        this.bookings = data.bookings.map((booking) => ({
          ...booking,
          fullDateTime: new Date(
            `${booking.bookingDate}T${booking.bookingTime}`
          ),
        }));
        this.loading = false;
      },
      error: (err) => {
        this.loading = false;
      },
    });
  }

  //update the bookings
  handleToggle(seats: number) {
    this.updateSeats = seats
    this.toggle = !this.toggle;
  }

  handleUpdateBooking(id:number) {
    this.loading = true;
    this.bookingService.updateBookingById(id, this.updateSeats).subscribe({
      next: (data) => {
        this.bookings = this.bookings.map((b) => {
          if (b.id === id) {
            return { ...b, seatsBooked: data.seatsBooked, totalAmount: data.totalAmount };
          }
          return b;
        });
        this.loading = false;
        this.toastr.success('Booking updated successfully', 'Success');
        this.toggle = !this.toggle;
      },
      error: (err) =>{
        this.loading = false;
        this.toastr.error('Failed to update booking', 'Error');
        this.toggle = !this.toggle;
      }
    })
  }

  incrementSeats(booking:BookingResponse) {
    if (this.updateSeats < booking.seatsBooked) {
      this.updateSeats++;
    } 
  }

  decrementSeats() {
    if (this.updateSeats > 1) {
      this.updateSeats--;
    } else {
      this.toastr.info('Seats booked cannot be less than 1');
    }
  }


  //cancel a booking
  handleCancelBooking(id: number) {
    const token = localStorage.getItem('token') || '';
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    this.http
      .delete<any>(
        `http://localhost:8081/booking-service/api/bookings/delete/${id}`,
        { headers: headers }
      )
      .subscribe({
        next: () => {
          this.loading = true;
          setTimeout(() => {
            this.toastr.success('Booking cancelled successfully', 'Success');
            this.router.navigate(['/events']);
            this.loading = false;
          }, 200);
        },
        error: () => {
          this.loading = true;
          setTimeout(() => {
            this.toastr.error('Failed to cancel booking', 'Error');
            this.router.navigate(['/events']);
            this.loading = false;
          }, 200);
        },
      });
  }

  //razorpay payment system
  payNow(booking: BookingResponse): void {
    const token = localStorage.getItem('token') || '';
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    const paymentRequest = {
      bookingId: booking.id,
      userId: booking.userId,
      eventId: booking.eventId,
      amount: booking.totalAmount,
      currency: 'INR',
      description: 'Payment for event booking',
    };

    this.payingBookingId = booking.id;

    this.http
      .post<any>(
        'http://localhost:8081/payment-service/api/payments/initiate',
        paymentRequest,
        { headers }
      )
      .subscribe({
        next: (data) => {
          const options = {
            key: data.keyId,
            amount: data.amount,
            currency: data.currency,
            name: 'Event Booking System',
            description: paymentRequest.description,
            order_id: data.orderId,
            handler: (response: any) => {
              this.verifyPayment(response, paymentRequest, headers);
            },
            theme: {
              color: '#E11D48',
            },
          };

          const rzp = new Razorpay(options);
          rzp.open();
          this.payingBookingId = null;
        },
        error: (err) => {
          this.toastr.error('Payment initiation failed', 'Error');
          this.payingBookingId = null;
        },
      });
  }

  verifyPayment(
    response: any,
    paymentRequest: any,
    headers: HttpHeaders
  ): void {
    const verifyRequest = {
      razorpayOrderId: response.razorpay_order_id,
      razorpayPaymentId: response.razorpay_payment_id,
      razorpaySignature: response.razorpay_signature,
      ...paymentRequest,
    };

    this.http
      .post<any>(
        'http://localhost:8081/payment-service/api/payments/verify',
        verifyRequest,
        { headers }
      )
      .subscribe({
        next: () => {
          this.loading = true;
          setTimeout(() => {
            this.toastr.success('Payment successful. Explore More', 'Success');
            this.router.navigate(['/events']);
            this.loading = false;
          }, 100);
        },
        error: () => {
          setTimeout(() => {
            this.loading = true;
            this.toastr.error('Payment verification failed', 'Error');
            this.router.navigate(['/events']);
            this.loading = false;
          }, 200);
        },
      });
  }

  
}
