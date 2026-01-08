package com.ibizabroker.lms.controller;

import com.ibizabroker.lms.dao.NewsRepository;
import com.ibizabroker.lms.dao.UsersRepository;
import com.ibizabroker.lms.entity.News;
import com.ibizabroker.lms.entity.Users;
import com.ibizabroker.lms.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("http://localhost:4200/")
@RestController
@RequestMapping("/api/admin/news")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequiredArgsConstructor
public class AdminNewsController {

    private final NewsRepository newsRepository;
    private final UsersRepository usersRepository;
    private final EmailService emailService;

    @GetMapping
    public List<News> getAll() {
        return newsRepository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public News create(@RequestBody News news,
                      @RequestParam(name = "notifyEmail", defaultValue = "false") boolean notifyEmail) {
        news.setId(null);
        News saved = newsRepository.save(news);
        if (notifyEmail) {
            broadcastNews(saved);
        }
        return saved;
    }

    @PutMapping("/{id}")
    @SuppressWarnings("null")
    public ResponseEntity<News> update(@PathVariable Long id,
                                       @RequestBody News news,
                                       @RequestParam(name = "notifyEmail", defaultValue = "false") boolean notifyEmail) {
        return newsRepository.findById(id)
                .map(existing -> {
                    existing.setTitle(news.getTitle());
                    existing.setContent(news.getContent());
                    News saved = newsRepository.save(existing);
                    if (notifyEmail) {
                        broadcastNews(saved);
                    }
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @SuppressWarnings("null")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (newsRepository.existsById(id)) {
            newsRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private void broadcastNews(News news) {
        List<Users> usersWithEmail = usersRepository.findAll().stream()
                .filter(u -> u.getEmail() != null && !u.getEmail().isBlank())
                .toList();

        String subject = "Tin tức mới: " + news.getTitle();
        String html = "<p>Chào bạn,</p>" +
                "<p>Có tin tức mới trên hệ thống thư viện:</p>" +
                "<p><strong>" + news.getTitle() + "</strong></p>" +
                "<p>" + news.getContent() + "</p>" +
                "<p>Trân trọng,<br/>Thư viện LMS</p>";

        usersWithEmail.forEach(u -> emailService.sendHtmlMessage(u.getEmail(), subject, html));
    }
}
