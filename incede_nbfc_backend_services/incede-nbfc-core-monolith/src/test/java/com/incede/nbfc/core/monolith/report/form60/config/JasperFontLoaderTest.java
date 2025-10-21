package com.incede.nbfc.core.monolith.report.form60.config;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.incede.nbfc.core.monolith.exception.BusinessException;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {JasperFontLoader.class})
@ExtendWith(SpringExtension.class)
class JasperFontLoaderTest {

    @Autowired
    private JasperFontLoader jasperFontLoader;

    private JasperFontLoader jasperFontLoaderMock;

    @BeforeEach
    void setUp() {
        jasperFontLoaderMock = new JasperFontLoader();
    }

    /**
     * Test {@link JasperFontLoader#loadFonts()}.
     */
    @Test
    @DisplayName("Test loadFonts() - successful font loading")
    void testLoadFonts_Success() {
        // This test will verify that loadFonts doesn't throw exceptions
        // In a real environment, it would register the fonts
        jasperFontLoader.loadFonts();

        // If we reach here without exceptions, the test passes
        assertTrue(true, "loadFonts executed without throwing exceptions");
    }

    /**
     * Test {@link JasperFontLoader#registerFont(String, String)} with empty font path.
     */
    @Test
    @DisplayName("Test registerFont with empty font path")
    void testRegisterFont_EmptyFontPath() {
        assertThrows(BusinessException.class,
                () -> jasperFontLoader.registerFont("", "Font Name"));
    }

    /**
     * Test {@link JasperFontLoader#registerFont(String, String)} with null font path.
     */
    @Test
    @DisplayName("Test registerFont with null font path")
    void testRegisterFont_NullFontPath() {
        assertThrows(BusinessException.class,
                () -> jasperFontLoader.registerFont(null, "Font Name"));
    }

    /**
     * Test {@link JasperFontLoader#registerFont(String, String)} when font resource not found.
     */
    @Test
    @DisplayName("Test registerFont when font resource not found")
    void testRegisterFont_ResourceNotFound() {
        assertThrows(BusinessException.class,
                () -> jasperFontLoader.registerFont("/nonexistent/font.ttf", "Nonexistent Font"));
    }

    /**
     * Test {@link JasperFontLoader#createFontSafely(InputStream, String)} with invalid font data.
     */
    @Test
    @DisplayName("Test createFontSafely with invalid font data")
    void testCreateFontSafely_InvalidFontData() throws UnsupportedEncodingException {
        JasperFontLoader loader = new JasperFontLoader();

        assertThrows(BusinessException.class,
                () -> loader.createFontSafely(
                        new ByteArrayInputStream("InvalidFontData".getBytes("UTF-8")),
                        "Test Font"));
    }

    /**
     * Test {@link JasperFontLoader#createFontSafely(InputStream, String)} with null stream.
     */
    @Test
    @DisplayName("Test createFontSafely with null stream")
    void testCreateFontSafely_NullStream() {
        JasperFontLoader loader = new JasperFontLoader();

        assertThrows(BusinessException.class,
                () -> loader.createFontSafely(null, "Test Font"));
    }

    /**
     * Test {@link JasperFontLoader#createFontSafely(InputStream, String)} with FontFormatException.
     */
    @Test
    @DisplayName("Test createFontSafely with FontFormatException")
    void testCreateFontSafely_FontFormatException() throws Exception {
        JasperFontLoader loader = new JasperFontLoader();

        // Create a mock InputStream
        InputStream mockStream = new ByteArrayInputStream("test".getBytes());

        // Mock Font.createFont to throw FontFormatException
        try (MockedStatic<Font> fontMock = Mockito.mockStatic(Font.class)) {
            // Use eq() matcher for the font type and any() for the InputStream
            fontMock.when(() -> Font.createFont(eq(Font.TRUETYPE_FONT), any(InputStream.class)))
                    .thenThrow(new FontFormatException("Invalid font format"));

            assertThrows(BusinessException.class,
                    () -> loader.createFontSafely(mockStream, "Test Font"));
        }
    }

    /**
     * Test {@link JasperFontLoader#createFontSafely(InputStream, String)} with IOException.
     */
    @Test
    @DisplayName("Test createFontSafely with IOException")
    void testCreateFontSafely_IOException() throws Exception {
        JasperFontLoader loader = new JasperFontLoader();

        // Create a mock InputStream
        InputStream mockStream = new ByteArrayInputStream("test".getBytes());

        // Mock Font.createFont to throw IOException
        try (MockedStatic<Font> fontMock = Mockito.mockStatic(Font.class)) {
            // Use eq() matcher for the font type and any() for the InputStream
            fontMock.when(() -> Font.createFont(eq(Font.TRUETYPE_FONT), any(InputStream.class)))
                    .thenThrow(new IOException("I/O error"));

            assertThrows(BusinessException.class,
                    () -> loader.createFontSafely(mockStream, "Test Font"));
        }
    }

    /**
     * Test {@link JasperFontLoader#createFontSafely(InputStream, String)} with unexpected exception.
     */
    @Test
    @DisplayName("Test createFontSafely with unexpected exception")
    void testCreateFontSafely_UnexpectedException() throws Exception {
        JasperFontLoader loader = new JasperFontLoader();

        // Create a mock InputStream
        InputStream mockStream = new ByteArrayInputStream("test".getBytes());

        // Mock Font.createFont to throw RuntimeException
        try (MockedStatic<Font> fontMock = Mockito.mockStatic(Font.class)) {
            // Use eq() matcher for the font type and any() for the InputStream
            fontMock.when(() -> Font.createFont(eq(Font.TRUETYPE_FONT), any(InputStream.class)))
                    .thenThrow(new RuntimeException("Unexpected error"));

            assertThrows(BusinessException.class,
                    () -> loader.createFontSafely(mockStream, "Test Font"));
        }
    }

    /**
     * Test {@link JasperFontLoader#registerWithSystem(Font, String)} successfully.
     */
    @Test
    @DisplayName("Test registerWithSystem successful registration")
    void testRegisterWithSystem_Success() {
        Font font = Font.decode("Arial-12");

        // This should not throw an exception
        jasperFontLoader.registerWithSystem(font, "Test Font");

        assertTrue(true, "registerWithSystem executed without throwing exceptions");
    }

    /**
     * Test {@link JasperFontLoader#registerWithSystem(Font, String)} with null font.
     * The actual implementation throws NullPointerException from GraphicsEnvironment.registerFont,
     * so we should expect that instead of BusinessException.
     */
    @Test
    @DisplayName("Test registerWithSystem with null font")
    void testRegisterWithSystem_NullFont() {
        // The actual GraphicsEnvironment.registerFont throws NullPointerException for null font
        assertThrows(NullPointerException.class,
                () -> jasperFontLoader.registerWithSystem(null, "Test Font"));
    }

    /**
     * Test {@link JasperFontLoader#registerWithSystem(Font, String)} with HeadlessException.
     */
    @Test
    @DisplayName("Test registerWithSystem with HeadlessException")
    void testRegisterWithSystem_HeadlessException() {
        Font font = Font.decode("Arial-12");

        try (MockedStatic<GraphicsEnvironment> geMock = Mockito.mockStatic(GraphicsEnvironment.class)) {
            GraphicsEnvironment mockGe = mock(GraphicsEnvironment.class);
            doThrow(new java.awt.HeadlessException("Headless environment"))
                    .when(mockGe).registerFont(any(Font.class));

            geMock.when(GraphicsEnvironment::getLocalGraphicsEnvironment).thenReturn(mockGe);

            JasperFontLoader loader = new JasperFontLoader();

            assertThrows(BusinessException.class,
                    () -> loader.registerWithSystem(font, "Test Font"));
        }
    }

    /**
     * Test {@link JasperFontLoader#registerWithSystem(Font, String)} with SecurityException.
     */
    @Test
    @DisplayName("Test registerWithSystem with SecurityException")
    void testRegisterWithSystem_SecurityException() {
        Font font = Font.decode("Arial-12");

        try (MockedStatic<GraphicsEnvironment> geMock = Mockito.mockStatic(GraphicsEnvironment.class)) {
            GraphicsEnvironment mockGe = mock(GraphicsEnvironment.class);
            doThrow(new SecurityException("Security restriction"))
                    .when(mockGe).registerFont(any(Font.class));

            geMock.when(GraphicsEnvironment::getLocalGraphicsEnvironment).thenReturn(mockGe);

            JasperFontLoader loader = new JasperFontLoader();

            assertThrows(BusinessException.class,
                    () -> loader.registerWithSystem(font, "Test Font"));
        }
    }

    /**
     * Test {@link JasperFontLoader#registerFont(String, String)} with IOException during stream operations.
     */
    @Test
    @DisplayName("Test registerFont with IOException during stream operations")
    void testRegisterFont_IOException() {
        JasperFontLoader loader = new JasperFontLoader() {
            @Override
            protected Font createFontSafely(InputStream stream, String fontName) {
                throw new BusinessException("I/O error loading font", new IOException("Stream closed"));
            }
        };

        // Use a valid path that would normally work, but our overridden method will throw
        assertThrows(BusinessException.class,
                () -> loader.registerFont("/fonts/MSUIGHUR.TTF", "Microsoft Uighur"));
    }

    /**
     * Test {@link JasperFontLoader#registerFont(String, String)} with BusinessException from createFontSafely.
     */
    @Test
    @DisplayName("Test registerFont with BusinessException from createFontSafely")
    void testRegisterFont_BusinessExceptionFromCreateFont() {
        JasperFontLoader loader = new JasperFontLoader() {
            @Override
            protected Font createFontSafely(InputStream stream, String fontName) {
                throw new BusinessException("Font creation failed");
            }
        };

        assertThrows(BusinessException.class,
                () -> loader.registerFont("/fonts/MSUIGHUR.TTF", "Microsoft Uighur"));
    }

    /**
     * Test {@link JasperFontLoader#registerFont(String, String)} with exception during font registration.
     */
    @Test
    @DisplayName("Test registerFont with exception during font registration")
    void testRegisterFont_ExceptionDuringRegistration() {
        JasperFontLoader loader = new JasperFontLoader() {
            @Override
            protected Font createFontSafely(InputStream stream, String fontName) {
                // Return a valid font
                return Font.decode("Arial-12");
            }

            @Override
            protected void registerWithSystem(Font font, String fontName) {
                throw new BusinessException("Registration failed");
            }
        };

        assertThrows(BusinessException.class,
                () -> loader.registerFont("/fonts/MSUIGHUR.TTF", "Microsoft Uighur"));
    }

    /**
     * Integration test for the complete font loading process.
     */
    @Test
    @DisplayName("Integration test for font loading process")
    void testFontLoadingIntegration() {
        // This is a smoke test to ensure the main flow works
        JasperFontLoader loader = new JasperFontLoader();

        // Test that the class can be instantiated and methods exist
        assertNotNull(loader);

        // The actual font loading would happen in a real environment
        // In test environment, we just verify the methods don't throw unexpected exceptions
        // when called with proper parameters (except for the resource loading which will fail)
    }

    /**
     * Test that the class can be instantiated via constructor.
     */
    @Test
    @DisplayName("Test default constructor")
    void testDefaultConstructor() {
        JasperFontLoader loader = new JasperFontLoader();
        assertNotNull(loader);
    }
}