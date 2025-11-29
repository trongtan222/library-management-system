import { Injectable } from '@angular/core';
import { HttpContext, HttpContextToken } from '@angular/common/http';
import { environment } from '../../environments/environment';

// Tạo Token để đánh dấu API nào không cần gửi Token (Public)
export const IS_PUBLIC_API = new HttpContextToken<boolean>(() => false);

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private baseUrl = environment.apiBaseUrl;
  
  // Google Books API Key (Nên để trong environment.ts, tạm để đây)
  public readonly GOOGLE_BOOKS_API_KEY = 'AIzaSyB2Yrs1oWkbIirD3BmF2lOM7bIE9d3Zn40';

  constructor() {}

  getBaseUrl(): string {
    return this.baseUrl;
  }

  buildUrl(endpoint: string): string {
    const path = endpoint.startsWith('/') ? endpoint : '/' + endpoint;
    return `${this.baseUrl}${path}`;
  }
}