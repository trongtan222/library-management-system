package com.ibizabroker.lms.service;

import com.ibizabroker.lms.dao.SystemSettingRepository;
import com.ibizabroker.lms.entity.SystemSetting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SystemSettingService {

    public static final String KEY_LOAN_MAX_DAYS = "LOAN_MAX_DAYS";
    public static final String KEY_FINE_PER_DAY = "FINE_PER_DAY";

    private final SystemSettingRepository repo;

    @Transactional(readOnly = true)
    public List<SystemSetting> findAll() {
        return repo.findAll();
    }

    public SystemSetting upsert(String key, String value) {
        Optional<SystemSetting> existing = repo.findByKeyIgnoreCase(key);
        SystemSetting s = existing.orElseGet(() -> new SystemSetting(key, value));
        s.setKey(key);
        s.setValue(value);
        return repo.save(s);
    }

    @Transactional(readOnly = true)
    public int getInt(String key, int defaultValue) {
        return repo.findByKeyIgnoreCase(key)
                .map(SystemSetting::getValue)
                .map(val -> {
                    try { return Integer.parseInt(val.trim()); } catch (Exception e) { return defaultValue; }
                })
                .orElse(defaultValue);
    }

    @Transactional(readOnly = true)
    public BigDecimal getBigDecimal(String key, BigDecimal defaultValue) {
        return repo.findByKeyIgnoreCase(key)
                .map(SystemSetting::getValue)
                .map(val -> {
                    try { return new BigDecimal(val.trim()); } catch (Exception e) { return defaultValue; }
                })
                .orElse(defaultValue);
    }
}
