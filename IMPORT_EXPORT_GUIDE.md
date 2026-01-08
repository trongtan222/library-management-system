# Import/Export Hàng Loạt

## API Endpoints (Admin)

- POST `/api/admin/import/users` — body multipart `file` (CSV/XLSX). Dòng đầu là header `Mã SV,Tên,Email,Lớp`. Mã SV trùng sẽ **bỏ qua**; mật khẩu tự sinh 10 ký tự; role mặc định `ROLE_USER`.
- POST `/api/admin/import/books` — body multipart `file` (CSV/XLSX). Header `Tên sách,ISBN,Năm XB,Số lượng,Ảnh bìa (URL)`. Nếu ISBN trùng, hệ thống cập nhật/cộng số lượng thay vì tạo bản ghi mới.
- GET `/api/admin/export/users` — trả về `users_export.xlsx`.
- GET `/api/admin/export/books` — trả về `books_export.xlsx`.
- GET `/api/admin/import/template/users` — tải template users (`users_template.xlsx`).
- GET `/api/admin/import/template/books` — tải template books (`books_template.xlsx`).

## Frontend (Trang Import/Export)

- Đường dẫn: `/admin/import-export`.
- Hỗ trợ CSV/XLSX; có nút tải template; hiển thị tiến trình và thống kê `success/failed/skipped` và lỗi chi tiết per-row.

## Định dạng File Mẫu

- CSV mẫu (có sẵn trong `scripts/templates/`):
  - `scripts/templates/users_template.csv`
  - `scripts/templates/books_template.csv`
- Lưu ý dùng UTF-8, giữ nguyên thứ tự cột.

## Quy tắc xử lý

- User: bỏ qua trùng mã SV (username), role mặc định `ROLE_USER`, mật khẩu tự sinh 10 ký tự, lưu `email`, `studentClass` nếu có.
- Book: nếu ISBN tồn tại → cập nhật bản ghi và số lượng; nếu ISBN trống → luôn tạo mới dựa trên tên.

## Kiểm thử nhanh

1. POST `/api/admin/import/users` với `users_template.csv` → kỳ vọng `successCount=3`, `failedCount=0`.
2. POST `/api/admin/import/books` với `books_template.csv` → kỳ vọng `successCount=3`.
3. GET `/api/admin/export/users` và `/api/admin/export/books` → file tải về mở được bằng Excel.

## Việc còn lại

- Bổ sung unit/integration test cho import service (parse CSV/XLSX, dedupe ISBN/username).
- Thêm log/audit cho các lần import lớn nếu cần.
