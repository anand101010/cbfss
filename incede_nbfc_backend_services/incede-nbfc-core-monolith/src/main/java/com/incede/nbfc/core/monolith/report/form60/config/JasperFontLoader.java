package com.incede.nbfc.core.monolith.report.form60.config;

import com.incede.nbfc.core.monolith.exception.BusinessException;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.io.IOException;
import java.io.InputStream;

@Component
public class JasperFontLoader {

    private static final Logger log = LoggerFactory.getLogger(JasperFontLoader.class);

    @PostConstruct
    public void loadFonts() {
        registerFont("/fonts/MSUIGHUR.TTF", "Microsoft Uighur");
        registerFont("/fonts/ARIALN.TTF", "Arial Narrow");
    }

    protected void registerFont(String fontPath, String fontName) {
        try (InputStream fontStream = getClass().getResourceAsStream(fontPath)) {
            if (fontStream == null) {
                throw new BusinessException(String.format("Font '%s' not found in resources/fonts", fontName));
            }

            Font font = createFontSafely(fontStream, fontName);
            registerWithSystem(font, fontName);

            log.info("Font '{}' registered successfully.", fontName);

        } catch (BusinessException e) {
            throw e; // already wrapped
        } catch (Exception e) {
            throw new BusinessException(String.format("Unexpected error registering font '%s': %s", fontName, e.getMessage()), e);
        }
    }

    /**
     * Creates a Font safely, wrapping checked exceptions into BusinessException.
     */
    protected Font createFontSafely(InputStream stream, String fontName) {
        try {
            return Font.createFont(Font.TRUETYPE_FONT, stream);
        } catch (FontFormatException e) {
            throw new BusinessException(String.format("Invalid format for font '%s': %s", fontName, e.getMessage()), e);
        } catch (IOException e) {
            throw new BusinessException(String.format("I/O error loading font '%s': %s", fontName, e.getMessage()), e);
        } catch (Exception e) {
            throw new BusinessException(String.format("Unexpected error creating font '%s': %s", fontName, e.getMessage()), e);
        }
    }

    /**
     * Registers the Font with the system safely, wrapping potential Headless or Security issues.
     */
    protected void registerWithSystem(Font font, String fontName) {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
        } catch (HeadlessException e) {
            throw new BusinessException(String.format("Cannot register font '%s' in headless environment: %s", fontName, e.getMessage()), e);
        } catch (SecurityException e) {
            throw new BusinessException(String.format("Security restriction registering font '%s': %s", fontName, e.getMessage()), e);
        }
    }
}
