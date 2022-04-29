package com.github.quaoz.tests;

import com.github.quaoz.database.RecordHandler;

public class RecordHandlerTest implements RecordHandler<RecordTest> {
    private static final int length = 16;

    @Override
    public int recordLength() {
        return length;
    }

    @Override
    public RecordTest getRecord(String source) {
        return null;
    }
}
