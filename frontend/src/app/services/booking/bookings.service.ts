import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BookingRequest, BookingResponse } from '../../model/booking';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class BookingsService {
  private baseUrl = 'http://localhost:8081/booking-service/api/bookings';

  constructor(private http: HttpClient) {}

  createBooking(booking: BookingRequest): Observable<BookingResponse> {
    const token = localStorage.getItem('token'); 
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
    return this.http.post<BookingResponse>(`${this.baseUrl}/add`, booking, {
      headers: headers,
    });
  }

  getBookingsById(id:number):Observable<{bookings: BookingResponse[]}>{
    const token = localStorage.getItem('token'); 
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
    return this.http.get<{bookings: BookingResponse[]}>(`${this.baseUrl}/user/${id}`, {
      headers: headers,
    });
  }

  updateBookingById(id:number,updatedSeats:number):Observable<BookingResponse>{
    const token = localStorage.getItem('token'); 
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
    return this.http.put<BookingResponse>(`${this.baseUrl}/update/${id}?updatedSeats=${updatedSeats}`,{}, {
      headers: headers,
    });
  }

  getBookingsByEventId(id:number):Observable<{bookings: BookingResponse[]}>{
    const token = localStorage.getItem('token'); 
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
    return this.http.get<{bookings: BookingResponse[]}>(`${this.baseUrl}/event/${id}`, {
      headers: headers,
    });
  }
}
