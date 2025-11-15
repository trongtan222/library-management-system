import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private baseUrl = environment.apiBaseUrl;

  constructor() {}

  /**
   * Get the API base URL
   */
  getBaseUrl(): string {
    return this.baseUrl;
  }

  /**
   * Build full API endpoint URL
   * @param endpoint - API endpoint path (e.g., '/admin/books')
   * @returns Full URL
   */
  buildUrl(endpoint: string): string {
    // Remove leading slash if present
    const path = endpoint.startsWith('/') ? endpoint : '/' + endpoint;
    return `${this.baseUrl}${path}`;
  }

  /**
   * Get authentication endpoints
   */
  getAuthEndpoints() {
    return {
      login: this.buildUrl('/auth/authenticate'),
      register: this.buildUrl('/auth/register'),
    };
  }

  /**
   * Get books endpoints
   */
  getBooksEndpoints() {
    return {
      list: this.buildUrl('/public/books'),
      getById: (id: number) => this.buildUrl(`/admin/books/${id}`),
      create: this.buildUrl('/admin/books'),
      update: (id: number) => this.buildUrl(`/admin/books/${id}`),
      delete: (id: number) => this.buildUrl(`/admin/books/${id}`),
      authors: this.buildUrl('/admin/books/authors'),
      categories: this.buildUrl('/admin/books/categories'),
    };
  }

  /**
   * Get circulation/borrow endpoints
   */
  getCirculationEndpoints() {
    return {
      borrow: this.buildUrl('/user/borrow'),
      returnBook: this.buildUrl('/user/return'),
      myBorrows: this.buildUrl('/user/my-borrows'),
    };
  }

  /**
   * Get user endpoints
   */
  getUserEndpoints() {
    return {
      list: this.buildUrl('/admin/users'),
      getById: (id: number) => this.buildUrl(`/admin/users/${id}`),
      create: this.buildUrl('/admin/users'),
      update: (id: number) => this.buildUrl(`/admin/users/${id}`),
      delete: (id: number) => this.buildUrl(`/admin/users/${id}`),
    };
  }
}
