package com.filippov.data.validation.tool.datastorage;

public enum RelationType {
    LEFT, RIGHT;

    public static RelationType parse(String val)  {
        switch (val.toLowerCase()) {
            case "left":
                return RelationType.LEFT;
            case "right":
                return RelationType.RIGHT;
            default:
                throw new IllegalArgumentException("Unsupported relation type: " + val);
        }
    }
}
