import { Component, OnInit } from '@angular/core';
import { AdminService, DashboardDetails, LoanDetails } from '../../services/admin.service';
import { BooksService } from '../../services/books.service';
import { UsersService } from '../../services/users.service';
import { Books } from '../../models/books';
import { Users } from '../../models/users';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  details: DashboardDetails | null = null;
  isLoading = true;
  errorMessage = '';

  // Dữ liệu chi tiết cho các bảng
  allBooks: Books[] = [];
  allUsers: Users[] = [];
  allLoans: LoanDetails[] = [];
  
  // Trạng thái hiển thị bảng
  activeView: 'books' | 'users' | 'activeLoans' | 'overdueLoans' | null = null;
  loan: any;

  constructor(
    private adminService: AdminService,
    private booksService: BooksService,
    private usersService: UsersService
  ) { }

  ngOnInit(): void {
    this.loadDashboardDetails();
  }

  loadDashboardDetails(): void {
    this.isLoading = true;
    this.adminService.getDashboardDetails().subscribe({
      next: (data: DashboardDetails) => {
        this.details = data;
        this.isLoading = false;
      },
      error: (err: any) => this.handleError(err)
    });
  }

  // Chuyển đổi trạng thái hiển thị bảng
  toggleView(view: 'books' | 'users' | 'activeLoans' | 'overdueLoans'): void {
    if (this.activeView === view) {
      this.activeView = null; // Nhấp lần nữa để ẩn đi
    } else {
      this.activeView = view;
      // Tải dữ liệu nếu cần
      if (view === 'books' && this.allBooks.length === 0) this.loadAllBooks();
      if (view === 'users' && this.allUsers.length === 0) this.loadAllUsers();
      if ((view === 'activeLoans' || view === 'overdueLoans') && this.allLoans.length === 0) {
        this.loadAllLoans();
      }
    }
  }

  // Các hàm tải dữ liệu chi tiết
  loadAllBooks(): void {
    this.booksService.getBooksList().subscribe((data: Books[]) => this.allBooks = data);
  }

  loadAllUsers(): void {
    this.usersService.getUsersList().subscribe((data: Users[]) => this.allUsers = data);
  }
  
  loadAllLoans(): void {
    this.adminService.getAllLoans().subscribe((data: LoanDetails[]) => this.allLoans = data);
  }

  // Hàm tiện ích
  private handleError(err: any): void {
    this.errorMessage = "Could not load dashboard data.";
    this.isLoading = false;
    console.error(err);
  }
}