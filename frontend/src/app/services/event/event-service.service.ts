import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Events } from '../../model/events';

@Injectable({
  providedIn: 'root'
})
export class EventServiceService {

  private baseURl = "http://localhost:8081/event-service/api/events";

  constructor(private http:HttpClient) { }

  getEvents(): Observable<{ events: Events[] }> {
    return this.http.get<{ events: Events[] }>(`${this.baseURl}/all`);
  }

  getEventById(id: number): Observable<Events> {
    return this.http.get<Events>(`${this.baseURl}/get/${id}`);
  }

  getAdminEvents(): Observable<{ events: Events[] }> {

    const token = localStorage.getItem('token'); // or however you store it
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
    const organizerName = localStorage.getItem('fullName');
    return this.http.get<{ events: Events[] }>(`${this.baseURl}/my-events?organizerName=${organizerName}`,{headers: headers});
  }
}
