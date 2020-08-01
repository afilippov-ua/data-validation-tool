package com.filippov.data.validation.tool.validation.transformer;

public abstract class AbstractTransformer implements Transformer {
    private Transformer next;

    public AbstractTransformer() {
    }

    @Override
    public Transformer getNext() {
        return next;
    }

    @Override
    public void setNext(Transformer nextTransformer) {
        this.next = nextTransformer;
    }

    @Override
    public Transformer getLastTransformer() {
        if (next == null) {
            return this;
        } else {
            return next.getLastTransformer();
        }
    }
}
