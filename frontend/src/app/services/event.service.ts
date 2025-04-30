// event.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';
// import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class EventService {
  private apiUrl = 'http://localhost:8081/event-service/api/events/api/events';

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) {}

  // Get headers with auth token
  private getHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  // Get all events
  getAllEvents(): Observable<any> {
    return this.http.get(`${this.apiUrl}/all`);
  }

  // Get event by ID
  getEventById(id: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/get/${id}`);
  }

  // Create new event
  createEvent(eventData: FormData): Observable<any> {
    return this.http.post(`${this.apiUrl}/add`, eventData, {
      headers: this.getHeaders()
    });
  }

  // Update event
  updateEvent(id: number, eventData: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/update/${id}`, eventData, {
      headers: this.getHeaders()
    });
  }

  // Delete event
  deleteEvent(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/delete/${id}`, {
      headers: this.getHeaders()
    });
  }

  // Search events
  searchEvents(keyword: string): Observable<any> {
    const params = new HttpParams().set('param', keyword);
    return this.http.get(`${this.apiUrl}/search`, { params });
  }

  // Get events by organizer
  getMyEvents(organizerName: string): Observable<any> {
    const params = new HttpParams().set('organizerName', organizerName);
    return this.http.get(`${this.apiUrl}/my-events`, { 
      params,
      headers: this.getHeaders()
    });
  }
}