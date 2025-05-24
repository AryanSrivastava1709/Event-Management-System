import { Component } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { EventServiceService } from '../../../../services/event/event-service.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
@Component({
  selector: 'app-event-create',
  imports: [ReactiveFormsModule],
  templateUrl: './event-create.component.html',
  styleUrl: './event-create.component.css',
})
export class EventCreateComponent {
  eventForm: FormGroup;
  loading = false;
  imageFile: File | null = null;

  constructor(
    private fb: FormBuilder,
    private eventService: EventServiceService,
    private toastr: ToastrService,
    private router: Router
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

  onFileChange(event: any) {
    const file = event.target.files[0];
    if (file) this.imageFile = file;
  }

  submitEvent() {
    if (this.eventForm.invalid || !this.imageFile) {
      this.toastr.warning('Please fill all fields and upload an image.');
      return;
    }

    const formData = new FormData();
    Object.entries(this.eventForm.value).forEach(([key, value]) => {
      formData.append(key, value as string);
    });

    formData.append('organizername', localStorage.getItem('fullName')!);
    formData.append('image', this.imageFile!);

    this.loading = true;
    this.eventService.addEvent(formData).subscribe({
      next: () => {
        this.toastr.success('Event created successfully!');
        this.eventForm.reset();
        this.imageFile = null;
        this.loading = false;
        this.router.navigate(['/admin/events']);
      },
      error: (err) => {
        this.toastr.error(err.error.error || 'Failed to create event.');
        this.loading = false;
      },
    });
  }
}
