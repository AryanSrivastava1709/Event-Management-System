import { Component } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { EventServiceService } from '../../../../services/event/event-service.service';
import { ToastrService } from 'ngx-toastr';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-event-edit',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './event-edit.component.html',
  styleUrl: './event-edit.component.css',
})
export class EventEditComponent {
  eventForm: FormGroup;
  loading = false;
  eventId!: number;
  existingImageUrl: string = ''; // <-- Store the fetched image URL

  constructor(
    private fb: FormBuilder,
    private eventService: EventServiceService,
    private toastr: ToastrService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.eventForm = this.fb.group({
      eventName: ['', Validators.required],
      description: ['', Validators.required],
      location: ['', Validators.required],
      date: ['', Validators.required],
      time: ['', Validators.required],
      price: [0, [Validators.required, Validators.min(1)]],
      availableSeats: [0, [Validators.required, Validators.min(1)]],
      status: ['ACTIVE', Validators.required],
    });
  }

  ngOnInit(): void {
    this.eventId = +this.route.snapshot.paramMap.get('eventId')!;
    this.eventService.getEventById(this.eventId).subscribe({
      next: (event) => {
        this.eventForm.patchValue(event);
        this.existingImageUrl = event.imageUrl || ''; // Save imageUrl
      },
      error: () => {
        this.toastr.error('Failed to load event.');
        this.router.navigate(['/admin/events']);
      },
    });
  }

  updateEvent() {
    if (this.eventForm.invalid) {
      this.toastr.warning('Please fill all fields.');
      return;
    }

    const updatedEvent = {
      ...this.eventForm.value,
      organizerName: localStorage.getItem('fullName'),
      imageUrl: this.existingImageUrl, // Use previously stored image
    };

    this.loading = true;

    this.eventService.updateEvent(this.eventId, updatedEvent).subscribe({
      next: () => {
        this.toastr.success('Event updated successfully!');
        this.router.navigate(['/admin/events']);
      },
      error: (err) => {
        console.error(err);
        this.toastr.error(err.error?.message || 'Failed to update event.');
        this.loading = false;
      },
    });
  }
}
