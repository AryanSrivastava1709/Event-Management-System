import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Events } from '../../model/events';
import { EventServiceService } from '../../services/event-service.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-events',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './events.component.html',
  styleUrl: './events.component.css'
})
export class EventsComponent {
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
    this.eventService.getEvents().subscribe({
      next: (data) => {
        this.events = data.events.map(event => ({
          ...event,
          fullDateTime: new Date(`${event.date}T${event.time}`)
        }));
        this.loading = false;
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
