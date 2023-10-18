package com.romaskull.summarytgbot.dto;

public class GptMessage {

    private GptRole role;

    private String content;

    public GptMessage(GptRole role, String content) {
        this.role = role;
        this.content = content;
    }

    public GptMessage() {
    }

    public static GptMessageBuilder builder() {
        return new GptMessageBuilder();
    }

    public GptRole getRole() {
        return this.role;
    }

    public String getContent() {
        return this.content;
    }

    public void setRole(GptRole role) {
        this.role = role;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof GptMessage)) return false;
        final GptMessage other = (GptMessage) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$role = this.getRole();
        final Object other$role = other.getRole();
        if (this$role == null ? other$role != null : !this$role.equals(other$role)) return false;
        final Object this$content = this.getContent();
        final Object other$content = other.getContent();
        if (this$content == null ? other$content != null : !this$content.equals(other$content))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof GptMessage;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $role = this.getRole();
        result = result * PRIME + ($role == null ? 43 : $role.hashCode());
        final Object $content = this.getContent();
        result = result * PRIME + ($content == null ? 43 : $content.hashCode());
        return result;
    }

    public String toString() {
        return "GptMessage(role=" + this.getRole() + ", content=" + this.getContent() + ")";
    }

    public static class GptMessageBuilder {
        private GptRole role;
        private String content;

        GptMessageBuilder() {
        }

        public GptMessageBuilder role(GptRole role) {
            this.role = role;
            return this;
        }

        public GptMessageBuilder content(String content) {
            this.content = content;
            return this;
        }

        public GptMessage build() {
            return new GptMessage(this.role, this.content);
        }

        public String toString() {
            return "GptMessage.GptMessageBuilder(role=" + this.role + ", content=" + this.content +
                    ")";
        }
    }
}
