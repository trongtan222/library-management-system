import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged, switchMap } from 'rxjs/operators';
import { Book } from 'src/app/models/book';
import { User } from 'src/app/models/user';
import { BooksService } from 'src/app/services/books.service';
import { CirculationService } from 'src/app/services/circulation.service';
import { UsersService } from 'src/app/services/users.service';

@Component({
  selector: 'app-create-loan',
  templateUrl: './create-loan.component.html',
  styleUrls: ['./create-loan.component.css'],
  standalone: false
})
export class CreateLoanComponent implements OnInit {

  // State tìm kiếm User
  users: User[] = [];
  selectedUser: User | null = null;
  userSearchTerm = '';
  
  // State tìm kiếm Book
  books: Book[] = [];
  selectedBook: Book | null = null;
  bookSearchTerm = '';

  // Loan config
  loanDays = 14;
  isLoading = false;

  constructor(
    private usersService: UsersService,
    private booksService: BooksService,
    private circulationService: CirculationService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    // Load danh sách user ban đầu (nếu cần) hoặc để trống chờ search
    this.searchUsers();
  }

  // --- USER SEARCH ---
  onUserSearchChange(term: string) {
    this.userSearchTerm = term;
    // Đơn giản hóa: filter trên client nếu danh sách ít, 
    // hoặc gọi API search nếu danh sách nhiều.
    // Ở đây mình gọi lại API lấy list và filter client cho nhanh (demo)
    this.searchUsers();
  }

  searchUsers() {
    this.usersService.getUsersList().subscribe({
      next: (data) => {
        if (this.userSearchTerm.trim()) {
          const term = this.userSearchTerm.toLowerCase();
          this.users = data.filter(u => 
            u.username.toLowerCase().includes(term) || 
            u.name.toLowerCase().includes(term)
          ).slice(0, 5); // Lấy 5 kết quả đầu
        } else {
          this.users = []; // Xóa list nếu không tìm
        }
      }
    });
  }

  selectUser(user: User) {
    this.selectedUser = user;
    this.users = []; // Ẩn danh sách gợi ý
    this.userSearchTerm = '';
  }

  clearSelectedUser() {
    this.selectedUser = null;
  }

  // --- BOOK SEARCH ---
  onBookSearchChange(term: string) {
    this.bookSearchTerm = term;
    if (!term.trim()) {
      this.books = [];
      return;
    }
    
    // Gọi API search public books
    this.booksService.getPublicBooks(true, term, null, 0, 5).subscribe({
      next: (page) => {
        this.books = page.content;
      }
    });
  }

  selectBook(book: Book) {
    this.selectedBook = book;
    this.books = []; // Ẩn danh sách gợi ý
    this.bookSearchTerm = '';
  }

  clearSelectedBook() {
    this.selectedBook = null;
  }

  // --- SUBMIT ---
  createLoan() {
    if (!this.selectedUser || !this.selectedBook) {
      this.toastr.warning('Vui lòng chọn Người dùng và Sách.');
      return;
    }

    this.isLoading = true;
    const payload = {
      bookId: this.selectedBook.id,
      memberId: this.selectedUser.userId,
      loanDays: this.loanDays,
      // Admin tạo thì có thể lấy tên/lớp từ User object nếu cần lưu history
      studentName: this.selectedUser.name,
      studentClass: '' // Hoặc thêm trường nhập lớp nếu muốn
    };

    this.circulationService.loan(payload).subscribe({
      next: () => {
        this.toastr.success(`Đã cấp sách "${this.selectedBook?.name}" cho ${this.selectedUser?.name}`);
        this.resetForm();
      },
      error: (err) => {
        this.toastr.error(err.error?.message || 'Tạo phiếu mượn thất bại.');
        this.isLoading = false;
      },
      complete: () => this.isLoading = false
    });
  }

  resetForm() {
    this.selectedBook = null;
    this.selectedUser = null;
    this.loanDays = 14;
    this.isLoading = false;
  }
}