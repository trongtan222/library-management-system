import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Book } from '../models/book';
import { BooksService } from '../services/books.service';
import { UserAuthService } from '../services/user-auth.service';
import { ReviewService, BookReviewsSummary } from '../services/review.service';
import { WishlistService } from '../services/wishlist.service'; // <--- MỚI
import { ToastrService } from 'ngx-toastr';
import { ApiService } from '../services/api.service';
import { finalize, takeUntil } from 'rxjs/operators';
import { Subject } from 'rxjs';

@Component({
  selector: 'app-book-details',
  templateUrl: './book-details.component.html',
  styleUrls: ['./book-details.component.css'],
  standalone: false
})
export class BookDetailsComponent implements OnInit, OnDestroy {

  book: Book | null = null;
  isLoading = true;
  isUser = false;
  reviewsSummary: BookReviewsSummary | null = null;
  userCanReview = false;
  isCheckingPermission = true; 
  newReview = { rating: 5, comment: '' };
  
  private destroy$ = new Subject<void>();
  errorMessage: any;

  constructor(
    private route: ActivatedRoute,
    private booksService: BooksService,
    private userAuthService: UserAuthService,
    private wishlistService: WishlistService, // <--- Inject Service
    private router: Router,
    private http: HttpClient,
    private reviewService: ReviewService,
    private toastr: ToastrService,
    private apiService: ApiService
  ) {}

  ngOnInit(): void {
    this.isUser = this.userAuthService.isUser();
    const bookIdParam = this.route.snapshot.paramMap.get('id');
    const bookId = Number(bookIdParam);

    if (bookId) {
      this.loadBookDetails(bookId);
      this.loadReviewsAndCheckPermission(bookId);
    } else {
      this.toastr.error("Không tìm thấy ID sách.", "Lỗi");
      this.isLoading = false;
    }
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private loadBookDetails(id: number): void {
    this.isLoading = true;
    this.booksService.getBookById(id)
      .pipe(
        finalize(() => this.isLoading = false),
        takeUntil(this.destroy$)
      )
      .subscribe({
        next: (data: Book) => {
          this.book = data;
          
          // Nếu chưa có ảnh bìa, tìm từ Google Books
          if (this.book && !this.book.coverUrl) {
            this.findAndSaveCover(this.book);
          }

          // <--- MỚI: Nếu là User, kiểm tra xem sách này đã thích chưa
          if (this.isUser && this.book) {
            this.checkWishlistStatus(this.book.id);
          }
        },
        error: (err) => {
          this.toastr.error('Không thể tải chi tiết sách.', 'Lỗi');
          console.error(err);
        }
      });
  }

  // <--- MỚI: Kiểm tra trạng thái Wishlist
  private checkWishlistStatus(bookId: number): void {
    this.wishlistService.checkStatus(bookId)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (isWishlisted: boolean) => {
          if (this.book) {
            this.book.isWishlisted = isWishlisted;
          }
        },
        error: () => console.warn('Không thể kiểm tra trạng thái Wishlist')
      });
  }

  // <--- MỚI: Xử lý nút bấm Wishlist
  toggleWishlist(): void {
    if (!this.book) return;

    if (this.book.isWishlisted) {
      // Đang thích -> Xóa
      this.wishlistService.removeFromWishlist(this.book.id)
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next: () => {
            this.book!.isWishlisted = false;
            this.toastr.info('Đã xóa khỏi danh sách yêu thích');
          },
          error: () => this.toastr.error('Lỗi khi xóa khỏi wishlist')
        });
    } else {
      // Chưa thích -> Thêm
      this.wishlistService.addToWishlist(this.book.id)
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next: () => {
            this.book!.isWishlisted = true;
            this.toastr.success('Đã thêm vào danh sách yêu thích');
          },
          error: () => this.toastr.error('Lỗi khi thêm vào wishlist')
        });
    }
  }

  loadReviewsAndCheckPermission(bookId: number): void {
    this.reviewService.getReviewsForBook(bookId)
      .pipe(takeUntil(this.destroy$))
      .subscribe(summary => this.reviewsSummary = summary);

    if (this.isUser) {
      this.isCheckingPermission = true;
      this.reviewService.checkIfUserHasReviewed(bookId)
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next: response => {
            this.userCanReview = !response.hasReviewed;
            this.isCheckingPermission = false;
          },
          error: () => {
            this.isCheckingPermission = false;
            this.userCanReview = false;
          }
        });
    } else {
      this.isCheckingPermission = false;
    }
  }
  
  submitReview(): void {
    if (!this.book?.id || this.newReview.rating < 1) {
        this.toastr.error('Vui lòng chọn ít nhất 1 sao.');
        return;
    }

    this.reviewService.addReview(this.book.id, this.newReview)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: () => {
            this.toastr.success('Cảm ơn bạn đã đánh giá! Đánh giá sẽ hiển thị sau khi duyệt.');
            this.userCanReview = false;
        },
        error: (err) => this.toastr.error(err.error?.message || 'Gửi đánh giá thất bại.')
    });
  }

  private findAndSaveCover(book: Book): void {
    const apiKey = this.apiService.GOOGLE_BOOKS_API_KEY; 
    let url = '';
    
    if (book.isbn) {
      url = `https://www.googleapis.com/books/v1/volumes?q=isbn:${book.isbn}&key=${apiKey}`;
    } else {
      const title = encodeURIComponent(book.name);
      const author = book.authors?.length > 0 ? encodeURIComponent(book.authors[0].name) : '';
      url = `https://www.googleapis.com/books/v1/volumes?q=intitle:${title}+inauthor:${author}&key=${apiKey}`;
    }

    this.http.get<any>(url)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response) => {
          const coverUrl = response?.items?.[0]?.volumeInfo?.imageLinks?.thumbnail;
          if (coverUrl) {
            this.setAndSaveCover(book, coverUrl);
          }
        },
        error: () => console.warn('Không thể tìm thấy bìa sách từ Google Books.')
      });
  }

  private setAndSaveCover(book: Book, coverUrl: string): void {
    if (this.book) {
      this.book.coverUrl = coverUrl;
      if (this.userAuthService.isAdmin()) {
        this.booksService.updateBook(book.id, { coverUrl: coverUrl })
          .pipe(takeUntil(this.destroy$))
          .subscribe({
            error: (err) => console.error('Lỗi khi lưu bìa sách:', err)
          });
      }
    }
  }

  navigateToBorrow(): void {
    this.router.navigate(['/borrow-book']);
  }
}