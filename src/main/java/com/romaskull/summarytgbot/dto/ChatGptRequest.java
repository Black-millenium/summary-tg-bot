package com.romaskull.summarytgbot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ChatGptRequest {

    private String model;

    @JsonProperty("max_tokens")
    private int maxTokens;

    private double temperature;

    private List<GptMessage> messages;

    public ChatGptRequest(String model,
                          int maxTokens,
                          double temperature,
                          List<GptMessage> messages) {
        this.model = model;
        this.maxTokens = maxTokens;
        this.temperature = temperature;
        this.messages = messages;
    }

    public ChatGptRequest() {
    }

    public static ChatGptRequestBuilder builder() {
        return new ChatGptRequestBuilder();
    }

    public String getModel() {
        return this.model;
    }

    public int getMaxTokens() {
        return this.maxTokens;
    }

    public double getTemperature() {
        return this.temperature;
    }

    public List<GptMessage> getMessages() {
        return this.messages;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @JsonProperty("max_tokens")
    public void setMaxTokens(int maxTokens) {
        this.maxTokens = maxTokens;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public void setMessages(List<GptMessage> messages) {
        this.messages = messages;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ChatGptRequest)) return false;
        final ChatGptRequest other = (ChatGptRequest) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$model = this.getModel();
        final Object other$model = other.getModel();
        if (this$model == null ? other$model != null : !this$model.equals(other$model)) return
                false;
        if (this.getMaxTokens() != other.getMaxTokens()) return false;
        if (Double.compare(this.getTemperature(), other.getTemperature()) != 0) return false;
        final Object this$messages = this.getMessages();
        final Object other$messages = other.getMessages();
        if (this$messages == null ? other$messages != null :
                !this$messages.equals(other$messages)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ChatGptRequest;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $model = this.getModel();
        result = result * PRIME + ($model == null ? 43 : $model.hashCode());
        result = result * PRIME + this.getMaxTokens();
        final long $temperature = Double.doubleToLongBits(this.getTemperature());
        result = result * PRIME + (int) ($temperature >>> 32 ^ $temperature);
        final Object $messages = this.getMessages();
        result = result * PRIME + ($messages == null ? 43 : $messages.hashCode());
        return result;
    }

    public String toString() {
        return "ChatGptRequest(model=" + this.getModel()
                + ", maxTokens=" + this.getMaxTokens()
                + ", temperature=" + this.getTemperature()
                + ", messages=" + this.getMessages() + ")";
    }

    public static class ChatGptRequestBuilder {
        private String model;
        private int maxTokens;
        private double temperature;
        private List<GptMessage> messages;

        ChatGptRequestBuilder() {
        }

        public ChatGptRequestBuilder model(String model) {
            this.model = model;
            return this;
        }

        @JsonProperty("max_tokens")
        public ChatGptRequestBuilder maxTokens(int maxTokens) {
            this.maxTokens = maxTokens;
            return this;
        }

        public ChatGptRequestBuilder temperature(double temperature) {
            this.temperature = temperature;
            return this;
        }

        public ChatGptRequestBuilder messages(List<GptMessage> messages) {
            this.messages = messages;
            return this;
        }

        public ChatGptRequest build() {
            return new ChatGptRequest(this.model, this.maxTokens, this.temperature,
                    this.messages);
        }

        public String toString() {
            return "ChatGptRequest.ChatGptRequestBuilder(model=" + this.model
                    + ", maxTokens=" + this.maxTokens
                    + ", temperature=" + this.temperature
                    + ", messages=" + this.messages + ")";
        }
    }
}