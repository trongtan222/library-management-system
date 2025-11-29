import { Component, OnInit } from '@angular/core';
import { UserAuthService } from '../services/user-auth.service';
import { HttpErrorResponse } from '@angular/common/http';
import { CirculationService, ReservationDTO } from '../services/circulation.service';
import { FineDetails, LoanDetails } from '../services/admin.service';
import { UsersService } from '../services/users.service';
import { User } from '../models/user';
import { forkJoin, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators'; // Thêm map
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-my-account',
  templateUrl: './my-account.component.html',
  styleUrls: ['./my-account.component.css'],
  standalone: false
})
export class MyAccountComponent implements OnInit {

  userProfile: User | null = null;
  activeLoans: LoanDetails[] = [];
  historyLoans: LoanDetails[] = [];
  myFines: FineDetails[] = [];
  myReservations: ReservationDTO[] = [];
  
  isLoading = true;
  currentTab: 'PROFILE' | 'ACTIVE' | 'HISTORY' | 'FINES' | 'RESERVATIONS' = 'PROFILE';

  // Stats
  totalBorrowed = 0;
  totalReturned = 0;
  totalFinesAmount = 0;

  constructor(
    private userAuthService: UserAuthService,
    private circulationService: CirculationService,
    private userService: UsersService,
    private toastr: ToastrService
  ) { }

  ngOnInit(): void {
    const userId = this.userAuthService.getUserId();
    if (userId) {
      this.loadAllData(userId);
    } else {
      this.isLoading = false;
      this.errorMessage = "Bạn chưa đăng nhập."; // Hiển thị lỗi nếu chưa login
    }
  }

  loadAllData(userId: number): void {
    this.isLoading = true;
    
    // 1. Xử lý User Profile riêng biệt để tránh gọi API Admin nếu không cần thiết
    const userObs = this.userAuthService.isAdmin() 
      ? this.userService.getUserById(userId) // Admin được phép gọi API này
      : of(this.getLocalUserProfile(userId)); // User thường dùng thông tin local

    forkJoin({
      user: userObs.pipe(
        catchError(err => {
          console.warn('Lỗi tải profile, dùng fallback local.', err);
          return of(this.getLocalUserProfile(userId));
        })
      ),
      loans: this.circulationService.getMyLoanHistory().pipe(catchError(() => of([]))),
      fines: this.circulationService.getMyFines().pipe(catchError(() => of([]))),
      // reservations: this.circulationService.getMyReservations().pipe(catchError(() => of([]))) 
    }).subscribe({
      next: (result) => {
        this.userProfile = result.user;
        
        // Xử lý mảng Loans an toàn
        const loans = Array.isArray(result.loans) ? result.loans : [];

        this.activeLoans = loans.filter(l => l.status === 'ACTIVE' || l.status === 'OVERDUE');
        this.historyLoans = loans.filter(l => l.status === 'RETURNED');
        
        // Xử lý mảng Fines an toàn
        this.myFines = Array.isArray(result.fines) ? result.fines : [];
        
        // this.myReservations = Array.isArray(result.reservations) ? result.reservations : [];

        // Tính toán thống kê
        this.totalBorrowed = loans.length;
        this.totalReturned = this.historyLoans.length;
        this.totalFinesAmount = this.myFines.reduce((sum, f) => sum + (f.fineAmount || 0), 0);

        this.isLoading = false;
      },
      error: (err) => {
        console.error('Lỗi tải dữ liệu tổng hợp:', err);
        this.isLoading = false;
        this.toastr.error("Có lỗi khi tải dữ liệu.");
      }
    });
  }

  // Hàm helper để tạo User object từ LocalStorage
  private getLocalUserProfile(userId: number): User {
    const u = new User();
    u.userId = userId;
    u.name = this.userAuthService.getName() || 'Người dùng';
    u.username = '---'; // Backend hiện tại chưa trả về username trong login response chuẩn
    u.roles = this.userAuthService.getRoles();
    return u;
  }

  changeTab(tab: 'PROFILE' | 'ACTIVE' | 'HISTORY' | 'FINES' | 'RESERVATIONS') {
    this.currentTab = tab;
  }

  renewLoan(loanId: number): void {
    this.circulationService.renew({ loanId: loanId, extraDays: 7 }).subscribe({
      next: () => {
        this.toastr.success("Gia hạn thành công thêm 7 ngày!");
        if (this.userProfile) this.loadAllData(this.userProfile.userId);
      },
      error: (err: HttpErrorResponse) => {
        this.toastr.error(err.error?.message || "Gia hạn thất bại.");
      }
    });
  }
  
  // Thêm biến errorMessage để template sử dụng nếu cần
  errorMessage = ''; 

  cancelReservation(id: number) {
    if(confirm('Bạn có chắc muốn hủy đặt trước cuốn sách này?')) {
      this.circulationService.cancelReservation(id).subscribe({
        next: () => {
          this.toastr.info("Đã hủy đặt trước.");
          // Reload reservations logic here if needed
        },
        error: () => this.toastr.error("Lỗi khi hủy.")
      });
    }
  }
}