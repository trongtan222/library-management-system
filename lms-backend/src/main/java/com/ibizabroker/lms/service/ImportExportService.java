package com.ibizabroker.lms.service;

import com.ibizabroker.lms.dao.BooksRepository;
import com.ibizabroker.lms.dao.RoleRepository;
import com.ibizabroker.lms.dao.UsersRepository;
import com.ibizabroker.lms.dto.ImportSummaryDto;
import com.ibizabroker.lms.entity.Books;
import com.ibizabroker.lms.entity.Role;
import com.ibizabroker.lms.entity.Users;
import com.ibizabroker.lms.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class ImportExportService {

    private final UsersRepository usersRepository;
    private final BooksRepository booksRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String DEFAULT_BOOK_COVER = "";

    public ImportSummaryDto importUsers(MultipartFile file) {
        List<List<String>> rows = readRows(file);
        int success = 0;
        int failed = 0;
        int skipped = 0;
        List<String> errors = new ArrayList<>();

        Role userRole = roleRepository.findByRoleName("ROLE_USER")
                .orElseThrow(() -> new NotFoundException("Role ROLE_USER not found."));

        for (int i = 1; i < rows.size(); i++) { // skip header
            List<String> r = rows.get(i);
            String code = cell(r, 0);
            String name = cell(r, 1);
            String email = cell(r, 2);
            String clazz = cell(r, 3);

            if (isBlank(code) || isBlank(name)) {
                failed++;
                errors.add("Dòng " + (i + 1) + ": Thiếu mã SV hoặc tên.");
                continue;
            }

            if (usersRepository.existsByUsernameIgnoreCase(code)) {
                skipped++;
                continue;
            }

            try {
                Users user = new Users();
                user.setUsername(code.toLowerCase(Locale.ROOT));
                user.setName(name);
                user.setEmail(email);
                user.setStudentClass(clazz);
                user.setPassword(passwordEncoder.encode(RandomStringUtils.randomAlphanumeric(10)));
                Set<Role> roles = new HashSet<>();
                roles.add(userRole);
                user.setRoles(roles);
                usersRepository.save(user);
                success++;
            } catch (Exception ex) {
                failed++;
                errors.add("Dòng " + (i + 1) + ": " + ex.getMessage());
            }
        }

        ImportSummaryDto summary = new ImportSummaryDto();
        summary.setSuccessCount(success);
        summary.setFailedCount(failed);
        summary.setSkippedCount(skipped);
        summary.setErrors(errors);
        return summary;
    }

    public ImportSummaryDto importBooks(MultipartFile file) {
        List<List<String>> rows = readRows(file);
        int success = 0;
        int failed = 0;
        int skipped = 0;
        List<String> errors = new ArrayList<>();

        for (int i = 1; i < rows.size(); i++) { // skip header
            List<String> r = rows.get(i);
            String name = cell(r, 0);
            String isbn = cell(r, 1);
            String yearStr = cell(r, 2);
            String copiesStr = cell(r, 3);
            String coverUrl = cell(r, 4);

            if (isBlank(name)) {
                failed++;
                errors.add("Dòng " + (i + 1) + ": Thiếu tên sách.");
                continue;
            }

            Integer publishedYear = parseInteger(yearStr);
            Integer copies = parseInteger(copiesStr);
            if (copies == null || copies < 0) copies = 1;

            try {
                Optional<Books> existing = isBlank(isbn) ? Optional.empty() : booksRepository.findByIsbnIgnoreCase(isbn);
                Books book = existing.orElseGet(Books::new);
                book.setName(name);
                book.setIsbn(isbn);
                if (publishedYear != null) {
                    book.setPublishedYear(publishedYear);
                }

                int baseCopies = book.getNumberOfCopiesAvailable() == null ? 0 : book.getNumberOfCopiesAvailable();
                book.setNumberOfCopiesAvailable(baseCopies + copies);

                if (!isBlank(coverUrl)) {
                    book.setCoverUrl(coverUrl);
                } else if (book.getCoverUrl() == null) {
                    book.setCoverUrl(DEFAULT_BOOK_COVER);
                }
                booksRepository.save(book);
                success++;
            } catch (Exception ex) {
                failed++;
                errors.add("Dòng " + (i + 1) + ": " + ex.getMessage());
            }
        }

        ImportSummaryDto summary = new ImportSummaryDto();
        summary.setSuccessCount(success);
        summary.setFailedCount(failed);
        summary.setSkippedCount(skipped);
        summary.setErrors(errors);
        return summary;
    }

    public ByteArrayResource exportUsersWorkbook() {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Users");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Mã SV");
            header.createCell(1).setCellValue("Tên");
            header.createCell(2).setCellValue("Email");
            header.createCell(3).setCellValue("Lớp");

            var all = usersRepository.findAll();
            for (int i = 0; i < all.size(); i++) {
                Users u = all.get(i);
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(value(u.getUsername()));
                row.createCell(1).setCellValue(value(u.getName()));
                row.createCell(2).setCellValue(value(u.getEmail()));
                row.createCell(3).setCellValue(value(u.getStudentClass()));
            }

            workbook.write(out);
            @SuppressWarnings("null")
            ByteArrayResource resource = new ByteArrayResource(out.toByteArray());
            return resource;
        } catch (IOException e) {
            throw new RuntimeException("Không thể xuất users", e);
        }
    }

    public ByteArrayResource exportBooksWorkbook() {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Books");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Tên sách");
            header.createCell(1).setCellValue("ISBN");
            header.createCell(2).setCellValue("Năm XB");
            header.createCell(3).setCellValue("Số lượng");
            header.createCell(4).setCellValue("Ảnh bìa (URL)");

            var all = booksRepository.findAll();
            for (int i = 0; i < all.size(); i++) {
                Books b = all.get(i);
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(value(b.getName()));
                row.createCell(1).setCellValue(value(b.getIsbn()));
                row.createCell(2).setCellValue(b.getPublishedYear() == null ? "" : String.valueOf(b.getPublishedYear()));
                row.createCell(3).setCellValue(b.getNumberOfCopiesAvailable() == null ? "" : String.valueOf(b.getNumberOfCopiesAvailable()));
                row.createCell(4).setCellValue(value(b.getCoverUrl()));
            }

            workbook.write(out);
            @SuppressWarnings("null")
            ByteArrayResource resource = new ByteArrayResource(out.toByteArray());
            return resource;
        } catch (IOException e) {
            throw new RuntimeException("Không thể xuất books", e);
        }
    }

    public ByteArrayResource usersTemplate() {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Template");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Mã SV");
            header.createCell(1).setCellValue("Tên");
            header.createCell(2).setCellValue("Email");
            header.createCell(3).setCellValue("Lớp");

            workbook.write(out);
            @SuppressWarnings("null")
            ByteArrayResource resource = new ByteArrayResource(out.toByteArray());
            return resource;
        } catch (IOException e) {
            throw new RuntimeException("Không tạo được template users", e);
        }
    }

    public ByteArrayResource booksTemplate() {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Template");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Tên sách");
            header.createCell(1).setCellValue("ISBN");
            header.createCell(2).setCellValue("Năm XB");
            header.createCell(3).setCellValue("Số lượng");
            header.createCell(4).setCellValue("Ảnh bìa (URL)");
            workbook.write(out);
            @SuppressWarnings("null")
            ByteArrayResource resource = new ByteArrayResource(out.toByteArray());
            return resource;
        } catch (IOException e) {
            throw new RuntimeException("Không tạo được template books", e);
        }
    }

    // --- Helpers ---
    private List<List<String>> readRows(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String filename = (originalFilename == null ? "" : originalFilename).toLowerCase(Locale.ROOT);
        try {
            if (filename.endsWith(".xlsx") || filename.endsWith(".xls")) {
                return readExcelRows(file);
            }
            return readCsvRows(file);
        } catch (IOException e) {
            throw new RuntimeException("Không đọc được file: " + e.getMessage(), e);
        }
    }

    private List<List<String>> readExcelRows(MultipartFile file) throws IOException {
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            List<List<String>> rows = new ArrayList<>();
            for (Row row : sheet) {
                List<String> cols = new ArrayList<>();
                for (int i = 0; i < 8; i++) {
                    cols.add(row.getCell(i) == null ? "" : row.getCell(i).toString().trim());
                }
                rows.add(cols);
            }
            return rows;
        }
    }

    private List<List<String>> readCsvRows(MultipartFile file) throws IOException {
        List<List<String>> rows = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] cols = line.split(",", -1);
                List<String> row = new ArrayList<>();
                for (String col : cols) {
                    row.add(col == null ? "" : col.trim());
                }
                rows.add(row);
            }
        }
        return rows;
    }

    private String cell(List<String> row, int idx) {
        if (idx >= row.size()) return "";
        return row.get(idx) == null ? "" : row.get(idx).trim();
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private Integer parseInteger(String val) {
        try {
            if (isBlank(val)) return null;
            return Integer.parseInt(val.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String value(String input) {
        return input == null ? "" : input;
    }

    public MediaType excelContentType() {
        return MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }
}
