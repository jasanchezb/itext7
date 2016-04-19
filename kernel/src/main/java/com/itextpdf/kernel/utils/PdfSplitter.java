/*
    $Id$

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
package com.itextpdf.kernel.utils;

import com.itextpdf.kernel.PdfException;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfOutline;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PdfSplitter {

    private PdfDocument pdfDocument;

    /**
     * Creates a new instance of PdfSplitter class.
     *
     * @param pdfDocument the document to be split.
     */
    public PdfSplitter(PdfDocument pdfDocument) {
        if (pdfDocument.getWriter() != null) {
            throw new PdfException(PdfException.CannotSplitDocumentThatIsBeingWritten);
        }
        this.pdfDocument = pdfDocument;
    }

    /**
     * Splits the document basing on the given size.
     *
     * @param size <strog>Preferred</strog> size for splitting.
     * @return The documents which the source document was split into.
     *         Be warned that these documents are not closed.
     */
    public List<PdfDocument> splitBySize(long size) {
        List<PageRange> splitRanges = new ArrayList<>();
        int currentPage = 1;
        int numOfPages = pdfDocument.getNumberOfPages();

        while (currentPage <= numOfPages) {
            PageRange nextRange = getNextRange(currentPage, numOfPages, size);
            splitRanges.add(nextRange);
            currentPage = nextRange.getAllPages().last() + 1;
        }

        return extractPageRanges(splitRanges);
    }

    /**
     * Splits the document by page numbers.
     *
     * @param pageNumbers   the numbers of pages from which another document is to be started.
     *                      If the first element is not 1, then 1 is implied (i.e. the first split document will start from page 1 in any case).
     * @param documentReady the event listener which is called when another document is ready.
     *                      You can close this document in this listener, for instance.
     * @throws PdfException
     */
    public void splitByPageNumbers(List<Integer> pageNumbers, IDocumentReadyListener documentReady) {
        int currentPageNumber = 1;

        for (int ind = 0; ind <= pageNumbers.size(); ind++) {
            int nextPageNumber = ind == pageNumbers.size() ? pdfDocument.getNumberOfPages() + 1 : pageNumbers.get(ind);
            if (ind == 0 && nextPageNumber == 1)
                continue;

            PageRange currentPageRange = new PageRange().addPageSequence(currentPageNumber, nextPageNumber - 1);
            PdfDocument currentDocument = createPdfDocument(currentPageRange);
            pdfDocument.copyPagesTo(currentPageNumber, nextPageNumber - 1, currentDocument);
            documentReady.documentReady(currentDocument, currentPageRange);

            currentPageNumber = nextPageNumber;
        }
    }

    /**
     * Splits the document by page numbers.
     *
     * @param pageNumbers the numbers of pages from which another document is to be started.
     *                    If the first element is not 1, then 1 is implied (i.e. the first split document will start from page 1 in any case).
     * @return the list of resultant documents. By warned that they are not closed.
     * @throws PdfException
     */
    public List<PdfDocument> splitByPageNumbers(List<Integer> pageNumbers) {
        final List<PdfDocument> splitDocuments = new ArrayList<>();

        splitByPageNumbers(pageNumbers, new IDocumentReadyListener() {
            @Override
            public void documentReady(PdfDocument pdfDocument, PageRange pageRange) {
                splitDocuments.add(pdfDocument);
            }
        });

        return splitDocuments;
    }

    /**
     * Splits a document into smaller documents with no more than @pageCount pages each.
     *
     * @param pageCount     the biggest possible number of pages in a split document.
     * @param documentReady the event listener which is called when another document is ready.
     *                      You can close this document in this listener, for instance.
     * @throws PdfException
     */
    public void splitByPageCount(int pageCount, IDocumentReadyListener documentReady) {
        for (int startPage = 1; startPage <= pdfDocument.getNumberOfPages(); startPage += pageCount) {
            int endPage = Math.min(startPage + pageCount - 1, pdfDocument.getNumberOfPages());

            PageRange currentPageRange = new PageRange().addPageSequence(startPage, endPage);
            PdfDocument currentDocument = createPdfDocument(currentPageRange);
            pdfDocument.copyPagesTo(startPage, endPage, currentDocument);
            documentReady.documentReady(currentDocument, currentPageRange);
        }
    }

    /**
     * Splits a document into smaller documents with no more than @pageCount pages each.
     *
     * @param pageCount the biggest possible number of pages in a split document.
     * @return the list of resultant documents. By warned that they are not closed.
     * @throws PdfException
     */
    public List<PdfDocument> splitByPageCount(int pageCount) {
        final List<PdfDocument> splitDocuments = new ArrayList<>();

        splitByPageCount(pageCount, new IDocumentReadyListener() {
            @Override
            public void documentReady(PdfDocument pdfDocument, PageRange pageRange) {
                splitDocuments.add(pdfDocument);
            }
        });

        return splitDocuments;
    }

    /**
     * Extracts the specified page ranges from a document.
     *
     * @param pageRanges the list of page ranges for each of the resultant document.
     * @return the list of the resultant documents for each of the specified page range.
     * Be warned that these documents are not closed.
     * @throws PdfException
     */
    public List<PdfDocument> extractPageRanges(List<PageRange> pageRanges) {
        List<PdfDocument> splitDocuments = new ArrayList<>();

        for (PageRange currentPageRange : pageRanges) {
            PdfDocument currentPdfDocument = createPdfDocument(currentPageRange);
            splitDocuments.add(currentPdfDocument);
            pdfDocument.copyPagesTo(currentPageRange.getAllPages(), currentPdfDocument);
        }

        return splitDocuments;
    }

    /**
     * Extracts the specified page ranges from a document.
     *
     * @param pageRange the page range to be extracted from the document.
     * @return the resultant document containing the pages specified by the provided page range.
     * Be warned that this document is not closed.
     * @throws PdfException
     */
    public PdfDocument extractPageRange(PageRange pageRange) {
        return extractPageRanges(Collections.singletonList(pageRange)).get(0);
    }

    public PdfDocument getPdfDocument() {
        return pdfDocument;
    }

    /**
     * This method is called when another split document is to be created.
     * You can override this method and return your own {@see PdfWriter} depending on your needs.
     *
     * @param documentPageRange the page range of the original document to be included in the document being created now.
     * @return the PdfWriter instance for the document which is being created.
     */
    protected PdfWriter getNextPdfWriter(PageRange documentPageRange) {
        return new PdfWriter(new ByteArrayOutputStream());
    }

    private PdfDocument createPdfDocument(PageRange currentPageRange) {
        PdfDocument newDocument = new PdfDocument(getNextPdfWriter(currentPageRange));
        if (pdfDocument.isTagged())
            newDocument.setTagged();
        if (pdfDocument.getCatalog().isOutlineMode())
            newDocument.initializeOutlines();
        return newDocument;
    }

    public interface IDocumentReadyListener {
        void documentReady(PdfDocument pdfDocument, PageRange pageRange);
    }

    public static class PageRange {
        private List<Integer> sequenceStarts = new ArrayList<>();
        private List<Integer> sequenceEnds = new ArrayList<>();

        public PageRange() {
        }

        /**
         * You can call specify the page range in a string form, for example: "1-12, 15, 45-66".
         *
         * @param pageRange the page range.
         */
        public PageRange(String pageRange) {
            pageRange = pageRange.replaceAll("\\s+", "");
            Pattern sequencePattern = Pattern.compile("(\\d+)-(\\d+)");
            Pattern singlePagePattern = Pattern.compile("(\\d+)");
            for (String pageRangePart : pageRange.split(",")) {
                Matcher matcher;
                if ((matcher = sequencePattern.matcher(pageRangePart)).matches()) {
                    sequenceStarts.add(Integer.parseInt(matcher.group(1)));
                    sequenceEnds.add(Integer.parseInt(matcher.group(2)));
                } else if ((matcher = singlePagePattern.matcher(pageRangePart)).matches()) {
                    int pageNumber = Integer.parseInt(matcher.group(1));
                    sequenceStarts.add(pageNumber);
                    sequenceEnds.add(pageNumber);
                }
            }
        }

        public PageRange addPageSequence(int startPageNumber, int endPageNumber) {
            sequenceStarts.add(startPageNumber);
            sequenceEnds.add(endPageNumber);
            return this;
        }

        public PageRange addSinglePage(int pageNumber) {
            sequenceStarts.add(pageNumber);
            sequenceEnds.add(pageNumber);
            return this;
        }

        public TreeSet<Integer> getAllPages() {
            TreeSet<Integer> allPages = new TreeSet<>();
            for (int ind = 0; ind < sequenceStarts.size(); ind++) {
                for (int pageInRange = sequenceStarts.get(ind); pageInRange <= sequenceEnds.get(ind); pageInRange++) {
                    allPages.add(pageInRange);
                }
            }
            return allPages;
        }

        public boolean isPageInRange(int pageNumber) {
            for (int ind = 0; ind < sequenceStarts.size(); ind++) {
                if (sequenceStarts.get(ind) <= pageNumber && pageNumber <= sequenceEnds.get(ind))
                    return true;
            }
            return false;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof PageRange))
                return false;

            PageRange other = (PageRange) obj;
            return sequenceStarts.equals(other.sequenceStarts) && sequenceEnds.equals(other.sequenceEnds);
        }

        @Override
        public int hashCode() {
            return sequenceStarts.hashCode() * 31 + sequenceEnds.hashCode();
        }
    }

    /**
     * Split a document by outline title (bookmark name), find outline by name
     * and places the entire hierarchy in a separate document ( outlines and pages ) .
     *
     * @param outlineTitles list of outline titles .
     * @throws PdfException
     */
    public List<PdfDocument> splitByOutlines(List<String> outlineTitles) {
        if (outlineTitles == null || outlineTitles.isEmpty()) {
            return Collections.EMPTY_LIST;
        }

        List<PdfDocument> documentList = new ArrayList<>(outlineTitles.size());
        for (String title : outlineTitles) {
            PdfDocument document = splitByOutline(title);
            if (document != null) {
                documentList.add(document);
            }
        }

        return documentList;
    }

    private PdfDocument splitByOutline(String outlineTitle) {

        int startPage = -1;
        int endPage = -1;

        PdfDocument toDocument = createPdfDocument(null);

        int size = pdfDocument.getNumberOfPages();
        for (int i = 1; i <= size; i++) {
            PdfPage pdfPage = pdfDocument.getPage(i);
            List<PdfOutline> outlineList = pdfPage.getOutlines(false);
            if (outlineList != null) {
                for (PdfOutline pdfOutline : outlineList) {
                    if (pdfOutline.getTitle().equals(outlineTitle)) {
                        startPage = pdfDocument.getPageNumber(pdfPage);
                        PdfOutline nextOutLine = getAbsoluteTreeNextOutline(pdfOutline);
                        if (nextOutLine != null) {
                            endPage = pdfDocument.getPageNumber(getPageByOutline(i, nextOutLine)) - 1;
                        } else {
                            endPage = size;
                        }
                        // fix case: if two sequential bookmark point to one page
                        if (startPage - endPage == 1) {
                            endPage = startPage;
                        }
                        break;
                    }
                }
            }
        }

        if (startPage == -1 || endPage == -1) {
            return null;
        }
        pdfDocument.copyPagesTo(startPage, endPage, toDocument);

        return toDocument;
    }

    private PdfPage getPageByOutline(int fromPage, PdfOutline outline) {
        int size = pdfDocument.getNumberOfPages();
        for (int i = fromPage; i <= size; i++) {
            PdfPage pdfPage = pdfDocument.getPage(i);
            List<PdfOutline> outlineList = pdfPage.getOutlines(false);
            if (outlineList != null) {
                for (PdfOutline pdfOutline : outlineList) {
                    if (pdfOutline.equals(outline)) {
                        return pdfPage;
                    }
                }
            }
        }
        return null;
    }

    /**
     * the next element in the entire hierarchy
     *
     * @param outline *
     * @throws PdfException
     */
    private PdfOutline getAbsoluteTreeNextOutline(PdfOutline outline) {

        PdfObject nextPdfObject = outline.getContent().get(PdfName.Next);
        PdfOutline nextPdfOutline = null;

        if (outline.getParent() != null && nextPdfObject != null) {
            for (PdfOutline pdfOutline : outline.getParent().getAllChildren()) {
                if (pdfOutline.getContent().getIndirectReference().equals(nextPdfObject.getIndirectReference())) {
                    nextPdfOutline = pdfOutline;
                    break;
                }
            }
        }
        if (nextPdfOutline == null && outline.getParent() != null) {
            nextPdfOutline = getAbsoluteTreeNextOutline(outline.getParent());
        }
        return nextPdfOutline;
    }

    private PageRange getNextRange(int startPage, int endPage, long size) {
        PdfResourceCounter counter = new PdfResourceCounter(pdfDocument.getTrailer());
        Map<Integer, PdfObject> resources = counter.getResources();
        long lengthWithoutXref = counter.getLength(null); // initialize with trailer length
        int currentPage = startPage;
        boolean oversized = false;

        do {
            PdfPage page = pdfDocument.getPage(currentPage++);
            counter = new PdfResourceCounter(page.getPdfObject());
            lengthWithoutXref += counter.getLength(resources);
            resources.putAll(counter.getResources());

            if (lengthWithoutXref + xrefLength(resources.size()) > size) {
                oversized = true;
            }
        } while (currentPage <= endPage && !oversized);

        // true if at least the first page to be copied didn't cause the oversize
        if (oversized && (currentPage - 1) != startPage) {
            // we shouldn't copy previous page because it caused
            // the oversize and it isn't the first page to be copied
            --currentPage;
        }

        return new PageRange().addPageSequence(startPage, currentPage - 1);
    }

    private long xrefLength(int size) {
        return 20L * (size + 1);
    }
}
