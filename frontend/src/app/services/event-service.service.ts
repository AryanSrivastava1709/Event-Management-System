import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Events } from '../model/events';

@Injectable({
  providedIn: 'root'
})
export class EventServiceService {

  private apiUrl = "http://localhost:8081/event-service/api/events/all";

  constructor(private http:HttpClient) { }

  getEvents(): Observable<{ events: Events[] }> {
    return this.http.get<{ events: Events[] }>(this.apiUrl);
  }
}
