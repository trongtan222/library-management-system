import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common'; // <--- Thêm import này (cho *ngIf, *ngFor, date pipe)
import { RouterModule } from '@angular/router'; // <--- Thêm import này (cho routerLink)
import { WishlistService } from '../services/wishlist.service';
import { ToastrService } from 'ngx-toastr';
import { Book } from '../models/book'; // Đảm bảo bạn đã có model Book

@Component({
  selector: 'app-wishlist',
  standalone: true, // <--- Quan trọng: Xác định đây là Standalone Component
  imports: [CommonModule, RouterModule], // <--- Quan trọng: Khai báo các module cần dùng
  templateUrl: './wishlist.component.html',
  styleUrls: ['./wishlist.component.css']
})
export class WishlistComponent implements OnInit {

  wishlistItems: any[] = [];
  isLoading = true;

  constructor(
    private wishlistService: WishlistService,
    private toastr: ToastrService
  ) { }

  ngOnInit(): void {
    this.loadWishlist();
  }

  loadWishlist(): void {
    this.isLoading = true;
    this.wishlistService.getMyWishlist().subscribe({
      next: (data) => {
        this.wishlistItems = data;
        this.isLoading = false;
        console.log('Wishlist data:', data);
      },
      error: (err) => {
        // Chỉ hiện lỗi nếu không phải do chưa đăng nhập (401)
        if (err.status !== 401) {
            this.toastr.error('Không thể tải danh sách yêu thích');
        }
        this.isLoading = false;
      }
    });
  }

  remove(bookId: number): void {
    if(!confirm('Bạn có chắc muốn bỏ sách này khỏi danh sách yêu thích?')) return;

    this.wishlistService.removeFromWishlist(bookId).subscribe({
      next: () => {
        this.toastr.success('Đã xóa khỏi danh sách yêu thích');
        // Xóa item khỏi mảng hiển thị ngay lập tức
        this.wishlistItems = this.wishlistItems.filter(item => item.book.id !== bookId);
      },
      error: () => {
        this.toastr.error('Lỗi khi xóa sách');
      }
    });
  }
}