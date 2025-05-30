import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Events } from '../../model/events';

@Injectable({
  providedIn: 'root',
})
export class EventServiceService {
  private baseURl = 'http://localhost:8081/event-service/api/events';

  constructor(private http: HttpClient) {}

  getEvents(): Observable<{ events: Events[] }> {
    return this.http.get<{ events: Events[] }>(`${this.baseURl}/all`);
  }

  getEventById(id: number): Observable<Events> {
    return this.http.get<Events>(`${this.baseURl}/get/${id}`);
  }

  getAdminEvents(): Observable<{ events: Events[] }> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
    const organizerName = localStorage.getItem('fullName');
    return this.http.get<{ events: Events[] }>(
      `${this.baseURl}/my-events?organizerName=${organizerName}`,
      { headers: headers }
    );
  }

  addEvent(formData: FormData): Observable<any> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
    return this.http.post(`${this.baseURl}/add`, formData, { headers });
  }

  deleteEvent(id: number): Observable<void> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
    return this.http.delete<void>(`${this.baseURl}/delete/${id}`, { headers });
  }

  updateEvent(id: number, eventData: any): Observable<any> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
      'Content-Type': 'application/json',
    });
    return this.http.put(`${this.baseURl}/update/${id}`, eventData, {
      headers,
    });
  }
}
