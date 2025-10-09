package com.incede.nbfc.core.monolith.common;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * Custom message converter to handle cases where external APIs return JSON payloads
 * but incorrectly set the Content-Type as "text/html" or "text/plain".
 *
 * By extending {@link MappingJackson2HttpMessageConverter}, this converter allows
 * Spring to still parse those responses as JSON, preventing HttpMessageNotReadableException.
 *
 * Use case:
 * Some external services return a valid JSON body but send headers like:
 *     Content-Type: text/html
 *     or
 *     Content-Type: text/plain
 *
 * Without this converter, Spring would not treat the response as JSON.
 * This class fixes that by explicitly allowing text/html and text/plain
 * to be parsed by Jackson as JSON.
 */
public class TextHtmlJsonMessageConverter extends MappingJackson2HttpMessageConverter {

    public TextHtmlJsonMessageConverter() {
        List<MediaType> mediaTypes = new ArrayList<>(getSupportedMediaTypes());
        mediaTypes.add(MediaType.TEXT_HTML);
        mediaTypes.add(MediaType.TEXT_PLAIN);
        setSupportedMediaTypes(mediaTypes);
    }
}
