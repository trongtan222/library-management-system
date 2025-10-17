import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgForm } from '@angular/forms';
import { BooksService } from '../services/books.service';
import { Books } from '../models/books';

@Component({
  selector: 'app-create-book',
  templateUrl: './create-book.component.html',
  styleUrls: ['./create-book.component.css']
})
export class CreateBookComponent implements OnInit {

  book: Partial<Books> = {};
  errorMessage = '';
  submitting = false; // Bổ sung thuộc tính submitting

  constructor(
    private booksService: BooksService,
    private router: Router
  ) { }

  ngOnInit(): void { }

  saveBook(bookForm: NgForm) {
    if (bookForm.invalid) {
      this.errorMessage = "Please fill in all required fields.";
      return;
    }
    
    this.errorMessage = '';
    this.submitting = true; // Bắt đầu submitting

    const payload: Partial<Books> = {
      name: this.book.name,
      author: this.book.author,
      genre: this.book.genre,
      // SỬA LỖI Ở ĐÂY
      numberOfCopiesAvailable: this.book.numberOfCopiesAvailable,
      publishedYear: this.book.publishedYear,
      isbn: this.book.isbn
    };

    this.booksService.createBook(payload).subscribe({
      next: () => {
        this.router.navigate(['/books']);
        // this.submitting = false; // Không cần thiết vì đã chuyển trang
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = "An error occurred while creating the book.";
        this.submitting = false; // Dừng submitting khi có lỗi
      }
    });
  }
}
