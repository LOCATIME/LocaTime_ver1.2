package kr.co.locatime.decoder;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Joonha on 2015-12-07.
 */
public class Decoder_Stream {

    public String convertToString(InputStream is) {
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            Log.d("convert_error", e.toString());
        } finally {
            try {
                is.close();
            } catch (Exception e) {
                Log.d("closing_error", e.toString());
            }
        }

        return sb.toString();

    }

}
