package com.uestc.lyreg.customview.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Administrator on 2016/6/23.
 *
 * @Author lyreg
 */
public class GankResult {
    private boolean error;

    @JsonProperty("results")
    private List<GankFuli> fuli;

    public GankResult() {}

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<GankFuli> getFuli() {
        return fuli;
    }

    public void setFuli(List<GankFuli> fuli) {
        this.fuli = fuli;
    }
}
