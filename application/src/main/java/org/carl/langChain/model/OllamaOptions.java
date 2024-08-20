package org.carl.langChain.model;

import java.util.List;

public record OllamaOptions(Double temperature, Integer topK, Double topP, Double repeatPenalty, Integer seed, Integer numPredict, Integer numCtx, List<String> stop) {
    public OllamaOptions(Double temperature, Integer topK, Double topP, Double repeatPenalty, Integer seed, Integer numPredict, Integer numCtx, List<String> stop) {
        this.temperature = temperature;
        this.topK = topK;
        this.topP = topP;
        this.repeatPenalty = repeatPenalty;
        this.seed = seed;
        this.numPredict = numPredict;
        this.numCtx = numCtx;
        this.stop = stop;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Double temperature() {
        return this.temperature;
    }

    public Integer topK() {
        return this.topK;
    }

    public Double topP() {
        return this.topP;
    }

    public Double repeatPenalty() {
        return this.repeatPenalty;
    }

    public Integer seed() {
        return this.seed;
    }

    public Integer numPredict() {
        return this.numPredict;
    }

    public Integer numCtx() {
        return this.numCtx;
    }

    public List<String> stop() {
        return this.stop;
    }

    public static class Builder {
        private Double temperature;
        private Integer topK;
        private Double topP;
        private Double repeatPenalty;
        private Integer seed;
        private Integer numPredict;
        private Integer numCtx;
        private List<String> stop;

        public Builder() {
        }

        public Builder temperature(Double temperature) {
            this.temperature = temperature;
            return this;
        }

        public Builder topK(Integer topK) {
            this.topK = topK;
            return this;
        }

        public Builder topP(Double topP) {
            this.topP = topP;
            return this;
        }

        public Builder repeatPenalty(Double repeatPenalty) {
            this.repeatPenalty = repeatPenalty;
            return this;
        }

        public Builder seed(Integer seed) {
            this.seed = seed;
            return this;
        }

        public Builder numPredict(Integer numPredict) {
            this.numPredict = numPredict;
            return this;
        }

        public Builder numCtx(Integer numCtx) {
            this.numCtx = numCtx;
            return this;
        }

        public Builder stop(List<String> stop) {
            this.stop = stop;
            return this;
        }

        public OllamaOptions build() {
            return new OllamaOptions(this.temperature, this.topK, this.topP, this.repeatPenalty, this.seed, this.numPredict, this.numCtx, this.stop);
        }
    }
}
