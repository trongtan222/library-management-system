import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { BooksService } from '../../services/books.service';

@Component({
  selector: 'app-import-export',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './import-export.component.html',
  styleUrls: ['./import-export.component.css']
})
export class ImportExportComponent {
  booksFile?: File;
  usersFile?: File;
  isUploadingBooks = false;
  isUploadingUsers = false;
  isExportingBooks = false;
  isExportingUsers = false;

  constructor(private booksService: BooksService, private toastr: ToastrService) {}

  onFileChange(event: Event, type: 'books' | 'users') {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    if (type === 'books') this.booksFile = file || undefined;
    else this.usersFile = file || undefined;
  }

  async importBooks() {
    if (!this.booksFile) { this.toastr.error('Chọn file sách (CSV/Excel)'); return; }
    this.isUploadingBooks = true;
    try {
      await this.booksService.importBooks(this.booksFile).toPromise();
      this.toastr.success('Nhập sách thành công');
      this.booksFile = undefined;
    } catch (e) {
      this.toastr.error('Nhập sách thất bại');
    } finally {
      this.isUploadingBooks = false;
    }
  }

  async importUsers() {
    if (!this.usersFile) { this.toastr.error('Chọn file người dùng (CSV/Excel)'); return; }
    this.isUploadingUsers = true;
    try {
      await this.booksService.importUsers(this.usersFile).toPromise();
      this.toastr.success('Nhập người dùng thành công');
      this.usersFile = undefined;
    } catch (e) {
      this.toastr.error('Nhập người dùng thất bại');
    } finally {
      this.isUploadingUsers = false;
    }
  }

  async exportBooks() {
    this.isExportingBooks = true;
    try {
      const blob = await this.booksService.exportBooks().toPromise();
      if (blob) this.downloadBlob(blob, 'books_export.xlsx');
      this.toastr.success('Xuất sách thành công');
    } catch (e) {
      this.toastr.error('Xuất sách thất bại');
    } finally {
      this.isExportingBooks = false;
    }
  }

  async exportUsers() {
    this.isExportingUsers = true;
    try {
      const blob = await this.booksService.exportUsers().toPromise();
      if (blob) this.downloadBlob(blob, 'users_export.xlsx');
      this.toastr.success('Xuất người dùng thành công');
    } catch (e) {
      this.toastr.error('Xuất người dùng thất bại');
    } finally {
      this.isExportingUsers = false;
    }
  }

  private downloadBlob(blob: Blob, filename: string) {
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = filename;
    a.click();
    window.URL.revokeObjectURL(url);
  }
}
