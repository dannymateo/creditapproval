package com.cotrafa.creditapproval.customer.domain.port.out;

import com.cotrafa.creditapproval.user.domain.model.User;

public interface NotificationPort {
    void sendWelcomeEmail(User user, String rawPassword);
}