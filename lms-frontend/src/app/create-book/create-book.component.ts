import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgForm } from '@angular/forms';
import { BooksService } from '../services/books.service'; 
// SỬA: Import Book, Author, Category từ model
import { Book, Author, Category } from '../models/book'; 
import { forkJoin } from 'rxjs';

interface BookCreateModel {
  name?: string;
  numberOfCopiesAvailable?: number;
  publishedYear?: number;
  isbn?: string;
  authorIds?: number[];
  categoryIds?: number[];
}

@Component({
    selector: 'app-create-book',
    templateUrl: './create-book.component.html',
    styleUrls: ['./create-book.component.css'],
    standalone: false
})
export class CreateBookComponent implements OnInit {
  // ... (Phần logic còn lại giữ nguyên)
  book: BookCreateModel = {
    authorIds: [],
    categoryIds: []
  }; 
  errorMessage = '';
  submitting = false;

  allAuthors: Author[] = [];
  allCategories: Category[] = [];

  constructor(
    private booksService: BooksService,
    private router: Router
  ) { }

  ngOnInit(): void {
    forkJoin({
      authors: this.booksService.getAllAuthors(),
      categories: this.booksService.getAllCategories()
    }).subscribe({
      next: ({ authors, categories }) => {
        this.allAuthors = authors;
        this.allCategories = categories;
      },
      error: (err) => {
        this.errorMessage = "Không thể tải danh sách tác giả/thể loại.";
        console.error(err);
      }
    });
  }

  saveBook(bookForm: NgForm) {
    if (bookForm.invalid) {
      this.errorMessage = "Please fill in all required fields.";
      return;
    }
    
    this.errorMessage = '';
    this.submitting = true; 

    const payload = {
      name: this.book.name,
      authorIds: this.book.authorIds,
      categoryIds: this.book.categoryIds,
      numberOfCopiesAvailable: this.book.numberOfCopiesAvailable,
      publishedYear: this.book.publishedYear,
      isbn: this.book.isbn
    };

    // Cast payload cho đúng kiểu tham số của createBook
    this.booksService.createBook(payload as any).subscribe({
      next: () => {
        this.router.navigate(['/books']);
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = err.error?.message || "An error occurred while creating the book.";
        this.submitting = false; 
      }
    });
  }
}