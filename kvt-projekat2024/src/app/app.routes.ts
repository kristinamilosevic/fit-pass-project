import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component'; 
import { AccountRequestComponent } from './components/account-request/account-request.component'; 
import { AccountRequestListComponent } from './components/account-request-list/account-request-list.component';
import { AdminComponent } from './components/admin/admin.component'; 
import { ChangePasswordComponent } from './components/change-password/change-password.component';
import { FacilityListComponent } from './components/facility-list/facility-list.component';
import { AddFacilityComponent } from './components/add-facility/add-facility.component';
import { UpdateFacilityComponent } from './components/update-facility/update-facility.component';
import { FacilityComponent } from './components/facility/facility.component';
import { ExerciseComponent } from './components/exercise/exercise.component';
import { ReviewComponent } from './components/review/review.component';  
import { UpdateUserComponent } from './components/update-user/update-user.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'account-request', component: AccountRequestComponent },
  { path: 'account-request-list', component: AccountRequestListComponent },
  { path: 'admin', component: AdminComponent },
  { path: 'change-password', component: ChangePasswordComponent },
  { path: 'facilities', component: FacilityListComponent },
  { path: 'add-facility', component: AddFacilityComponent },
  { path: 'facilities/update/:id', component: UpdateFacilityComponent },
  { path: 'facility-details/:id', component: FacilityComponent },
  { path: 'exercise/:id', component: ExerciseComponent },
  { path: 'review/:id', component: ReviewComponent },
  { path: 'update-user', component: UpdateUserComponent }
];



