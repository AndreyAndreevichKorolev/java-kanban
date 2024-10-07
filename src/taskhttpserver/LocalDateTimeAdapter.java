package taskhttpserver;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm");

    @Override
    public void write(final JsonWriter writer, final LocalDateTime time) throws IOException {
        if (time != null) {
            writer.value(time.format(formatter));
        } else {
            writer.value("null");
        }

    }

    @Override
    public LocalDateTime read(final JsonReader reader) throws IOException {
        String body = reader.nextString();
        if (!body.equals("null")) {
            return LocalDateTime.parse(body, formatter);
        } else {
            return null;
        }

    }
}
