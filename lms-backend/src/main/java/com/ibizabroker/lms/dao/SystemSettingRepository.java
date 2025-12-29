package com.ibizabroker.lms.dao;

import com.ibizabroker.lms.entity.SystemSetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SystemSettingRepository extends JpaRepository<SystemSetting, Long> {
    Optional<SystemSetting> findByKeyIgnoreCase(String key);
}
