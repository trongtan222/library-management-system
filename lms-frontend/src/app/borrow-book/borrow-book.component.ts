import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { BooksService, Page } from '../services/books.service';
import { CirculationService, BorrowCreate } from '../services/circulation.service';
import { UserAuthService } from '../services/user-auth.service';
import { Book } from '../models/book';
import { Subject } from 'rxjs';
import { takeUntil, finalize } from 'rxjs/operators';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-borrow-book',
  templateUrl: './borrow-book.component.html',
  styleUrls: ['./borrow-book.component.css'],
  standalone: false
})
export class BorrowBookComponent implements OnInit, OnDestroy {
  
  books: Book[] = [];
  filteredBooks: Book[] = []; // Biến quan trọng để hiển thị
  genres: string[] = [];
  searchTerm: string = '';
  selectedGenre: string = '';
  
  currentPage: number = 1;
  pageSize: number = 10;
  totalElements: number = 0;
  totalPages: number = 0;
  
  loading = new Set<number>();
  isLoadingPage = false; // Biến loading cho toàn trang
  userId: number | null = null;
  userName: string | null = null;
  
  // State modals
  showModal = false;
  showConfirmModal = false;
  showReserveModal = false;
  selectedBook: Book | null = null;
  enableScanner = false;

  borrowData = {
    studentName: '',
    studentClass: '',
    quantity: 1,
    loanDays: 14
  };

  private destroy$ = new Subject<void>();
  successMessage: any;
  errorMessage: any;
isScanning: any;

  constructor(
    private booksService: BooksService,
    private userAuthService: UserAuthService,
    private circulationService: CirculationService,
    private router: Router,
    private route: ActivatedRoute,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.userId = this.userAuthService.getUserId();
    this.userName = this.userAuthService.getName();
    
    if (this.userName) this.borrowData.studentName = this.userName;

    if (this.userId == null) {
      this.router.navigate(['/login']);
      return;
    }
    
    this.route.queryParams.pipe(takeUntil(this.destroy$)).subscribe(params => {
        this.searchTerm = params['search'] || '';
        this.loadBooks();
    });

    this.loadAllGenres();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  // --- HÀM QUAN TRỌNG ĐÃ SỬA ---
  loadBooks(): void {
    this.isLoadingPage = true;
    this.booksService.getPublicBooks(
      true, 
      this.searchTerm, 
      this.selectedGenre,
      this.currentPage - 1,
      this.pageSize
    ).pipe(
      takeUntil(this.destroy$),
      finalize(() => this.isLoadingPage = false)
    ).subscribe({
      next: (response: any) => {
        console.log('Dữ liệu nhận được:', response); // F12 xem log này có dữ liệu không

        // Xử lý linh hoạt cả 2 trường hợp trả về
        if (response && response.content) {
            this.books = response.content; // Chuẩn Spring Page
            this.totalPages = response.totalPages;
            this.totalElements = response.totalElements;
        } else if (Array.isArray(response)) {
            this.books = response; // Nếu trả về List thường
        } else {
            this.books = [];
        }

        // Gán dữ liệu hiển thị
        this.filteredBooks = [...this.books];
        this.isLoadingPage = false;
      },
      error: (err) => {
        console.error('Lỗi tải sách:', err);
        this.errorMessage = "Không thể tải danh sách sách. Vui lòng kiểm tra kết nối.";
        this.isLoadingPage = false;
      }
    });
  }
  // -----------------------------
  
  loadAllGenres(): void {
    this.booksService.getPublicBooks(false, '', '', 0, 1000)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (page: any) => {
          const content = page.content || page; // Xử lý cả 2 trường hợp
          if (content && Array.isArray(content)) {
            const allGenres = content
              .flatMap((b: Book) => b.categories || [])
              .map((c: any) => c.name)
              .filter(Boolean);
            this.genres = [...new Set(allGenres)];
          }
        }
      });
  }

  search(): void { this.onFilterChange(); }

  onFilterChange(): void {
    this.currentPage = 1;
    this.loadBooks();
  }

  resetFilters(): void {
    this.searchTerm = '';
    this.selectedGenre = '';
    this.currentPage = 1;
    this.loadBooks();
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages) return;
    this.currentPage = page;
    this.loadBooks();
  }

  openBorrowForm(book: Book) {
    this.selectedBook = book;
    this.showModal = true;
    this.showConfirmModal = true;
    this.borrowData.quantity = 1;
    this.borrowData.loanDays = 14;
    this.borrowData.studentClass = '';
    if (this.userName) this.borrowData.studentName = this.userName;
  }

  closeModal() {
    this.showModal = false;
    this.showConfirmModal = false;
    this.showReserveModal = false;
    this.selectedBook = null;
    this.errorMessage = null; // Reset lỗi cũ
  }

  // Đã sửa thành hàm để gọi từ HTML
  closeReserveModal() {
    this.closeModal();
  }

  confirmBorrow(): void {
    if (!this.selectedBook || !this.userId) return;

    if (!this.borrowData.studentName || !this.borrowData.studentClass) {
      this.toastr.warning('Vui lòng điền đầy đủ Họ tên và Lớp!');
      return;
    }

    this.loading.add(this.selectedBook.id);

    const payload: BorrowCreate = {
      bookId: this.selectedBook.id,
      memberId: this.userId,
      loanDays: this.borrowData.loanDays,
      studentName: this.borrowData.studentName,
      studentClass: this.borrowData.studentClass
    };

    this.circulationService.loan(payload)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: () => {
          this.toastr.success(`Đã mượn thành công sách "${this.selectedBook?.name}".`);
          this.loadBooks();
          this.closeModal();
        },
        error: (err: HttpErrorResponse) => {
          this.errorMessage = err.error?.message || 'Lỗi khi mượn sách.';
          this.toastr.error(this.errorMessage);
          if(this.selectedBook) this.loading.delete(this.selectedBook.id);
        },
        complete: () => {
          if (this.selectedBook) this.loading.delete(this.selectedBook.id);
        }
      });
  }

  openReserveModal(book: Book): void {
    this.selectedBook = book;
    this.showReserveModal = true;
    if (this.userName) this.borrowData.studentName = this.userName;
  }

  confirmReserve(): void {
    if (!this.selectedBook || !this.userId) return;

    if (!this.borrowData.studentName || !this.borrowData.studentClass) {
      this.toastr.warning('Vui lòng điền đầy đủ thông tin để đặt trước!');
      return;
    }

    this.loading.add(this.selectedBook.id);

    const payload = {
      bookId: this.selectedBook.id,
      memberId: this.userId,
      studentName: this.borrowData.studentName,
      studentClass: this.borrowData.studentClass
    };

    this.circulationService.reserve(payload)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: () => {
          this.toastr.success(`Đã đặt trước thành công cuốn "${this.selectedBook?.name}".`);
          this.closeModal();
        },
        error: (err: HttpErrorResponse) => {
          this.errorMessage = err.error?.message || 'Lỗi khi đặt trước sách.';
          this.toastr.error(this.errorMessage);
          if(this.selectedBook) this.loading.delete(this.selectedBook.id);
        },
        complete: () => {
          if(this.selectedBook) this.loading.delete(this.selectedBook.id);
        }
      });
  }

  toggleScanner() {
    this.enableScanner = !this.enableScanner;
  }

  onCodeResult(resultString: string) {
    if (!resultString) return;
    this.enableScanner = false;
    const bookId = Number(resultString);
    if (isNaN(bookId)) {
      this.toastr.error('Mã QR không hợp lệ!');
      return;
    }

    this.booksService.getBookById(bookId)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (book) => {
          if (book) {
            this.openBorrowForm(book);
            this.toastr.info('Đã tìm thấy sách từ mã QR!');
          } else {
            this.toastr.error('Không tìm thấy sách với ID này.');
          }
        },
        error: () => this.toastr.error('Lỗi khi tìm sách từ mã QR.')
      });
  }
}