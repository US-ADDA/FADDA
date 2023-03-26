package com.fadda.common.io;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.function.Function;


public class WriterManager {

    // Attributes -------------------------------------------------------------
    private String input;

    // Constructors -----------------------------------------------------------
    public WriterManager(String template) throws IOException {
        String location = getResource(template).orElseThrow(() -> new FileNotFoundException(template)).getFile();
        File file = new File(location);
        input = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
    }

    // Methods ----------------------------------------------------------------
    private Optional<URL> getResource(String template) {
        return Optional.ofNullable(getClass().getClassLoader().getResource(template));
    }

    public WriterManager map(Function<String, String> function) {
        this.input = function.apply(input);
        return this;
    }

    public void write(String title) throws IOException {
        FileUtils.writeStringToFile(new File(title), input, StandardCharsets.UTF_8);
    }

    public String get() {
        return input;
    }


}
