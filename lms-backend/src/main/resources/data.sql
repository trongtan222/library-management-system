-- ============================================================
-- DỮ LIỆU MẪU THƯ VIỆN TRƯỜNG THCS PHƯƠNG TÚ (BẢN MỞ RỘNG)
-- ============================================================

USE lms_db;
SET FOREIGN_KEY_CHECKS = 0;

-- 1. XÓA DỮ LIỆU CŨ
TRUNCATE TABLE book_authors;
TRUNCATE TABLE book_categories;
TRUNCATE TABLE books;
TRUNCATE TABLE authors;
TRUNCATE TABLE categories;

-- 2. THÊM DANH MỤC
INSERT INTO categories (id, name) VALUES
(1, 'Sách Giáo Khoa'),
(2, 'Sách Tham Khảo'),
(3, 'Văn Học Việt Nam'),
(4, 'Văn Học Nước Ngoài'),
(5, 'Truyện Tranh'),
(6, 'Kỹ Năng Sống'),
(7, 'Khoa Học - Khám Phá'),
(8, 'Lịch Sử - Địa Lý'),
(9, 'Tiếng Anh');

-- 3. THÊM TÁC GIẢ
INSERT INTO authors (id, name) VALUES
(1, 'Bộ Giáo Dục & Đào Tạo'),
(2, 'Nguyễn Nhật Ánh'),
(3, 'Tô Hoài'),
(4, 'Nam Cao'),
(5, 'Đoàn Giỏi'),
(6, 'J.K. Rowling'),
(7, 'Fujiko F. Fujio'),
(8, 'Arthur Conan Doyle'),
(9, 'Nguyễn Du'),
(10, 'Vũ Trọng Phụng'),
(11, 'Rosie Nguyễn'),
(12, 'Antoine de Saint-Exupéry'),
(13, 'Hector Malot'),
(14, 'Gosho Aoyama'),
(15, 'Nhiều Tác Giả'),
(16, 'Thạch Lam'),
(17, 'Nguyên Hồng'),
(18, 'Mark Twain'),
(19, 'Daniel Defoe'),
(20, 'Dale Carnegie');

-- 4. THÊM SÁCH
INSERT INTO books (id, name, published_year, number_of_copies_available, cover_url, isbn) VALUES

-- === KHỐI 6 (Chân trời sáng tạo / Kết nối tri thức) ===
(1, 'Ngữ Văn 6 - Tập 1 (CTST)', 2024, 50, 'https://cdn0.fahasa.com/media/catalog/product/8/9/8935279136912.jpg', 'SGK-6-NV1'),
(2, 'Ngữ Văn 6 - Tập 2 (CTST)', 2024, 50, 'https://cdn0.fahasa.com/media/catalog/product/8/9/8935279136929.jpg', 'SGK-6-NV2'),
(3, 'Toán 6 - Tập 1 (KNTT)', 2024, 45, 'https://cdn0.fahasa.com/media/catalog/product/8/9/8936036286039.jpg', 'SGK-6-T1'),
(4, 'Toán 6 - Tập 2 (KNTT)', 2024, 45, 'https://cdn0.fahasa.com/media/catalog/product/8/9/8936036286046.jpg', 'SGK-6-T2'),
(5, 'Tiếng Anh 6 (Global Success)', 2024, 40, 'https://cdn0.fahasa.com/media/catalog/product/8/9/8936036286176.jpg', 'SGK-6-AV'),
(6, 'Khoa Học Tự Nhiên 6', 2024, 35, 'https://cdn0.fahasa.com/media/catalog/product/8/9/8936036286091.jpg', 'SGK-6-KHTN'),
(7, 'Lịch Sử và Địa Lí 6', 2024, 35, 'https://cdn0.fahasa.com/media/catalog/product/8/9/8936036286138.jpg', 'SGK-6-SD'),

-- === KHỐI 7 (Cánh Diều / KNTT) ===
(8, 'Ngữ Văn 7 - Tập 1 (Cánh Diều)', 2024, 40, 'https://cdn0.fahasa.com/media/catalog/product/8/9/8935262406602.jpg', 'SGK-7-NV1'),
(9, 'Ngữ Văn 7 - Tập 2 (Cánh Diều)', 2024, 40, 'https://cdn0.fahasa.com/media/catalog/product/8/9/8935262406619.jpg', 'SGK-7-NV2'),
(10, 'Toán 7 - Tập 1 (KNTT)', 2024, 40, 'https://cdn0.fahasa.com/media/catalog/product/8/9/8936036286121.jpg', 'SGK-7-T1'),
(11, 'Toán 7 - Tập 2 (KNTT)', 2024, 40, 'https://cdn0.fahasa.com/media/catalog/product/8/9/8936036286138.jpg', 'SGK-7-T2'),
(12, 'Tiếng Anh 7 (Global Success)', 2024, 40, 'https://cdn0.fahasa.com/media/catalog/product/8/9/8936036288521.jpg', 'SGK-7-AV'),
(13, 'Khoa Học Tự Nhiên 7', 2024, 35, 'https://cdn0.fahasa.com/media/catalog/product/8/9/8936036287159.jpg', 'SGK-7-KHTN'),

-- === KHỐI 8 (Kết nối tri thức) ===
(14, 'Ngữ Văn 8 - Tập 1', 2024, 40, 'https://cdn0.fahasa.com/media/catalog/product/8/9/8936036289252.jpg', 'SGK-8-NV1'),
(15, 'Ngữ Văn 8 - Tập 2', 2024, 40, 'https://cdn0.fahasa.com/media/catalog/product/8/9/8936036289269.jpg', 'SGK-8-NV2'),
(16, 'Toán 8 - Tập 1', 2024, 40, 'https://cdn0.fahasa.com/media/catalog/product/8/9/8936036289191.jpg', 'SGK-8-T1'),
(17, 'Toán 8 - Tập 2', 2024, 40, 'https://cdn0.fahasa.com/media/catalog/product/8/9/8936036289207.jpg', 'SGK-8-T2'),
(18, 'Tiếng Anh 8 (Global Success)', 2024, 35, 'https://cdn0.fahasa.com/media/catalog/product/8/9/8936036289368.jpg', 'SGK-8-AV'),
(19, 'Khoa Học Tự Nhiên 8', 2024, 30, 'https://cdn0.fahasa.com/media/catalog/product/8/9/8936036289276.jpg', 'SGK-8-KHTN'),
(20, 'Lịch Sử và Địa Lí 8', 2024, 30, 'https://cdn0.fahasa.com/media/catalog/product/8/9/8936036289313.jpg', 'SGK-8-SD'),

-- === KHỐI 9 (Lớp cuối cấp) ===
(21, 'Ngữ Văn 9 - Tập 1', 2024, 50, 'https://cdn0.fahasa.com/media/catalog/product/i/m/image_195509_1_43156.jpg', 'SGK-9-NV1'),
(22, 'Ngữ Văn 9 - Tập 2', 2024, 50, 'https://cdn0.fahasa.com/media/catalog/product/i/m/image_195509_1_43157.jpg', 'SGK-9-NV2'),
(23, 'Toán 9 - Tập 1', 2024, 50, 'https://cdn0.fahasa.com/media/catalog/product/i/m/image_244718.jpg', 'SGK-9-T1'),
(24, 'Toán 9 - Tập 2', 2024, 50, 'https://cdn0.fahasa.com/media/catalog/product/i/m/image_244719.jpg', 'SGK-9-T2'),
(25, 'Tiếng Anh 9', 2024, 45, 'https://cdn0.fahasa.com/media/catalog/product/i/m/image_195509_1_43172.jpg', 'SGK-9-AV'),
(26, 'Vật Lí 9', 2024, 35, 'https://cdn0.fahasa.com/media/catalog/product/i/m/image_195509_1_43162.jpg', 'SGK-9-LY'),
(27, 'Hóa Học 9', 2024, 35, 'https://cdn0.fahasa.com/media/catalog/product/i/m/image_180353.jpg', 'SGK-9-HOA'),
(28, 'Sinh Học 9', 2024, 35, 'https://cdn0.fahasa.com/media/catalog/product/i/m/image_195509_1_43166.jpg', 'SGK-9-SINH'),
(29, 'Lịch Sử 9', 2024, 30, 'https://cdn0.fahasa.com/media/catalog/product/i/m/image_195509_1_43168.jpg', 'SGK-9-SU'),
(30, 'Địa Lí 9', 2024, 30, 'https://cdn0.fahasa.com/media/catalog/product/i/m/image_195509_1_43169.jpg', 'SGK-9-DIA'),

-- === SÁCH THAM KHẢO & ÔN LUYỆN ===
(31, '500 Bài Tập Vật Lí 6', 2022, 15, 'https://cdn0.fahasa.com/media/catalog/product/5/0/500-bai-tap-vat-li-6_1.jpg', 'TK-LY6'),
(32, 'Bồi Dưỡng Học Sinh Giỏi Toán 7', 2023, 10, 'https://cdn0.fahasa.com/media/catalog/product/b/o/boi-duong-hoc-sinh-gioi-toan-7-tap-1-hinh-hoc_1.jpg', 'TK-TOAN7'),
(33, 'Giải Bài Tập Hóa Học 8', 2023, 12, 'https://cdn0.fahasa.com/media/catalog/product/g/i/giai-bai-tap-hoa-hoc-8.jpg', 'TK-HOA8'),
(34, 'Ôn Luyện Thi Vào Lớp 10 Môn Toán', 2024, 30, 'https://cdn0.fahasa.com/media/catalog/product/o/n/on-luyen-thi-vao-lop-10-thpt-nam-hoc-2024-2025-mon-toan.jpg', 'TK-10-TOAN'),
(35, 'Ôn Luyện Thi Vào Lớp 10 Môn Văn', 2024, 30, 'https://cdn0.fahasa.com/media/catalog/product/o/n/on-luyen-thi-vao-lop-10-thpt-nam-hoc-2024-2025-mon-ngu-van.jpg', 'TK-10-VAN'),
(36, 'Ôn Luyện Thi Vào Lớp 10 Môn Anh', 2024, 30, 'https://cdn0.fahasa.com/media/catalog/product/o/n/on-luyen-thi-vao-lop-10-thpt-nam-hoc-2024-2025-mon-tieng-anh.jpg', 'TK-10-AV'),

-- === VĂN HỌC TUỔI XANH (Nguyễn Nhật Ánh) ===
(37, 'Cho Tôi Xin Một Vé Đi Tuổi Thơ', 2008, 15, 'https://cdn0.fahasa.com/media/catalog/product/c/h/cho-toi-xin-mot-ve-di-tuoi-tho-bia-mem-2023_1.jpg', 'NNA-001'),
(38, 'Mắt Biếc', 1990, 12, 'https://cdn0.fahasa.com/media/catalog/product/m/a/mat-biec-bia-mem-2019.jpg', 'NNA-002'),
(39, 'Kính Vạn Hoa (Bộ 3 Tập)', 2000, 8, 'https://cdn0.fahasa.com/media/catalog/product/8/9/8934974148183.jpg', 'NNA-003'),
(40, 'Cô Gái Đến Từ Hôm Qua', 1989, 10, 'https://cdn0.fahasa.com/media/catalog/product/c/o/co-gai-den-tu-hom-qua-bia-mem-2018.jpg', 'NNA-004'),
(41, 'Tôi Thấy Hoa Vàng Trên Cỏ Xanh', 2010, 14, 'https://cdn0.fahasa.com/media/catalog/product/t/o/toi-thay-hoa-vang-tren-co-xanh-bia-mem-2018.jpg', 'NNA-005'),
(42, 'Bồ Câu Không Đưa Thư', 1993, 8, 'https://cdn0.fahasa.com/media/catalog/product/i/m/image_195509_1_20701.jpg', 'NNA-006'),
(43, 'Ngồi Khóc Trên Cây', 2013, 10, 'https://cdn0.fahasa.com/media/catalog/product/n/g/ngoi_khoc_tren_cay_tai_ban_2018.jpg', 'NNA-007'),

-- === VĂN HỌC VIỆT NAM KINH ĐIỂN ===
(44, 'Dế Mèn Phiêu Lưu Ký', 1941, 20, 'https://cdn0.fahasa.com/media/catalog/product/8/9/8935244842435.jpg', 'VH-001'),
(45, 'Đất Rừng Phương Nam', 1957, 10, 'https://cdn0.fahasa.com/media/catalog/product/8/9/8935244874436.jpg', 'VH-002'),
(46, 'Truyện Kiều (Tái bản)', 1820, 15, 'https://cdn0.fahasa.com/media/catalog/product/t/r/truyen-kieu-tai-ban-2023_bia-1.jpg', 'VH-003'),
(47, 'Số Đỏ', 1936, 8, 'https://cdn0.fahasa.com/media/catalog/product/i/m/image_186076.jpg', 'VH-004'),
(48, 'Chí Phèo (Tuyển tập Nam Cao)', 1941, 10, 'https://cdn0.fahasa.com/media/catalog/product/i/m/image_195509_1_29046.jpg', 'VH-005'),
(49, 'Tắt Đèn', 1937, 10, 'https://cdn0.fahasa.com/media/catalog/product/i/m/image_180165.jpg', 'VH-006'),
(50, 'Bỉ Vỏ (Nguyên Hồng)', 1938, 6, 'https://cdn0.fahasa.com/media/catalog/product/i/m/image_195509_1_26605.jpg', 'VH-007'),
(51, 'Gió Đầu Mùa (Thạch Lam)', 1937, 8, 'https://cdn0.fahasa.com/media/catalog/product/8/9/8936067608077.jpg', 'VH-008'),

-- === VĂN HỌC NƯỚC NGOÀI ===
(52, 'Harry Potter và Hòn Đá Phù Thủy', 1997, 10, 'https://cdn0.fahasa.com/media/catalog/product/h/a/harry-potter-va-hon-da-phu-thuy-tai-ban-2024.jpg', 'NN-001'),
(53, 'Harry Potter và Phòng Chứa Bí Mật', 1998, 9, 'https://cdn0.fahasa.com/media/catalog/product/h/a/harry-potter-va-phong-chua-bi-mat-tai-ban-2024.jpg', 'NN-002'),
(54, 'Không Gia Đình', 1878, 12, 'https://cdn0.fahasa.com/media/catalog/product/k/h/khong-gia-dinh-bia-cung_2.jpg', 'NN-003'),
(55, 'Hoàng Tử Bé', 1943, 18, 'https://cdn0.fahasa.com/media/catalog/product/h/o/hoang-tu-be-tai-ban-2022.jpg', 'NN-004'),
(56, 'Sherlock Holmes Toàn Tập', 1887, 7, 'https://cdn0.fahasa.com/media/catalog/product/s/h/sherlock-holmes-toan-tap-3-tap-hop.jpg', 'NN-005'),
(57, 'Những Cuộc Phiêu Lưu Của Tom Sawyer', 1876, 8, 'https://cdn0.fahasa.com/media/catalog/product/n/h/nhung-cuoc-phieu-luu-cua-tom-sawyer-tai-ban-2020.jpg', 'NN-006'),
(58, 'Robinson Crusoe', 1719, 6, 'https://cdn0.fahasa.com/media/catalog/product/r/o/robinson-crusoe-tai-ban-2022.jpg', 'NN-007'),

-- === TRUYỆN TRANH & GIẢI TRÍ ===
(59, 'Doraemon - Tập 1', 2000, 25, 'https://cdn0.fahasa.com/media/catalog/product/i/m/image_240057.jpg', 'TR-001'),
(60, 'Doraemon - Tập 2', 2000, 20, 'https://cdn0.fahasa.com/media/catalog/product/i/m/image_240058.jpg', 'TR-002'),
(61, 'Thám Tử Lừng Danh Conan - Tập 1', 1994, 25, 'https://cdn0.fahasa.com/media/catalog/product/t/h/tham-tu-lung-danh-conan---tap-1_3.jpg', 'TR-003'),
(62, 'Thám Tử Lừng Danh Conan - Tập 100', 2021, 15, 'https://cdn0.fahasa.com/media/catalog/product/i/m/image_232512.jpg', 'TR-004'),
(63, 'Thần Đồng Đất Việt - Tập 1', 2002, 20, 'https://cdn0.fahasa.com/media/catalog/product/8/9/8935244833372.jpg', 'TR-005'),

-- === KỸ NĂNG SỐNG & KHOA HỌC ===
(64, 'Tuổi Trẻ Đáng Giá Bao Nhiêu?', 2016, 20, 'https://cdn0.fahasa.com/media/catalog/product/t/u/tuoi-tre-dang-gia-bao-nhieu-tai-ban-2021_1.jpg', 'KN-001'),
(65, 'Tôi Tài Giỏi, Bạn Cũng Thế!', 2005, 15, 'https://cdn0.fahasa.com/media/catalog/product/i/m/image_195509_1_51727.jpg', 'KN-002'),
(66, 'Đắc Nhân Tâm', 1936, 30, 'https://cdn0.fahasa.com/media/catalog/product/i/m/image_195509_1_36793.jpg', 'KN-003'),
(67, 'Mười Vạn Câu Hỏi Vì Sao - Vũ Trụ', 2020, 12, 'https://cdn0.fahasa.com/media/catalog/product/m/u/muoi-van-cau-hoi-vi-sao---bi-an-vu-tru---b_-vuong-tron.jpg', 'KH-001'),
(68, 'Mười Vạn Câu Hỏi Vì Sao - Động Vật', 2020, 12, 'https://cdn0.fahasa.com/media/catalog/product/m/u/muoi-van-cau-hoi-vi-sao---dong-vat.jpg', 'KH-002'),
(69, 'Atlas Địa Lý Việt Nam', 2023, 40, 'https://cdn0.fahasa.com/media/catalog/product/a/t/atlas-dia-ly-viet-nam_1_2.jpg', 'KH-003');


-- 5. LIÊN KẾT SÁCH - TÁC GIẢ
INSERT INTO book_authors (book_id, author_id) VALUES
-- Khối 6
(1, 1), (2, 1), (3, 1), (4, 1), (5, 1), (6, 1), (7, 1),
-- Khối 7
(8, 1), (9, 1), (10, 1), (11, 1), (12, 1), (13, 1),
-- Khối 8
(14, 1), (15, 1), (16, 1), (17, 1), (18, 1), (19, 1), (20, 1),
-- Khối 9
(21, 1), (22, 1), (23, 1), (24, 1), (25, 1), (26, 1), (27, 1), (28, 1), (29, 1), (30, 1),
-- Tham khảo
(31, 15), (32, 15), (33, 15), (34, 15), (35, 15), (36, 15),
-- NNA
(37, 2), (38, 2), (39, 2), (40, 2), (41, 2), (42, 2), (43, 2),
-- VH VN
(44, 3), (45, 5), (46, 9), (47, 10), (48, 4), (49, 10), (50, 17), (51, 16),
-- VH Nuoc Ngoai
(52, 6), (53, 6), (54, 13), (55, 12), (56, 8), (57, 18), (58, 19),
-- Truyen Tranh
(59, 7), (60, 7), (61, 14), (62, 14), (63, 15),
-- Ky Nang
(64, 11), (65, 15), (66, 20), (67, 15), (68, 15), (69, 1);


-- 6. LIÊN KẾT SÁCH - DANH MỤC
INSERT INTO book_categories (book_id, category_id) VALUES
-- SGK (1)
(1, 1), (2, 1), (3, 1), (4, 1), (5, 1), (6, 1), (7, 1),
(8, 1), (9, 1), (10, 1), (11, 1), (12, 1), (13, 1),
(14, 1), (15, 1), (16, 1), (17, 1), (18, 1), (19, 1), (20, 1),
(21, 1), (22, 1), (23, 1), (24, 1), (25, 1), (26, 1), (27, 1), (28, 1), (29, 1), (30, 1),
-- Tham Khao (2)
(31, 2), (32, 2), (33, 2), (34, 2), (35, 2), (36, 2),
-- NNA (3)
(37, 3), (38, 3), (39, 3), (40, 3), (41, 3), (42, 3), (43, 3),
-- VH VN (3)
(44, 3), (45, 3), (46, 3), (47, 3), (48, 3), (49, 3), (50, 3), (51, 3),
-- VH NN (4)
(52, 4), (53, 4), (54, 4), (55, 4), (56, 4), (57, 4), (58, 4),
-- Truyen Tranh (5)
(59, 5), (60, 5), (61, 5), (62, 5), (63, 5),
-- Ky Nang (6)
(64, 6), (65, 6), (66, 6),
-- Khoa Hoc (7)
(67, 7), (68, 7), (69, 7);

SET FOREIGN_KEY_CHECKS = 1;