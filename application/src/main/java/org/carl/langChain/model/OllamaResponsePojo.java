package org.carl.langChain.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import java.util.Date;
import java.util.List;

public class OllamaResponsePojo {
    public static volatile OllamaResponsePojo EMPTY = getEmpty();

    private static OllamaResponsePojo getEmpty() {
        if (EMPTY == null) {
            synchronized (OllamaResponsePojo.class) {
                if (EMPTY == null) {
                    EMPTY = new OllamaResponsePojo();
                }
            }
        }
        return EMPTY;
    }

    private String model;

    @JsonAlias({"created_at", "createdAt"})
    private Date createdAt;

    private String response;
    private boolean done;

    @JsonAlias({"done_reason", "doneReason"})
    private String doneReason;

    private List<String> context;

    @JsonAlias({"total_duration", "totalDuration"})
    private long totalDuration;
    @JsonAlias({"load_duration", "loadDuration"})
    private long loadDuration;

    @JsonAlias({"prompt_eval_count", "promptEvalCount"})
    private int promptEvalCount;


    @JsonAlias({"prompt_eval_duration", "promptEvalDuration"})
    private long promptEvalDuration;
    @JsonAlias({"eval_count", "evalCount"})
    private int evalCount;

    @JsonAlias({"eval_duration", "evalDuration"})
    private long evalDuration;

    public String getModel() {
        return model;
    }

    public OllamaResponsePojo setModel(String model) {
        this.model = model;
        return this;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public OllamaResponsePojo setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getResponse() {
        return response;
    }

    public OllamaResponsePojo setResponse(String response) {
        this.response = response;
        return this;
    }

    public boolean isDone() {
        return done;
    }

    public OllamaResponsePojo setDone(boolean done) {
        this.done = done;
        return this;
    }

    public String getDoneReason() {
        return doneReason;
    }

    public OllamaResponsePojo setDoneReason(String doneReason) {
        this.doneReason = doneReason;
        return this;
    }

    public List<String> getContext() {
        return context;
    }

    public OllamaResponsePojo setContext(List<String> context) {
        this.context = context;
        return this;
    }

    public long getTotalDuration() {
        return totalDuration;
    }

    public OllamaResponsePojo setTotalDuration(long totalDuration) {
        this.totalDuration = totalDuration;
        return this;
    }

    public long getLoadDuration() {
        return loadDuration;
    }

    public OllamaResponsePojo setLoadDuration(long loadDuration) {
        this.loadDuration = loadDuration;
        return this;
    }

    public int getPromptEvalCount() {
        return promptEvalCount;
    }

    public OllamaResponsePojo setPromptEvalCount(int promptEvalCount) {
        this.promptEvalCount = promptEvalCount;
        return this;
    }

    public long getPromptEvalDuration() {
        return promptEvalDuration;
    }

    public OllamaResponsePojo setPromptEvalDuration(long promptEvalDuration) {
        this.promptEvalDuration = promptEvalDuration;
        return this;
    }

    public int getEvalCount() {
        return evalCount;
    }

    public OllamaResponsePojo setEvalCount(int evalCount) {
        this.evalCount = evalCount;
        return this;
    }

    public long getEvalDuration() {
        return evalDuration;
    }

    public OllamaResponsePojo setEvalDuration(long evalDuration) {
        this.evalDuration = evalDuration;
        return this;
    }
}