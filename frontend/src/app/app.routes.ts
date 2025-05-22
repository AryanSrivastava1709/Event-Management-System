import { Routes } from '@angular/router';
import { EventsComponent } from './components/events/events.component';
import { RegistrationComponent } from './components/registration/registration.component';
import { LoginComponent } from './components/login/login.component';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';
import { AdminComponent } from './components/dashboard/admin/admin.component';
import { UserComponent } from './components/dashboard/user/user.component';
import { AuthGuard } from './guards/auth-guard.guard';

export const routes: Routes = [
    { path: '', redirectTo: '/events', pathMatch: 'full' },
    { path: 'register', component: RegistrationComponent },
    { path: 'login', component: LoginComponent },
    { 
        path: 'admin/dashboard', 
        component: AdminComponent, 
        canActivate: [AuthGuard],
        data: { roles: ['ADMIN'] }
      },
      { 
        path: 'user/dashboard', 
        component: UserComponent, 
        canActivate: [AuthGuard]
      },
      { path: 'events', component: EventsComponent },
    {path:'**', component:PageNotFoundComponent}
];
