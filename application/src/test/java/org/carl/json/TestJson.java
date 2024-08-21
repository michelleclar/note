package org.carl.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Test;
import java.io.IOException;

public class TestJson {
    static String json =
        """
                {
                    "model": "llama3.1",
                    "created_at": "2024-08-17T10:22:25.962860024Z",
                    "response": "",
                    "done": true,
                    "done_reason": "stop",
                    "context": [
                        128009,
                        128006,
                        882,
                        128007,
                        271,
                        10445,
                        374,
                        279,
                        13180,
                        6437,
                        30,
                        123768,
                        119,
                        115605,
                        13153,
                        108891,
                        128009,
                        128006,
                        78191,
                        128007,
                        271,
                        115427,
                        36827,
                        9554,
                        124510,
                        39135,
                        21043,
                        116382,
                        27384,
                        102146,
                        16325,
                        35894,
                        102146,
                        17620,
                        118920,
                        113266,
                        3922,
                        101828,
                        101426,
                        101307,
                        107471,
                        105644,
                        3922,
                        105390,
                        115931,
                        113961,
                        110354,
                        115427,
                        118458,
                        103682,
                        46961,
                        1811,
                        109491,
                        112886,
                        43240,
                        9039,
                        105703,
                        98739,
                        110354,
                        106947,
                        115427,
                        36827,
                        104122,
                        103668,
                        101828,
                        36827,
                        108966,
                        57752,
                        103309,
                        110477,
                        117373,
                        101600,
                        119237,
                        113954,
                        1811
                    ],
                    "total_duration": 9065919867,
                    "load_duration": 11961013,
                    "prompt_eval_count": 22,
                    "prompt_eval_duration": 1115797000,
                    "eval_count": 55,
                    "eval_duration": 7896607000
                }
            """;

    @Test
    public void testLocalDateTimeToJsonValue() {
        JsonObject _json = new JsonObject(json);
        System.out.println(_json);
    }
}