import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BooksListComponent } from './books-list/books-list.component';
import { CreateBookComponent } from './create-book/create-book.component';
import { FormsModule } from '@angular/forms';
import { UpdateBookComponent } from './update-book/update-book.component';
import { BookDetailsComponent } from './book-details/book-details.component';
import { UsersListComponent } from './users-list/users-list.component';
import { UserDetailsComponent } from './user-details/user-details.component';
import { UpdateUserComponent } from './update-user/update-user.component';
import { LoginComponent } from './login/login.component';
import { LogoutComponent } from './logout/logout.component';
import { HeaderComponent } from './header/header.component';
import { HomeComponent } from './home/home.component';
import { BooksService } from './services/books.service';
import { UsersService } from './services/users.service';
import { RouterModule } from '@angular/router';
import { AuthGuard } from './auth/auth.guard';
import { AuthInterceptor } from './auth/auth.interceptor';
import { ErrorInterceptor } from './auth/error.interceptor';
import { LoadingInterceptor } from './auth/loading.interceptor';
import { ForbiddenComponent } from './forbidden/forbidden.component';
import { BorrowBookComponent } from './borrow-book/borrow-book.component';
import { ReturnBookComponent } from './return-book/return-book.component';
import { SignupComponent } from './signup/signup.component';
import { UserAuthService } from './services/user-auth.service';
import { DashboardComponent } from './admin/dashboard/dashboard.component';
import { LoanManagementComponent } from './admin/loan-management/loan-management.component';
import { MyAccountComponent } from './my-account/my-account.component';
import { ManageFinesComponent } from './admin/manage-fines/manage-fines.component';
import { ReportsComponent } from './admin/reports/reports.component';
import { CommonModule } from '@angular/common';
import { ToastrModule } from 'ngx-toastr';
import { ManageReviewsComponent } from './admin/manage-reviews/manage-reviews.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CreateUserComponent } from './create-user/create-user.component';
import { ChatbotComponent } from './chatbot/chatbot.component';
import { CreateLoanComponent } from './admin/create-loan/create-loan.component'; // Import
// Import thư viện QR và Scanner
import { ZXingScannerModule } from '@zxing/ngx-scanner';
import { QRCodeComponent } from 'angularx-qrcode';

@NgModule({
  declarations: [
    AppComponent,
    BooksListComponent,
    CreateBookComponent,
    UpdateBookComponent,
    BookDetailsComponent,
    UsersListComponent,
    UserDetailsComponent,
    UpdateUserComponent,
    LoginComponent,
    LogoutComponent,
    HeaderComponent,
    HomeComponent,
    ForbiddenComponent,
    BorrowBookComponent,
    ReturnBookComponent,
    SignupComponent,
    DashboardComponent,
    LoanManagementComponent,
    MyAccountComponent,
    ManageFinesComponent,
    ReportsComponent,
    ManageReviewsComponent,
    CreateUserComponent,
    CreateLoanComponent,
    ChatbotComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    RouterModule,
    BrowserAnimationsModule,
    CommonModule,
    ToastrModule.forRoot({
      timeOut: 3000,
      positionClass: 'toast-bottom-right',
      preventDuplicates: true,
    }),
    ZXingScannerModule,
    QRCodeComponent
  ],
  providers: [
    AuthGuard,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: ErrorInterceptor,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: LoadingInterceptor,
      multi: true
    },
    UsersService,
    BooksService,
    UserAuthService,
    provideHttpClient(withInterceptorsFromDi())
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }