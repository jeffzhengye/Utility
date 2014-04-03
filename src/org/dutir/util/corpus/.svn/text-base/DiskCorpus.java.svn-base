/*
 * LingPipe v. 3.5
 * Copyright (C) 2003-2008 Alias-i
 *
 * This program is licensed under the Alias-i Royalty Free License
 * Version 1 WITHOUT ANY WARRANTY, without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the Alias-i
 * Royalty Free License Version 1 for more details.
 * 
 * You should have received a copy of the Alias-i Royalty Free License
 * Version 1 along with this program; if not, visit
 * http://alias-i.com/lingpipe/licenses/lingpipe-license-1.txt or contact
 * Alias-i, Inc. at 181 North 11th Street, Suite 401, Brooklyn, NY 11211,
 * +1 (718) 290-9170.
 */

package org.dutir.util.corpus;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.tools.bzip2.CBZip2InputStream;
import org.apache.tools.bzip2.CBZip2OutputStream;
import org.dutir.lucene.parser.DocumentParser;
import org.dutir.lucene.util.ApplicationSetup;
import org.dutir.lucene.util.Files;

import com.ice.tar.TarEntry;
import com.ice.tar.TarInputStream;

/**
 * A <code>DiskCorpus</code> reads data from a specified directory 
 * or a list of directory from a file containing directory path per line.
 * and then use a specified parser to process the data.
 * 
 * @author Bob Carpenter, yezheng
 */

public class DiskCorpus<P extends DocumentParser> extends Corpus<P> {
	/**
	 * true indicates that the input File is corpus, false indicates that it is
	 * a file that contains paths of corpus
	 */
	private boolean CorpusTag = true;
	private final P mParser;
	private final File mTrainDir;

	private String mCharEncoding = "utf8";

	// revised
	private File specifiedFile = null;

	/**
	 * Construct a corpus from the specified parser and training and test
	 * directories. If either directory is <code>null</code>, the corresponding
	 * <code>visit</code> method will not produce any events.
	 * 
	 * @param parser
	 *            Parser for the data.
	 * @param trainDir
	 *            Directory of training data.
	 */
	public DiskCorpus(P parser, File trainDir) {
		mParser = parser;
		mTrainDir = trainDir;
		CorpusTag = Boolean.parseBoolean(ApplicationSetup.getProperty(
				"DisCorpus.CorpusTag", "true"));
	}

	public DiskCorpus(P parser, String trainDir) {
		File file = new File(trainDir);
		mParser = parser;
		mTrainDir = file;
		CorpusTag = Boolean.parseBoolean(ApplicationSetup.getProperty(
				"DisCorpus.CorpusTag", "true"));
	}

	public void visitCorpus() {
		try {
			if (CorpusTag) {
				System.out.println("indexing Directory");
				visit(mParser, mTrainDir);
			} else {
				System.out.println("indexing Corpus from a file, per lineï¼?"
						+ mTrainDir.getAbsoluteFile());
				String lines[] = org.dutir.util.Files.readLinesFromFile(
						mTrainDir, this.getCharEncoding());
				for (int i = 0; i < lines.length; i++) {
					if (lines[i].length() < 2 || lines[i].startsWith("#")) {
						continue;
					}
					visit(mParser, new File(lines[i]));
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}
	}

	/**
	 * Sets the character encoding for this corpus. If there is no character
	 * encoding set, the parser will determine the default character encoding.
	 * 
	 * @param encoding
	 *            Character encoding.
	 */
	public void setCharEncoding(String encoding) {
		mCharEncoding = encoding;
	}

	/**
	 * Returns the current character encoding, or <code>null</code> if none has
	 * been specified.
	 * 
	 * @return The current character encoding.
	 */
	public String getCharEncoding() {
		return mCharEncoding;
	}

	/**
	 * Returns the data parser for this corpus.
	 * 
	 * @return The data parser for this corpus.
	 */
	public P parser() {
		return mParser;
	}

	public void visitSpecified(P handler) throws IOException {
		visit(handler, specifiedFile);
	}

	private void visit(P parser, File file) throws IOException {
		// System.out.println(file.getAbsolutePath());
		if (file.isDirectory())
			visitDir(parser, file);
		else if (file.getName().endsWith(".tar.gz")) {
			visitTARGZ(parser, file);
		} else if (file.getName().endsWith(".gz"))
			visitGzip(parser, file);
		else if (file.getName().endsWith(".zip"))
			visitZip(parser, file);
		else if (file.getName().endsWith(".bz2")) {
			// System.out.println(file.getAbsolutePath());
			visitBZ2(parser, file);
		} else
			visitOrdinaryFile(parser, file);
	}

	private void visitDir(P parser, File dir) throws IOException {

		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; ++i)
			visit(parser, files[i]);
	}


	private void visitGzip(P parser, File gzipFile) throws IOException {

		FileInputStream fileIn = null;
		BufferedInputStream bufIn = null;
		GZIPInputStream gzipIn = null;
		try {
			fileIn = new FileInputStream(gzipFile);
			bufIn = new BufferedInputStream(fileIn);
			gzipIn = new GZIPInputStream(bufIn);
			configure(gzipIn, gzipFile);
		} finally {
			gzipIn.close();
			bufIn.close();
			fileIn.close();
		}

	}

	public static void testBZ2() throws Exception{
		org.apache.tools.bzip2.CBZip2OutputStream bz2o = 
			new CBZip2OutputStream(new FileOutputStream("test.bz"));
		bz2o.write("this is only a test".getBytes());
		bz2o.close();
		CBZip2InputStream bz2i = new CBZip2InputStream(new FileInputStream("E:/IR/Corpus/Wikipedia/zhwikisource-20081124-pages-articles.xml.bz2"));
		
		BufferedInputStream is = new BufferedInputStream(bz2i);
		byte[] buf = new byte[1024];
		int len =is.read(buf);
		System.out.println(new String(buf, 0, len));
	}
	
	private void visitBZ2(P parser, File bz2File) throws IOException {
		// CBZip2InputStream

		// visitOrdinaryFile(parser, bz2File);

		FileInputStream fileIn = null;
		BufferedInputStream bufIn = null;
		CBZip2InputStream bz2In = null;
		try {
			fileIn = new FileInputStream(bz2File);
			bufIn = new BufferedInputStream(fileIn);
			bz2In = new CBZip2InputStream(bufIn);
			configure(bz2In, bz2File);
		} finally {
			bz2In.close();
			bufIn.close();
			fileIn.close();
		}

	}

	private void visitTARGZ(P parser, File targzFile) throws IOException {
		FileInputStream fileIn = null;
		BufferedInputStream bufIn = null;
		GZIPInputStream gzipIn = null;
		TarInputStream taris = null;
		try {
			fileIn = new FileInputStream(targzFile);
			bufIn = new BufferedInputStream(fileIn);
			gzipIn = new GZIPInputStream(bufIn);
			taris = new TarInputStream(gzipIn);
			
			TarEntry entry = null;
			while ((entry = taris.getNextEntry()) != null) {
				if (entry.isDirectory())
					continue;
				configure(taris, entry.getFile());
			}
		} finally {
			taris.close();
			gzipIn.close();
			bufIn.close();
			fileIn.close();
		}
	}
	
	private void visitZip(P parser, File zipFile) throws IOException {
		FileInputStream fileIn = null;
		BufferedInputStream bufIn = null;
		ZipInputStream zipIn = null;
		try {
			fileIn = new FileInputStream(zipFile);
			bufIn = new BufferedInputStream(fileIn);
			zipIn = new ZipInputStream(bufIn);
			ZipEntry entry = null;
			while ((entry = zipIn.getNextEntry()) != null) {
				if (entry.isDirectory())
					continue;
				configure(zipIn, zipFile);
			}
		} finally {
			zipIn.close();
			bufIn.close();
			fileIn.close();
		}
	}

	private void visitOrdinaryFile(P parser, File file) throws IOException {
		// Note: here is revised
		// InputSource in = new InputSource(file.getCanonicalPath());
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(
				file), 1024 * 1024 * 2);
		configure(bis, file);
		// System.out.println(file.getCanonicalPath());
	}

	private void configure(InputStream inSource, File file) throws IOException {
		mParser.setup(inSource, file, this.mCharEncoding);
		mParser.setIndexWriter(this.writer);
		mParser.indexAll();
//		BufferedReader br = new BufferedReader(new InputStreamReader(inSource));
//		String line = null;
//		int count =0;
//		while((line = br.readLine())!= null){
//			if(count < 1){
//				System.out.println(line);
//				count ++;
//			}
//		}
	}
	
	public static void main(String args[]){
		String path = "/home/yezheng/corpus/TREC/chemistry2009/000000.tar.gz";
		DiskCorpus<DocumentParser> corpus = new DiskCorpus<DocumentParser>(null, path);
		corpus.visitCorpus();
	}
	
}
