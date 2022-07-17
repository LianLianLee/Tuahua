package com.tanhua.dubbo.api;

import com.tanhua.model.domain.Settings;

public interface SettingsApi {

    Settings findByUserId(Long id);

    void save(Settings settings);

    void update(Settings settings);
}
