/**
 * 
 */
package org.dutir.tool;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

import org.apache.tools.bzip2.CBZip2InputStream;
import org.apache.tools.bzip2.CBZip2OutputStream;
import org.dutir.corpus.DiskCorpus;
import org.dutir.corpus.Handler;
import org.dutir.corpus.InputSourceParser;
import org.dutir.util.LogFormatter;
import org.dutir.util.Streams;
import org.dutir.util.stream.StreamGenerator;
import org.xml.sax.InputSource;


/**
 * 
 * @author yezheng
 *
 */
public class FileSplit {

	public static Logger log = LogFormatter.getLogger("org.dutir.tool.FileSplit"); 
	
	static int C = 1;
	public static void splitFirstXM(File source, File des, int mb){
//		StreamGenerator.getFileOIS(path, mbsize)
	}
	
	/**
	 * Extract x M data from a large file
	 * @param source
	 * @param des
	 * @param mb
	 */
	public static void splitFirstXM(String source, String des, int mb){
		C = mb;
//		log.info("begin split");
//		InputStream ois = StreamGenerator.getFIS(source, 10);
//		OutputStream oos = StreamGenerator.getFOS(des, 10);
//		byte[] bbuf = new byte[1024];
//		for(int i =0; i < mb*1000; i++){
//			try {
//				int len = ois.read(bbuf);
//				oos.write(bbuf, 0, len);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//		}
//		Streams.closeInputStream(ois);
//		Streams.closeOutputStream(oos);
//		log.info("split finished");     
		FileParser parser = new FileParser();
		FileSplitHandle dbindexer = new FileSplitHandle();
		parser.setHandler(dbindexer);

		try {
			DiskCorpus<FileSplitHandle> corpus = 
				new DiskCorpus<FileSplitHandle>(parser, new File(source));
			corpus.visitCorpus(dbindexer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void splitFirstXM(String source, int mb){
		String des = source + ".splited";
		splitFirstXM(source, des, mb);
	}
	
	
	static class FileParser <H extends Handler>
    extends InputSourceParser<H>{

		@Override
		public void parse(InputSource in) throws IOException {
			// TODO Auto-generated method stub
			((FileSplitHandle)getHandler()).handle(in);
		}
		
//		public void parse(InputSource in, )throws IOException {
//			// TODO Auto-generated method stub
//			((FileSplitHandle)getHandler()).handle(in);
//		}
	}
	
	static class FileSplitHandle implements Handler{
		
		public void handle(InputSource in){
			log.info("begin split");
			
			InputStream ois = new BufferedInputStream(in.getByteStream());
			
			
			String path = "";
			if(in.getPublicId()==null){
				path = in.getSystemId();
			}else{
				String temp = in.getSystemId();
				int pos = temp.lastIndexOf('/');
				path = temp.substring(0, pos+1) + in.getPublicId();
			}
			
//			OutputStream oos = StreamGenerator.getFOS(path +".splited", C);
			OutputStream oos = StreamGenerator.getFOS("xml" +".splited", C);
			byte[] bbuf = new byte[1024];
			
			for(int i =0; i < C*1000; i++){
				try {
					int len = ois.read(bbuf);
					oos.write(bbuf, 0, len);
					if(len <1024)
						break;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.exit(1);
				}
				
			}
			Streams.closeOutputStream(oos);
			log.info("split finished");     
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
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{

		String path = "/home/yezheng/corpus/TREC/chemistry2009/ar_size_100000_000001.txt";
		FileInputStream fis = new FileInputStream(path);
		
			
		
		splitFirstXM("/home/yezheng/corpus/TREC/chemistry2009/ar_size_100000_000001.txt", 3);
	}

}
