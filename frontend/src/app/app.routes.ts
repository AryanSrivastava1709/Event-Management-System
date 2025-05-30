import { Routes } from '@angular/router';
import { EventsComponent } from './components/event-comp/user/events/events.component';
import { RegistrationComponent } from './components/registration/registration.component';
import { LoginComponent } from './components/login/login.component';
import { PageNotFoundComponent } from './components/shared-comp/page-not-found/page-not-found.component';
import { AdminComponent } from './components/dashboard/admin/admin.component';
import { UserComponent } from './components/dashboard/user/user.component';
import { AuthGuard } from './guards/auth-guard.guard';
import { EventDetailComponent } from './components/event-comp/user/event-detail/event-detail.component';
import { AdminEventsComponent } from './components/event-comp/admin/admin-events/admin-events.component';
import { BookingComponent } from './components/booking/booking.component';
import { EventCreateComponent } from './components/event-comp/admin/event-create/event-create.component';
import { EventBookingsComponent } from './components/event-comp/admin/event-bookings/event-bookings.component';
import { EventEditComponent } from './components/event-comp/admin/event-edit/event-edit.component';

let role = localStorage.getItem('role')!;
export const routes: Routes = [
  {
    path: 'admin/dashboard',
    component: AdminComponent,
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN'] },
  },
  {
    path: 'event/create',
    component: EventCreateComponent,
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN'] },
  },
  {
    path: 'user/dashboard',
    component: UserComponent,
    canActivate: [AuthGuard],
    data: { roles: ['USER'] },
  },
  { path: '', redirectTo: '/events', pathMatch: 'full' },
  { path: 'register', component: RegistrationComponent },
  { path: 'login', component: LoginComponent },
  {
    path: 'admin/events',
    component: AdminEventsComponent,
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN'] },
  },
  {
    path: 'admin/event/:eventId',
    component: EventBookingsComponent,
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN'] },
  },
  {
    path: 'admin/event/edit/:eventId',
    component: EventEditComponent,
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN'] },
  },
  {
    path: 'events',
    component: EventsComponent,
    data: { roles: ['USER'] },
  },
  {
    path: 'event/:id',
    component: EventDetailComponent,
    canActivate: [AuthGuard],
    data: { roles: ['USER'] },
  },
  {
    path: 'bookings',
    component: BookingComponent,
    canActivate: [AuthGuard],
    data: { roles: ['USER'] },
  },
  { path: '**', component: PageNotFoundComponent },
];
