import { Component } from '@angular/core';
import { AuthServiceService } from '../../../services/auth/auth-service.service';
import { Events } from '../../../model/events';
import { EventServiceService } from '../../../services/event/event-service.service';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-user',
  imports: [RouterLink,CommonModule],
  templateUrl: './user.component.html',
  styleUrl: './user.component.css'
})
export class UserComponent {
  fullName :string ="";
  events:Events[] =[];
  error = false;

  constructor(private authService:AuthServiceService,private eventService:EventServiceService,private toastr:ToastrService){}
  
  ngOnInit(): void {
    this.fullName = this.authService.getCurrentUser()?.fullName || "";

    this.eventService.getEvents().subscribe({
      next:(data)=>{
        this.events = data.events.map(event => ({
          ...event,
          fullDateTime: new Date(`${event.date}T${event.time}`)
        }));
      },
      error: (error) => {
        this.toastr.error(error.error.error)
        console.error('Error fetching events:', error);
        this.error = true;
      }
    })
  }
}
