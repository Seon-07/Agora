package com.seon.moca.common.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.stereotype.Component;

/**
 * @author seon
 * @version 1.0
 * @since 25. 9. 7.
 */
@Slf4j
@Component
public class SessionEventListener {
    @EventListener
    public void onSessionDestroyed(HttpSessionDestroyedEvent event) {
        log.info("세션 만료됨:" +  event.getId());
    }
}
