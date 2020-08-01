package com.filippov.data.validation.tool.validation.transformer;

public class TransformerBuilder {
    private Transformer head;
    private Transformer tail;

    private TransformerBuilder() {
    }

    private TransformerBuilder(Transformer initial) {
        this.head = initial;
        this.tail = this.head;
    }

    public static TransformerBuilder withInitialTransformer(Transformer initialTransformer) {
        return new TransformerBuilder(initialTransformer);
    }

    public TransformerBuilder thenTransform(Transformer next) {
        this.tail.setNext(next);
        this.tail = next;
        return this;
    }

    public Transformer build() {
        return this.head;
    }
}
