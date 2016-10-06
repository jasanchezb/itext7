/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV
    Authors: Bruno Lowagie, Paulo Soares, et al.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License version 3
    as published by the Free Software Foundation with the addition of the
    following permission added to Section 15 as permitted in Section 7(a):
    FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
    ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
    OF THIRD PARTY RIGHTS

    This program is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
    or FITNESS FOR A PARTICULAR PURPOSE.
    See the GNU Affero General Public License for more details.
    You should have received a copy of the GNU Affero General Public License
    along with this program; if not, see http://www.gnu.org/licenses or write to
    the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
    Boston, MA, 02110-1301 USA, or download the license from the following URL:
    http://itextpdf.com/terms-of-use/

    The interactive user interfaces in modified source and object code versions
    of this program must display Appropriate Legal Notices, as required under
    Section 5 of the GNU Affero General Public License.

    In accordance with Section 7(b) of the GNU Affero General Public License,
    a covered work must retain the producer line in every PDF that is created
    or manipulated using iText.

    You can be released from the requirements of the license by purchasing
    a commercial license. Buying such a license is mandatory as soon as you
    develop commercial activities involving the iText software without
    disclosing the source code of your own applications.
    These activities include: offering paid services to customers as an ASP,
    serving PDFs on the fly in a web application, shipping iText with a closed
    source product.

    For more information, please contact iText Software Corp. at this
    address: sales@itextpdf.com
 */
package com.itextpdf.io.font;

import com.itextpdf.io.IOException;
import com.itextpdf.io.util.ArrayUtil;

import java.util.Set;

/**
 * Provides methods for creating various types of fonts.
 */
public final class FontProgramFactory {

    /**
     * This is the default value of the <VAR>cached</VAR> variable.
     */
    private static boolean DEFAULT_CACHED = true;

    private static FontRegisterProvider fontRegisterProvider = new FontRegisterProvider();

    private FontProgramFactory() {
    }

    /**
     * Creates a new standard Helvetica font program file.
     *
     * @return a {@link FontProgram} object with Helvetica font description
     */
    public static FontProgram createFont() throws java.io.IOException {
        return createFont(FontConstants.HELVETICA);
    }

    /**
     * Creates a new font program. This font program can be one of the 14 built in fonts,
     * a Type1 font referred to by an AFM or PFM file, a TrueType font (simple or one from collection) or
     * a CJK font from the Adobe Asian Font Pack.
     * TrueType fonts and CJK fonts can have an optional style modifier
     * appended to the name. These modifiers are: Bold, Italic and BoldItalic. An
     * example would be "STSong-Light,Bold". Note that this modifiers do not work if
     * the font is embedded. Fonts in TrueType Collections are addressed by index such as "msgothic.ttc,1".
     * This would get the second font (indexes start at 0), in this case "MS PGothic".
     * <p/>
     * The fonts are cached and if they already exist they are extracted from the cache,
     * not parsed again.
     * <p/>
     *
     * @param fontProgram the name of the font or its location on file
     * @return returns a new {@link FontProgram}. This font program may come from the cache
     */
    public static FontProgram createFont(String fontProgram) throws java.io.IOException {
        return createFont(fontProgram, null, DEFAULT_CACHED);
    }

    /**
     * Creates a new font program. This font program can be one of the 14 built in fonts,
     * a Type1 font referred to by an AFM or PFM file, a TrueType font (simple or one from collection) or
     * a CJK font from the Adobe Asian Font Pack.
     * TrueType fonts and CJK fonts can have an optional style modifier
     * appended to the name. These modifiers are: Bold, Italic and BoldItalic. An
     * example would be "STSong-Light,Bold". Note that this modifiers do not work if
     * the font is embedded. Fonts in TrueType Collections are addressed by index such as "msgothic.ttc,1".
     * This would get the second font (indexes start at 0), in this case "MS PGothic".
     * <p/>
     * The fonts are cached and if they already exist they are extracted from the cache,
     * not parsed again.
     * <p/>
     *
     * @param fontProgram the name of the font or its location on file
     * @param cached whether to to cache this font program after it has been loaded
     * @return returns a new {@link FontProgram}. This font program may come from the cache
     */
    public static FontProgram createFont(String fontProgram, boolean cached) throws java.io.IOException {
        return createFont(fontProgram, null, cached);
    }

    /**
     * Creates a new font program. This font program can be one of the 14 built in fonts,
     * a Type1 font referred to by an AFM or PFM file, a TrueType font (simple only) or
     * a CJK font from the Adobe Asian Font Pack.
     * TrueType fonts and CJK fonts can have an optional style modifier
     * appended to the name. These modifiers are: Bold, Italic and BoldItalic. An
     * example would be "STSong-Light,Bold". Note that this modifiers do not work if
     * the font is embedded. Fonts in TrueType Collections are addressed by index such as "msgothic.ttc,1".
     * This would get the second font (indexes start at 0), in this case "MS PGothic".
     * <p/>
     * The fonts are cached and if they already exist they are extracted from the cache,
     * not parsed again.
     * <p/>
     *
     * @param fontProgram the byte contents of the font program
     * @return returns a new {@link FontProgram}. This font program may come from the cache
     */
    public static FontProgram createFont(byte[] fontProgram) throws java.io.IOException {
        return createFont(null, fontProgram, DEFAULT_CACHED);
    }

    /**
     * Creates a new font program. This font program can be one of the 14 built in fonts,
     * a Type 1 font referred to by an AFM or PFM file, a TrueType font (simple only) or
     * a CJK font from the Adobe Asian Font Pack.
     * TrueType fonts and CJK fonts can have an optional style modifier
     * appended to the name. These modifiers are: Bold, Italic and BoldItalic. An
     * example would be "STSong-Light,Bold". Note that this modifiers do not work if
     * the font is embedded. Fonts in TrueType Collections are addressed by index such as "msgothic.ttc,1".
     * This would get the second font (indexes start at 0), in this case "MS PGothic".
     * <p/>
     * The fonts are cached and if they already exist they are extracted from the cache,
     * not parsed again.
     * <p/>
     *
     * @param fontProgram the byte contents of the font program
     * @param cached whether to to cache this font program
     * @return returns a new {@link FontProgram}. This font program may come from the cache
     */
    public static FontProgram createFont(byte[] fontProgram, boolean cached) throws java.io.IOException {
        return createFont(null, fontProgram, cached);
    }

    /**
     * This method is deprecated and will be made private in 7.1
     * @deprecated Use {@link #createFont(byte[], boolean)} or {@link #createFont(String, boolean)}
     */
    @Deprecated
    public static FontProgram createFont(String name, byte[] fontProgram, boolean cached) throws java.io.IOException {
        String baseName = FontProgram.getBaseName(name);

        //yes, we trying to find built-in standard font with original name, not baseName.
        boolean isBuiltinFonts14 = FontConstants.BUILTIN_FONTS_14.contains(name);
        boolean isCidFont = !isBuiltinFonts14 && FontCache.isPredefinedCidFont(baseName);

        FontProgram fontFound;
        String fontKey = null;
        if (cached) {
            if (name != null) {
                fontKey = name;
            } else {
                fontKey = Integer.toString(ArrayUtil.hashCode(fontProgram));
            }
            fontFound = FontCache.getFont(fontKey);
            if (fontFound != null) {
                return fontFound;
            }
        }

        FontProgram fontBuilt = null;
        if (name == null) {
            if (fontProgram != null) {
                try {
                    fontBuilt = new TrueTypeFont(fontProgram);
                } catch (Exception ignored) {
                }
                if (fontBuilt == null) {
                    try {
                        fontBuilt = new Type1Font(null, null, fontProgram, null);
                    } catch (Exception ignored) {
                    }
                }
            }
        } else {
            if (isBuiltinFonts14 || name.toLowerCase().endsWith(".afm") || name.toLowerCase().endsWith(".pfm")) {
                fontBuilt = new Type1Font(name, null, null, null);
            } else if (baseName.toLowerCase().endsWith(".ttf") || baseName.toLowerCase().endsWith(".otf") || baseName.toLowerCase().indexOf(".ttc,") > 0) {
                if (fontProgram != null) {
                    fontBuilt = new TrueTypeFont(fontProgram);
                } else if (baseName.toLowerCase().indexOf(".ttc,") > 0) {
                    // splitting by "," would be easier but is more error-prone
                    String[] parts = baseName.split(".ttc,");
                    try {
                        fontBuilt = new TrueTypeFont(parts[0] + ".ttc", Integer.parseInt(parts[1]));
                    } catch (NumberFormatException nfe) {
                        throw new IOException(nfe.getMessage(), nfe);
                    }
                } else {
                    fontBuilt = new TrueTypeFont(name);
                }
            } else if (isCidFont) {
                fontBuilt = new CidFont(name, FontCache.getCompatibleCmaps(baseName));
            }
        }
        if (fontBuilt == null) {
            if (name != null) {
                throw new IOException(IOException.TypeOfFont1IsNotRecognized).setMessageParams(name);
            } else {
                throw new IOException(IOException.TypeOfFontIsNotRecognized);
            }
        }
        return cached ? FontCache.saveFont(fontBuilt, fontKey) : fontBuilt;
    }

    /**
     * This method is deprecated and will be completely removed in 7.1
     * @deprecated Use {@link #createType1Font(byte[], byte[])} or {@link #createType1Font(String, String)} instead
     */
    @Deprecated
    public static FontProgram createType1Font(String name, byte[] afm, byte[] pfb, boolean cached) throws java.io.IOException {
        FontProgram fontProgram;
        String fontKey = null;
        if (cached) {
            if (name != null) {
                fontKey = name;
            } else {
                fontKey = Integer.toString(ArrayUtil.hashCode(afm));
            }
            fontProgram = FontCache.getFont(fontKey);
            if (fontProgram != null) {
                return fontProgram;
            }
        }
        fontProgram = new Type1Font(name, null, afm, pfb);
        return cached ? FontCache.saveFont(fontProgram, fontKey) : fontProgram;
    }

    /**
     * Creates a new Type 1 font by the byte contents of the corresponding AFM/PFM and PFB files
     * @param afm the contents of the AFM or PFM metrics file
     * @param pfb the contents of the PFB file
     * @return created {@link FontProgram} instance
     */
    public static FontProgram createType1Font(byte[] afm, byte[] pfb) throws java.io.IOException {
        return createType1Font(afm, pfb, DEFAULT_CACHED);
    }

    /**
     * Creates a new Type 1 font by the byte contents of the corresponding AFM/PFM and PFB files
     * @param afm the contents of the AFM or PFM metrics file
     * @param pfb the contents of the PFB file
     * @param cached specifies whether to cache the created {@link FontProgram} or not
     * @return created {@link FontProgram} instance
     */
    public static FontProgram createType1Font(byte[] afm, byte[] pfb, boolean cached) throws java.io.IOException {
        return createType1Font(null, null, afm, pfb, cached);
    }

    /**
     * Creates a new Type 1 font by the corresponding AFM/PFM and PFB files
     * @param metricsPath path to the AFM or PFM metrics file
     * @param binaryPath path to the contents of the PFB file
     * @return created {@link FontProgram} instance
     */
    public static FontProgram createType1Font(String metricsPath, String binaryPath) throws java.io.IOException {
        return createType1Font(metricsPath, binaryPath, DEFAULT_CACHED);
    }

    /**
     * Creates a new Type 1 font by the corresponding AFM/PFM and PFB files
     * @param metricsPath path to the AFM or PFM metrics file
     * @param binaryPath path to the contents of the PFB file
     * @param cached specifies whether to cache the created {@link FontProgram} or not
     * @return created {@link FontProgram} instance
     */
    public static FontProgram createType1Font(String metricsPath, String binaryPath, boolean cached) throws java.io.IOException {
        return createType1Font(metricsPath, binaryPath, null, null, cached);
    }

    /**
     * Creates a new TrueType font program from ttc (TrueType Collection) file.
     *
     * @param ttc      location  of TrueType Collection file (*.ttc)
     * @param ttcIndex the index of the font file from the collection to be read
     * @param cached   true if the font comes from the cache or is added to
     *                 the cache if new, false if the font is always created new
     * @return returns a new {@link FontProgram} instance. This font may come from the cache but only if cached
     * is true, otherwise it will always be created new
     */
    public static FontProgram createFont(String ttc, int ttcIndex, boolean cached) throws java.io.IOException {
        if (cached) {
            FontProgram fontFound = FontCache.getFont(ttc + ttcIndex);
            if (fontFound != null) {
                return fontFound;
            }
        }
        FontProgram fontBuilt = new TrueTypeFont(ttc, ttcIndex);
        return cached ? FontCache.saveFont(fontBuilt, ttc + ttcIndex) : fontBuilt;
    }

    /**
     * Creates a new TrueType font program from ttc (TrueType Collection) file bytes.
     *
     * @param ttc      the content of a TrueType Collection file (*.ttc)
     * @param ttcIndex the index of the font file from the collection to be read
     * @param cached   true if the font comes from the cache or is added to
     *                 the cache if new, false if the font is always created new
     * @return returns a new {@link FontProgram} instance. This font may come from the cache but only if cached
     * is true, otherwise it will always be created new
     */
    public static FontProgram createFont(byte[] ttc, int ttcIndex, boolean cached) throws java.io.IOException {
        String fontKey = null;
        if (cached) {
            fontKey = Integer.toString(ArrayUtil.hashCode(ttc)) + Integer.toString(ttcIndex);
            FontProgram fontFound = FontCache.getFont(fontKey);
            if (fontFound != null) {
                return fontFound;
            }
        }
        FontProgram fontBuilt = new TrueTypeFont(ttc, ttcIndex);
        return cached ? FontCache.saveFont(fontBuilt, fontKey) : fontBuilt;
    }

    /**
     * Creates a FontProgram from the font file that has been previously registered.
     * @param fontName either a font alias, if the font file has been registered with an alias,
     *                 or just a font name otherwise
     * @param style the style of the font to look for. Possible values are listed in {@link FontConstants}.
     *              See {@link FontConstants#BOLD}, {@link FontConstants#ITALIC}, {@link FontConstants#NORMAL},
     *              {@link FontConstants#BOLDITALIC}, {@link FontConstants#UNDEFINED}
     * @param cached whether to try to get the font program from cache
     * @return created {@link FontProgram}
     */
    public static FontProgram createRegisteredFont(String fontName, int style, boolean cached) throws java.io.IOException {
        return fontRegisterProvider.getFont(fontName, style, cached);
    }

    /**
     * Creates a FontProgram from the font file that has been previously registered.
     * @param fontName either a font alias, if the font file has been registered with an alias,
     *                 or just a font name otherwise
     * @param style the style of the font to look for. Possible values are listed in {@link FontConstants}.
     *              See {@link FontConstants#BOLD}, {@link FontConstants#ITALIC}, {@link FontConstants#NORMAL},
     *              {@link FontConstants#BOLDITALIC}, {@link FontConstants#UNDEFINED}
     * @return created {@link FontProgram}
     */
    public static FontProgram createRegisteredFont(String fontName, int style) throws java.io.IOException {
        return fontRegisterProvider.getFont(fontName, style);
    }

    /**
     * Creates a FontProgram from the font file that has been previously registered.
     * @param fontName either a font alias, if the font file has been registered with an alias,
     *                 or just a font name otherwise
     * @return created {@link FontProgram}
     */
    public static FontProgram createRegisteredFont(String fontName) throws java.io.IOException {
        return fontRegisterProvider.getFont(fontName, FontConstants.UNDEFINED);
    }

    /**
     * Register a font by giving explicitly the font family and name.
     *
     * @param familyName the font family
     * @param fullName   the font name
     * @param path       the font path
     */
    public static void registerFontFamily(String familyName, String fullName, String path) {
        fontRegisterProvider.registerFontFamily(familyName, fullName, path);
    }

    /**
     * Registers a .ttf, .otf, .afm, .pfm, or a .ttc font file.
     * In case if TrueType Collection (.ttc), an additional parameter may be specified defining the index of the font
     * to be registered, e.g. "path/to/font/collection.ttc,0". The index is zero-based.
     *
     * @param path the path to a font file
     */
    public static void registerFont(String path) {
        registerFont(path, null);
    }

    /**
     * Register a font file and use an alias for the font contained in it.
     *
     * @param path  the path to a font file
     * @param alias the alias you want to use for the font
     */
    public static void registerFont(String path, String alias) {
        fontRegisterProvider.registerFont(path, alias);
    }

    /**
     * Register all the fonts in a directory.
     *
     * @param dir the directory
     * @return the number of fonts registered
     */
    public static int registerFontDirectory(String dir) {
        return fontRegisterProvider.registerFontDirectory(dir);
    }

    /**
     * Register fonts in some probable directories. It usually works in Windows,
     * Linux and Solaris.
     *
     * @return the number of fonts registered
     */
    public static int registerSystemFontDirectories() {
        return fontRegisterProvider.registerSystemFontDirectories();
    }

    /**
     * Gets a set of registered font names.
     *
     * @return a set of registered fonts
     */
    public static Set<String> getRegisteredFonts() {
        return fontRegisterProvider.getRegisteredFonts();
    }

    /**
     * Gets a set of registered font names.
     *
     * @return a set of registered font families
     */
    public static Set<String> getRegisteredFontFamilies() {
        return fontRegisterProvider.getRegisteredFontFamilies();
    }

    /**
     * Checks if a certain font is registered.
     *
     * @param fontName the name of the font that has to be checked.
     * @return true if the font is found
     */
    public static boolean isRegisteredFont(String fontName) {
        return fontRegisterProvider.isRegisteredFont(fontName);
    }

    private static FontProgram createType1Font(String metricsPath, String binaryPath, byte[] afm, byte[] pfb, boolean cached) throws java.io.IOException {
        FontProgram fontProgram;
        String fontKey = null;
        if (cached) {
            if (metricsPath != null) {
                fontKey = metricsPath;
            } else {
                fontKey = Integer.toString(ArrayUtil.hashCode(afm));
            }
            fontProgram = FontCache.getFont(fontKey);
            if (fontProgram != null) {
                return fontProgram;
            }
        }

        fontProgram = new Type1Font(metricsPath, binaryPath, afm, pfb);
        return cached ? FontCache.saveFont(fontProgram, fontKey) : fontProgram;
    }

}
