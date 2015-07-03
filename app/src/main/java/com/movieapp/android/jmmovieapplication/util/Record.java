package com.movieapp.android.jmmovieapplication.util;

import java.util.HashMap;

/**
 * Created by jmyoo on 2015-06-24.
 */
public class Record extends HashMap<String, Object> {
    public static final Result RESULT_OK = Result.OK;
    public static final Result RESULT_FAIL = Result.FAIL;
    public static final Result RESULT_UNDEF = Result.UNDEF;
    public int id = 0;

    public enum Result {
        OK, FAIL, UNDEF;

        @Override
        public String toString() {
            String s = super.toString();
            return s;
        }

        public static Result parseRes(String s) {
            if (s == null) {
                return UNDEF;
            }
            else if (s.equals("OK")) {
                return Result.OK;
            }
            else if (s.equals("FAIL")) {
                return Result.FAIL;
            }
            else {
                return UNDEF;	// undefined
            }
        }
    }

    public Record setResultOK() {
        return this.setResult(Result.OK, "");
    }

    public Record setResult(Result res, String message) {
        this.put("result", res.toString());
        this.put("message", message);

        return this;
    }

}
