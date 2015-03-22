package com.equalexperts.weather1self.model.lib1self;

import org.json.JSONObject;

public class Stream {

    private String streamId;
    private String readToken;
    private String writeToken;

    public Stream(String streamId, String readToken, String writeToken) {
        this.streamId = streamId;
        this.readToken = readToken;
        this.writeToken = writeToken;
    }

    public String getId() {
        return streamId;
    }

    public String getReadToken() {
        return readToken;
    }

    public String getWriteToken() {
        return writeToken;
    }

    @Override
    public String toString() {
        return "[" + streamId + ", " + readToken + ", " + writeToken + "]";
    }

    public static Stream fromJSON(JSONObject streamJSON) {
        return new Stream(streamJSON.getString("streamId"), streamJSON.getString("readToken"),
                streamJSON.getString("writeToken"));
    }
}
