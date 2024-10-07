package taskhttpserver;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;
import java.time.format.DateTimeFormatter;

public class DurationAdapter extends TypeAdapter<Duration> {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm");

    @Override
    public void write(final JsonWriter writer, final Duration time) throws IOException {
        writer.value(time.toMinutes());
    }

    @Override
    public Duration read(final JsonReader reader) throws IOException {

        return Duration.ofMinutes(Long.parseLong(reader.nextString()));
    }
}
