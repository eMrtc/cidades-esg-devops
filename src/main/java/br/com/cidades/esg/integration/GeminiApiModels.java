package br.com.cidades.esg.integration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class GeminiApiModels {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GeminiRequest {
        private List<Content> contents;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Content {
        private List<Part> parts;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Part {
        private String text;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GeminiResponse {
        private List<Candidate> candidates;

        public String getFirstText() {
            if (candidates != null && !candidates.isEmpty()) {
                Candidate first = candidates.get(0);
                if (first.getContent() != null && first.getContent().getParts() != null && !first.getContent().getParts().isEmpty()) {
                    return first.getContent().getParts().get(0).getText();
                }
            }
            return "";
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Candidate {
        private Content content;
    }
}
