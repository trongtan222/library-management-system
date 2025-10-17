import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { BooksService } from '../services/books.service';
import { CirculationService } from '../services/circulation.service';
import { ToastrService } from 'ngx-toastr';

type Row = {
  id: number;
  name: string;
  author: string;
  genre: string;
  available: number;
  publishedYear?: number | null;
  isbn?: string | null;
};

@Component({
  selector: 'app-books-list',
  templateUrl: './books-list.component.html',
  styleUrls: ['./books-list.component.css']
})
export class BooksListComponent implements OnInit {

  rows: Row[] = [];
  filteredRows: Row[] = [];
  pagedRows: Row[] = [];

  loading = new Set<number>();
  page = 1;
  pageSize = 5;
  totalPages = 0;
  pageSizes = [5, 10, 20, 50];
  searchTerm = '';

  // State cho borrow modal
  showBorrow = false;
  borrowingRow: Row | null = null;
  borrowForm = {
    memberId: 0,
    dueDate: '',
  };

  // State cho reserve modal
  showReserve = false;
  reservingRow: Row | null = null;
  reserveForm = {
    memberId: 0
  };
  
  // State cho confirm delete modal
  bookToDelete: Row | null = null;

  constructor(
    private booksService: BooksService,
    private circulation: CirculationService,
    private router: Router,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void { this.getBooks(); }

  private getBooks() {
    this.booksService.getBooksList().subscribe({
      next: (data: any[]) => {
        this.rows = data.map((b: any) => ({
          id: b.id ?? b.bookId,
          name: b.name ?? b.bookName,
          author: b.author ?? b.bookAuthor,
          genre: b.genre ?? b.bookGenre,
          available: b.numberOfCopiesAvailable ?? b.noOfCopies ?? 0,
          publishedYear: b.publishedYear ?? null,
          isbn: b.isbn ?? null
        }));
        this.applyFilters(1);
      },
      error: (err: any) => this.toastError(err)
    });
  }

  private norm(s: string): string {
    return (s || '')
      .toLowerCase()
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '');
  }

  private match(row: Row, term: string): boolean {
    if (!term) return true;
    const t = this.norm(term);
    return (
      String(row.id).includes(t) ||
      this.norm(row.name).includes(t) ||
      this.norm(row.author).includes(t) ||
      this.norm(row.genre).includes(t) ||
      this.norm(String(row.publishedYear ?? '')).includes(t) ||
      this.norm(row.isbn ?? '').includes(t)
    );
  }

  applyFilters(goTo?: number) {
    const term = this.searchTerm.trim();
    this.filteredRows = this.rows.filter(r => this.match(r, term));
    this.rebuildPage(goTo ?? this.page);
  }

  private rebuildPage(goTo: number) {
    this.totalPages = Math.max(1, Math.ceil(this.filteredRows.length / this.pageSize));
    this.page = Math.min(Math.max(1, goTo), this.totalPages);
    const start = (this.page - 1) * this.pageSize;
    const end = start + this.pageSize;
    this.pagedRows = this.filteredRows.slice(start, end);
  }

  setPage(p: number) { this.rebuildPage(p); }
  prevPage() { if (this.page > 1) this.rebuildPage(this.page - 1); }
  nextPage() { if (this.page < this.totalPages) this.rebuildPage(this.page + 1); }
  changePageSize(s: number) { this.pageSize = +s; this.applyFilters(1); }


  updateBook(bookId: number) { this.router.navigate(['update-book', bookId]); }

  bookDetails(bookId: number) { this.router.navigate(['book-details', bookId]); }

  // --- Borrow Modal Logic ---
  openBorrow(row: Row) {
    if (row.available <= 0 || this.loading.has(row.id)) return;
    this.borrowingRow = row;
    this.borrowForm = {
      memberId: 0,
      dueDate: this.defaultDueDate(14),
    };
    this.showBorrow = true;
  }

  closeBorrow() {
    this.showBorrow = false;
    this.borrowingRow = null;
  }

  submitBorrow() {
    if (!this.borrowingRow) return;
    const { memberId, dueDate } = this.borrowForm;
    if (!memberId || !dueDate || memberId <= 0) {
      return this.toastError({ message: 'Please enter a valid Member ID and due date.' });
    }
    const loanDays = this.daysFromToday(dueDate);
    if (loanDays <= 0) {
      return this.toastError({ message: 'Due date must be in the future.' });
    }
    const row = this.borrowingRow;
    this.loading.add(row.id);
    this.circulation.loan({ bookId: row.id, memberId: Number(memberId), loanDays }).subscribe({
      next: () => {
        this.loading.delete(row.id);
        this.toastOk(`Loan created for "${row.name}"`);
        this.closeBorrow();
        this.getBooks();
      },
      error: (err: any) => {
        this.loading.delete(row.id);
        this.toastError(err);
      }
    });
  }

  // --- Reserve Modal Logic ---
  openReserve(row: Row) {
    if (this.loading.has(row.id)) return;
    this.reservingRow = row;
    this.reserveForm = { memberId: 0 };
    this.showReserve = true;
  }

  closeReserve() {
    this.showReserve = false;
    this.reservingRow = null;
  }

  submitReserve() {
    if (!this.reservingRow) return;
    const { memberId } = this.reserveForm;
    if (!memberId || memberId <= 0) {
      return this.toastError({ message: 'Please enter a valid Member ID.' });
    }
    const row = this.reservingRow;
    this.loading.add(row.id);
    this.circulation.reserve({ bookId: row.id, memberId: Number(memberId) }).subscribe({
      next: () => {
        this.loading.delete(row.id);
        this.toastOk(`Reserved "${row.name}" for Member ID ${memberId}.`);
        this.closeReserve();
      },
      error: (err: any) => {
        this.loading.delete(row.id);
        this.toastError(err);
      }
    });
  }
  
  isLoading(id: number) { return this.loading.has(id); }

  private defaultDueDate(addDays: number): string {
    const d = new Date();
    d.setDate(d.getDate() + addDays);
    return d.toISOString().slice(0, 10);
  }

  private daysFromToday(dateStr: string): number {
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    const due = new Date(dateStr);
    const diff = Math.ceil((due.getTime() - today.getTime()) / (1000 * 60 * 60 * 24));
    return diff;
  }

  deleteBook(book: Row): void {
    this.bookToDelete = book; // Mở modal xác nhận
  }

  confirmDelete(): void {
    if (!this.bookToDelete) return;
    
    const bookToDeleteCopy = { ...this.bookToDelete }; // Tạo bản sao để tránh lỗi
    this.bookToDelete = null; // Đóng modal ngay

    this.booksService.deleteBook(bookToDeleteCopy.id).subscribe({
      next: () => {
        this.toastOk(`Book "${bookToDeleteCopy.name}" was deleted.`);
        this.getBooks(); // Tải lại danh sách
      },
      error: (err: any) => this.toastError(err)
    });
  }

  private toastOk(msg: string) { 
    this.toastr.success(msg, 'Thành công'); 
  }

  private toastError(err: any) {
    const msg = err?.error?.message || err?.message || 'Đã có lỗi không mong muốn xảy ra.';
    this.toastr.error(msg, 'Lỗi');
    console.error(err);
  }
}