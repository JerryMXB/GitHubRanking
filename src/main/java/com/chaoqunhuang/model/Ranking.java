package com.chaoqunhuang.model;

import lombok.Data;
import lombok.ToString;

/**
 *
 * Created by chaoqunhuang on 6/14/18.
 */
@Data
@ToString
public class Ranking {
    private String userName;
    private int contributes;

    public Ranking(String userName, int contributes) {
        this.userName = userName;
        this.contributes = contributes;
    }
}
