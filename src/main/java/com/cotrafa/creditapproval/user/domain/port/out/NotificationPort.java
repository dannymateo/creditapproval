package com.cotrafa.creditapproval.user.domain.port.out;

import com.cotrafa.creditapproval.user.domain.model.User;

public interface NotificationPort {
    void sendWelcomeEmail(User user, String rawPassword);
    void sendPasswordResetEmail(User user, String rawPassword);
}