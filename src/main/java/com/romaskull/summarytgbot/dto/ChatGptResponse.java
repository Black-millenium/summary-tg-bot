package com.romaskull.summarytgbot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;


public final class ChatGptResponse {

    private final String id;

    private final String object;

    private final Long created;

    private final String model;

    @JsonProperty("usage")
    private final UsageInfo usage;

    private final List<ChoicesResult> choices;

    public ChatGptResponse(String id,
                           String object,
                           Long created,
                           String model,
                           @JsonProperty("usage") UsageInfo usage,
                           List<ChoicesResult> choices) {
        this.id = id;
        this.object = object;
        this.created = created;
        this.model = model;
        this.usage = usage;
        this.choices = choices;
    }

    public String id() {
        return id;
    }

    public String object() {
        return object;
    }

    public Long created() {
        return created;
    }

    public String model() {
        return model;
    }

    @JsonProperty("usage")
    public UsageInfo usage() {
        return usage;
    }

    public List<ChoicesResult> choices() {
        return choices;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ChatGptResponse) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.object, that.object) &&
                Objects.equals(this.created, that.created) &&
                Objects.equals(this.model, that.model) &&
                Objects.equals(this.usage, that.usage) &&
                Objects.equals(this.choices, that.choices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, object, created, model, usage, choices);
    }

    @Override
    public String toString() {
        return "ChatGptResponse[" +
                "id=" + id + ", " +
                "object=" + object + ", " +
                "created=" + created + ", " +
                "model=" + model + ", " +
                "usage=" + usage + ", " +
                "choices=" + choices + ']';
    }


    public static final class UsageInfo {

        @JsonProperty("prompt_tokens")
        private final Long promptTokens;

        @JsonProperty("completion_tokens")
        private final Long completionTokens;

        @JsonProperty("total_tokens")
        private final Long totalTokens;

        public UsageInfo(@JsonProperty("prompt_tokens") Long promptTokens,
                         @JsonProperty("completion_tokens") Long completionTokens,
                         @JsonProperty("total_tokens") Long totalTokens) {
            this.promptTokens = promptTokens;
            this.completionTokens = completionTokens;
            this.totalTokens = totalTokens;
        }

        @JsonProperty("prompt_tokens")
        public Long promptTokens() {
            return promptTokens;
        }

        @JsonProperty("completion_tokens")
        public Long completionTokens() {
            return completionTokens;
        }

        @JsonProperty("total_tokens")
        public Long totalTokens() {
            return totalTokens;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (UsageInfo) obj;
            return Objects.equals(this.promptTokens, that.promptTokens) &&
                    Objects.equals(this.completionTokens, that.completionTokens) &&
                    Objects.equals(this.totalTokens, that.totalTokens);
        }

        @Override
        public int hashCode() {
            return Objects.hash(promptTokens, completionTokens, totalTokens);
        }

        @Override
        public String toString() {
            return "UsageInfo[" +
                    "promptTokens=" + promptTokens + ", " +
                    "completionTokens=" + completionTokens + ", " +
                    "totalTokens=" + totalTokens + ']';
        }

    }

    public static final class ChoicesResult {

        private final Message message;

        @JsonProperty("finish_reason")
        private final String finishReason;

        private final Long index;

        public ChoicesResult(Message message,
                             @JsonProperty("finish_reason") String finishReason,
                             Long index) {
            this.message = message;
            this.finishReason = finishReason;
            this.index = index;
        }

        public Message message() {
            return message;
        }

        @JsonProperty("finish_reason")
        public String finishReason() {
            return finishReason;
        }

        public Long index() {
            return index;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (ChoicesResult) obj;
            return Objects.equals(this.message, that.message) &&
                    Objects.equals(this.finishReason, that.finishReason) &&
                    Objects.equals(this.index, that.index);
        }

        @Override
        public int hashCode() {
            return Objects.hash(message, finishReason, index);
        }

        @Override
        public String toString() {
            return "ChoicesResult[" +
                    "message=" + message + ", " +
                    "finishReason=" + finishReason + ", " +
                    "index=" + index + ']';
        }

    }

    public static final class Message {

        private final String role;

        private final String content;

        public Message(String role,
                       String content) {
            this.role = role;
            this.content = content;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Message.class.getSimpleName() + "[", "]")
                    .add("role='" + role + "'")
                    .toString();
        }

        public String role() {
            return role;
        }

        public String content() {
            return content;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (Message) obj;
            return Objects.equals(this.role, that.role) &&
                    Objects.equals(this.content, that.content);
        }

        @Override
        public int hashCode() {
            return Objects.hash(role, content);
        }

    }
}
