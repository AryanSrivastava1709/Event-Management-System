import { Component } from '@angular/core';
import { Events } from '../../../../model/events';
import { EventServiceService } from '../../../../services/event/event-service.service';
import { ToastrService } from 'ngx-toastr';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-admin-events',
  imports: [CommonModule,RouterLink],
  templateUrl: './admin-events.component.html',
  styleUrl: './admin-events.component.css'
})
export class AdminEventsComponent {

  events: Events[] = [];
    loading = true;
    error = false;
  
    // Pagination
    page: number = 1;
    pageSize: number = 8;
  
    constructor(
      private eventService: EventServiceService,
      private toastr: ToastrService
    ) {}

    ngOnInit(): void {
      this.fetchEvents();
    }
  
    fetchEvents(): void {
      this.eventService.getAdminEvents().subscribe({
        next: (data) => {
          this.events = data.events.map(event => ({
            ...event,
            fullDateTime: new Date(`${event.date}T${event.time}`)
          }));
          this.loading = false;
          console.log('Fetched events:', this.events);
        },
        error: (error) => {
          this.toastr.error(error.error?.error || 'Failed to fetch events');
          console.error('Error fetching events:', error);
          this.error = true;
          this.loading = false;
        }
      });
    }

    get paginatedEvents(): Events[] {
      const start = (this.page - 1) * this.pageSize;
      return this.events.slice(start, start + this.pageSize);
    }
  
    get totalPages(): number {
      return Math.ceil(this.events.length / this.pageSize);
    }
  
    get pages(): number[] {
      return Array.from({ length: this.totalPages }, (_, i) => i + 1);
    }
  
    goToPage(pageNum: number): void {
      if (pageNum >= 1 && pageNum <= this.totalPages) {
        this.page = pageNum;
        window.scrollTo({ top: 0, behavior: 'smooth' });
      }
    }
}
