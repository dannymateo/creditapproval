package com.cotrafa.creditapproval.shared.infrastructure.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailTemplateDTO {

    private String title;
    private String subtitle;
    private String banner;

    private String content; // HTML Content
    private String description;

    private String footer;

    private ActionDto action;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ActionDto {
        private String title;
        private String url;
    }
}