package com.ibizabroker.lms.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Feature 6: i18n - Internationalization Service
 */
@Service
public class I18nService {

    private static final Map<String, Map<String, String>> messages = new HashMap<>();

    static {
        // Vietnamese messages
        Map<String, String> vi = new HashMap<>();
        vi.put("book.not.found", "Không tìm thấy sách với ID: {0}");
        vi.put("user.not.found", "Không tìm thấy người dùng với ID: {0}");
        vi.put("loan.not.found", "Không tìm thấy phiếu mượn với ID: {0}");
        vi.put("book.no.copies", "Sách này đã hết bản sao.");
        vi.put("loan.already.returned", "Phiếu mượn này đã được trả.");
        vi.put("loan.create.success", "Mượn sách thành công.");
        vi.put("loan.return.success", "Trả sách thành công.");
        vi.put("login.success", "Đăng nhập thành công.");
        vi.put("login.failed", "Tên đăng nhập hoặc mật khẩu không đúng.");
        vi.put("register.success", "Đăng ký thành công.");
        vi.put("password.reset.sent", "Email đặt lại mật khẩu đã được gửi.");
        vi.put("password.reset.success", "Đặt lại mật khẩu thành công.");
        vi.put("password.change.success", "Đổi mật khẩu thành công.");
        vi.put("validation.required", "Trường này không được để trống.");
        vi.put("validation.email", "Email không hợp lệ.");
        vi.put("validation.min", "Giá trị phải lớn hơn hoặc bằng {0}.");
        vi.put("validation.max", "Giá trị phải nhỏ hơn hoặc bằng {0}.");
        vi.put("fine.paid.success", "Thanh toán phạt thành công.");
        vi.put("review.create.success", "Đánh giá của bạn đã được gửi.");
        vi.put("review.update.success", "Cập nhật đánh giá thành công.");
        vi.put("wishlist.add.success", "Đã thêm vào danh sách yêu thích.");
        vi.put("wishlist.remove.success", "Đã xóa khỏi danh sách yêu thích.");
        vi.put("ebook.download.limit", "Bạn đã đạt giới hạn tải xuống cho ebook này.");
        vi.put("challenge.join.success", "Tham gia thử thách thành công.");
        vi.put("challenge.already.joined", "Bạn đã tham gia thử thách này rồi.");
        vi.put("badge.earned", "Chúc mừng! Bạn đã đạt được huy hiệu: {0}");
        vi.put("points.earned", "+{0} điểm: {1}");
        messages.put("vi", vi);

        // English messages
        Map<String, String> en = new HashMap<>();
        en.put("book.not.found", "Book not found with ID: {0}");
        en.put("user.not.found", "User not found with ID: {0}");
        en.put("loan.not.found", "Loan not found with ID: {0}");
        en.put("book.no.copies", "No copies available for this book.");
        en.put("loan.already.returned", "This loan has already been returned.");
        en.put("loan.create.success", "Book borrowed successfully.");
        en.put("loan.return.success", "Book returned successfully.");
        en.put("login.success", "Login successful.");
        en.put("login.failed", "Invalid username or password.");
        en.put("register.success", "Registration successful.");
        en.put("password.reset.sent", "Password reset email has been sent.");
        en.put("password.reset.success", "Password reset successful.");
        en.put("password.change.success", "Password changed successfully.");
        en.put("validation.required", "This field is required.");
        en.put("validation.email", "Invalid email address.");
        en.put("validation.min", "Value must be greater than or equal to {0}.");
        en.put("validation.max", "Value must be less than or equal to {0}.");
        en.put("fine.paid.success", "Fine paid successfully.");
        en.put("review.create.success", "Your review has been submitted.");
        en.put("review.update.success", "Review updated successfully.");
        en.put("wishlist.add.success", "Added to wishlist.");
        en.put("wishlist.remove.success", "Removed from wishlist.");
        en.put("ebook.download.limit", "You have reached the download limit for this ebook.");
        en.put("challenge.join.success", "Successfully joined the challenge.");
        en.put("challenge.already.joined", "You have already joined this challenge.");
        en.put("badge.earned", "Congratulations! You earned the badge: {0}");
        en.put("points.earned", "+{0} points: {1}");
        messages.put("en", en);
    }

    public String getMessage(String key, String lang, Object... args) {
        String language = lang != null ? lang.toLowerCase() : "vi";
        Map<String, String> langMessages = messages.getOrDefault(language, messages.get("vi"));
        String message = langMessages.getOrDefault(key, key);
        
        // Replace placeholders {0}, {1}, etc.
        for (int i = 0; i < args.length; i++) {
            message = message.replace("{" + i + "}", String.valueOf(args[i]));
        }
        
        return message;
    }

    public String getMessage(String key, Locale locale, Object... args) {
        return getMessage(key, locale.getLanguage(), args);
    }

    public Map<String, String> getAllMessages(String lang) {
        String language = lang != null ? lang.toLowerCase() : "vi";
        return new HashMap<>(messages.getOrDefault(language, messages.get("vi")));
    }
}
