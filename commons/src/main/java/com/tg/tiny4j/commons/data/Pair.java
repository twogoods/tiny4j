package com.tg.tiny4j.commons.data;

/**
 * Created by twogoods on 16/10/29.
 */
public class Pair<L,R> {
    private L l;
    private R r;

    private Pair(L l, R r) {
        this.l = l;
        this.r = r;
    }

    public static <L, R> Pair<L,R> of(L left, R right){
        return new Pair<L, R>(left,right);
    }

    public L getL() {
        return l;
    }

    public void setL(L l) {
        this.l = l;
    }

    public R getR() {
        return r;
    }

    public void setR(R r) {
        this.r = r;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "l=" + l +
                ", r=" + r +
                '}';
    }
}
