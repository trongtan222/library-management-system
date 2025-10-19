import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { Books } from '../models/books';
import { BooksService } from '../services/books.service';
import { UserAuthService } from '../services/user-auth.service';
import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  searchTerm: string = '';
  categories = [
    'Tiểu thuyết',
    'Khoa học',
    'Lịch sử',
    'Phát triển bản thân',
    'Khoa học máy tính',
    'Kinh doanh',
    'Giả tưởng',
    'Trinh thám'
  ];
  newestBooks$!: Observable<Books[]>;

  constructor(
    public userAuthService: UserAuthService,
    private booksService: BooksService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.newestBooks$ = this.booksService.getNewestBooks().pipe(
      map((data: any[]) => (data || []).map(b => ({
          id: b.id,
          name: b.name,
          author: b.author,
          genre: b.genre,
          publishedYear: b.publishedYear,
          isbn: b.isbn,
          numberOfCopiesAvailable: b.numberOfCopiesAvailable
        }))
      ),
      catchError((err: HttpErrorResponse) => {
        console.error("Could not load newest books", err);
        return of([]);
      })
    );
  }

  searchBooks(): void {
    if (this.searchTerm.trim()) {
      this.router.navigate(['/borrow-book'], { queryParams: { search: this.searchTerm } });
    } else {
      this.router.navigate(['/borrow-book']);
    }
  }
}