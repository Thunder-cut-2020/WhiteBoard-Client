package com.thunder_cut.netio;

/**
 * Enum class for identify type of data used in net i/o
 */
public enum DataType {

    IMG('I'),
    MSG('M'),
    CMD('C');

    public final char type;

    DataType(char type) {
        this.type = type;
    }
}
