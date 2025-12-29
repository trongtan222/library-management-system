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
(1, 'Ngữ Văn 6 - Tập 1 (CTST)', 2024, 50, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444742/nv6t1ctst_kqnqx3.jpg', 'SGK-6-NV1'),
(2, 'Ngữ Văn 6 - Tập 2 (CTST)', 2024, 50, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444742/nv6t2ctst_bc9jow.jpg', 'SGK-6-NV2'),
(3, 'Toán 6 - Tập 1 (KNTT)', 2024, 45, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444748/t6t1kntt_ntberu.jpg', 'SGK-6-T1'),
(4, 'Toán 6 - Tập 2 (KNTT)', 2024, 45, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444744/t6t2kntt_ekqihh.jpg', 'SGK-6-T2'),
(5, 'Tiếng Anh 6 (Global Success)', 2024, 40, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444748/ta6t1gs_aafauy.jpg', 'SGK-6-AV'),
(6, 'Khoa Học Tự Nhiên 6', 2024, 35, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444742/khtn6_ye9mpi.jpg', 'SGK-6-KHTN'),
(7, 'Lịch Sử và Địa Lí 6', 2024, 35, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444742/lsdl6_onkfcn.jpg', 'SGK-6-SD'),

-- === KHỐI 7 (Cánh Diều / KNTT) ===
(8, 'Ngữ Văn 7 - Tập 1 (Cánh Diều)', 2024, 40, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444745/nv7t1cd_wxwe8z.jpg', 'SGK-7-NV1'),
(9, 'Ngữ Văn 7 - Tập 2 (Cánh Diều)', 2024, 40, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444745/nv7t2cd_oaitlq.jpg', 'SGK-7-NV2'),
(10, 'Toán 7 - Tập 1 (KNTT)', 2024, 40, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444745/t7t1kntt_ccahpu.jpg', 'SGK-7-T1'),
(11, 'Toán 7 - Tập 2 (KNTT)', 2024, 40, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444745/t7t2kntt_jsrrx9.jpg', 'SGK-7-T2'),
(12, 'Tiếng Anh 7 (Global Success)', 2024, 40, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444745/ta7gs_vgcvnd.jpg', 'SGK-7-AV'),
(13, 'Khoa Học Tự Nhiên 7', 2024, 35, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444745/khtn7_kjhxnm.jpg', 'SGK-7-KHTN'),

-- === KHỐI 8 (Kết nối tri thức) ===
(14, 'Ngữ Văn 8 - Tập 1', 2024, 40, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444749/nv8t1kntt_ocmlbx.jpg', 'SGK-8-NV1'),
(15, 'Ngữ Văn 8 - Tập 2', 2024, 40, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444752/nv8t2kntt_heozpr.jpg', 'SGK-8-NV2'),
(16, 'Toán 8 - Tập 1', 2024, 40, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444750/t8t1kntt_jflcnc.jpg', 'SGK-8-T1'),
(17, 'Toán 8 - Tập 2', 2024, 40, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444755/t8t2kntt_lfvvyr.jpg', 'SGK-8-T2'),
(18, 'Tiếng Anh 8 (Global Success)', 2024, 35, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444742/ta8gs_wguxbq.jpg', 'SGK-8-AV'),
(19, 'Khoa Học Tự Nhiên 8', 2024, 30, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444743/khtn8_qs7pbw.jpg', 'SGK-8-KHTN'),
(20, 'Lịch Sử và Địa Lí 8', 2024, 30, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444743/lsvdl8_wxrkat.jpg', 'SGK-8-SD'),

-- === KHỐI 9 (Lớp cuối cấp) ===
(21, 'Ngữ Văn 9 - Tập 1', 2024, 50, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444743/nv9t1_ksziho.jpg', 'SGK-9-NV1'),
(22, 'Ngữ Văn 9 - Tập 2', 2024, 50, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444743/nv9t2_ylswne.jpg', 'SGK-9-NV2'),
(23, 'Toán 9 - Tập 1', 2024, 50, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444743/t9t1_cpn9hv.jpg', 'SGK-9-T1'),
(24, 'Toán 9 - Tập 2', 2024, 50, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444743/t9t2_hctuxf.jpg', 'SGK-9-T2'),
(25, 'Tiếng Anh 9', 2024, 45, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444743/ta9_erijib.jpg', 'SGK-9-AV'),
(26, 'Vật Lí 9', 2024, 35, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444743/vl9_scmyxc.jpg', 'SGK-9-LY'),
(27, 'Hóa Học 9', 2024, 35, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444746/hh9_fyh1sq.jpg', 'SGK-9-HOA'),
(28, 'Sinh Học 9', 2024, 35, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444746/sh9_w9pi6f.jpg', 'SGK-9-SINH'),
(29, 'Lịch Sử 9', 2024, 30, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444746/ls9_kercmv.jpg', 'SGK-9-SU'),
(30, 'Địa Lí 9', 2024, 30, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444746/dl9_flr776.jpg', 'SGK-9-DIA'),

-- === SÁCH THAM KHẢO & ÔN LUYỆN ===
(31, '500 Bài Tập Vật Lí 6', 2022, 15, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444748/500btvl6_nflawv.jpg', 'TK-LY6'),
(32, 'Bồi Dưỡng Học Sinh Giỏi Toán 7', 2023, 10, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444749/bdhsgt7_thu7rw.jpg', 'TK-TOAN7'),
(33, 'Giải Bài Tập Hóa Học 8', 2023, 12, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444749/gbthh8_lpwifr.jpg', 'TK-HOA8'),
(34, 'Ôn Luyện Thi Vào Lớp 10 Môn Toán', 2024, 30, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444749/olv10t_syyk49.jpg', 'TK-10-TOAN'),
(35, 'Ôn Luyện Thi Vào Lớp 10 Môn Văn', 2024, 30, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444749/olv10v_bzdb49.jpg', 'TK-10-VAN'),
(36, 'Ôn Luyện Thi Vào Lớp 10 Môn Anh', 2024, 30, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444749/olv10a_sb2mdf.jpg', 'TK-10-AV'),
-- === VĂN HỌC TUỔI XANH (Nguyễn Nhật Ánh) ===
(37, 'Cho Tôi Xin Một Vé Đi Tuổi Thơ', 2008, 15, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444750/ctx1vdtt_jor7sj.jpg', 'NNA-001'),
(38, 'Mắt Biếc', 1990, 12, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444750/mb_iek8gl.jpg', 'NNA-002'),
(39, 'Kính Vạn Hoa (Bộ 3 Tập)', 2000, 8, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444750/shtt_tc9vdz.jpg', 'NNA-003'),
(40, 'Cô Gái Đến Từ Hôm Qua', 1989, 10, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444750/cgdthq_ojjzso.jpg', 'NNA-004'),
(41, 'Tôi Thấy Hoa Vàng Trên Cỏ Xanh', 2010, 14, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444750/tthvtcx_zrlin5.jpg', 'NNA-005'),
(42, 'Bồ Câu Không Đưa Thư', 1993, 8, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444750/bckdt_ewetmd.jpg', 'NNA-006'),
(43, 'Ngồi Khóc Trên Cây', 2013, 10, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444750/nktc_laffes.jpg', 'NNA-007'),

-- === VĂN HỌC VIỆT NAM KINH ĐIỂN ===
(44, 'Dế Mèn Phiêu Lưu Ký', 1941, 20, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444752/dmplk_msoczg.jpg', 'VH-001'),
(45, 'Đất Rừng Phương Nam', 1957, 10, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444752/drpn_olbhl5.jpg', 'VH-002'),
(46, 'Truyện Kiều (Tái bản)', 1820, 15, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444752/tk_biawjq.jpg', 'VH-003'),
(47, 'Số Đỏ', 1936, 8, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444752/sd_zgnasv.jpg', 'VH-004'),
(48, 'Chí Phèo (Tuyển tập Nam Cao)', 1941, 10, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444752/cp_z2aqtl.jpg', 'VH-005'),
(49, 'Tắt Đèn', 1937, 10, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444752/td_fpgyvo.jpg', 'VH-006'),
(50, 'Bỉ Vỏ (Nguyên Hồng)', 1938, 6, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444752/bv_lnlzqa.jpg', 'VH-007'),
(51, 'Gió Lạnh Đầu Mùa (Thạch Lam)', 1937, 8, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444752/gldm_rvxdpf.jpg', 'VH-008'),

-- === VĂN HỌC NƯỚC NGOÀI ===
(52, 'Harry Potter và Hòn Đá Phù Thủy', 1997, 10, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444754/hpvhdpt_cyqzzv.jpg', 'NN-001'),
(53, 'Harry Potter và Phòng Chứa Bí Mật', 1998, 9, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444754/hpvpcbm_tyi4cp.jpg', 'NN-002'),
(54, 'Không Gia Đình', 1878, 12, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444754/kgd_qkiki9.jpg', 'NN-003'),
(55, 'Hoàng Tử Bé', 1943, 18, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444754/htb_mpzrfi.jpg', 'NN-004'),
(56, 'Sherlock Holmes Toàn Tập', 1887, 7, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444754/shtt_tc9vdz.jpg', 'NN-005'),
(57, 'Những Cuộc Phiêu Lưu Của Tom Sawyer', 1876, 8, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444754/ncplcts_qh5ey8.jpg', 'NN-006'),
(58, 'Robinson Crusoe', 1719, 6, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444754/rc_pzrord.jpg', 'NN-007'),

-- === TRUYỆN TRANH & GIẢI TRÍ ===
(59, 'Doraemon - Tập 1', 2000, 25, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444755/drmt1_ql9lak.jpg', 'TR-001'),
(60, 'Doraemon - Tập 2', 2000, 20, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444755/drmt2_hdtogt.jpg', 'TR-002'),
(61, 'Thám Tử Lừng Danh Conan - Tập 1', 1994, 25, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444755/cnt1_kytbmi.jpg', 'TR-003'),
(62, 'Thám Tử Lừng Danh Conan - Tập 100', 2021, 15, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444755/cnt100_f2z8vs.jpg', 'TR-004'),
(63, 'Thần Đồng Đất Việt - Tập 1', 2002, 20, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444779/tddvt1_xtjvgb.jpg', 'TR-005'),
-- === KỸ NĂNG SỐNG & KHOA HỌC ===
(64, 'Tuổi Trẻ Đáng Giá Bao Nhiêu?', 2016, 20, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444779/ttdgbn_aqxhqn.jpg', 'KN-001'),
(65, 'Tôi Tài Giỏi, Bạn Cũng Thế!', 2005, 15, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444780/ttgbct_ajjrop.jpg', 'KN-002'),
(66, 'Đắc Nhân Tâm', 1936, 30, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444780/dnt_mu2cf3.jpg', 'KN-003'),
(67, 'Mười Vạn Câu Hỏi Vì Sao - Vũ Trụ', 2020, 12, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444780/10vchvsvt_wkxlyl.jpg', 'KH-001'),
(68, 'Mười Vạn Câu Hỏi Vì Sao - Động Vật', 2020, 12, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444780/10vchvsdv_inyr7l.jpg', 'KH-002'),
(69, 'Atlas Địa Lý Việt Nam', 2023, 40, 'https://res.cloudinary.com/dalyjpafn/image/upload/v1764444780/atlas_pmmjxf.jpg', 'KH-003');

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