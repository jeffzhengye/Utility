import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import org.dutir.util.Streams;

/**
 * 
 */

/**
 * @author zheng
 *
 */
public class test {

	public static void binarySearch(){
		String[] arr = new String[]{"a", "b", "c", "g", "z"};
		int pos = java.util.Arrays.binarySearch(arr, "正");
		System.out.println("" + pos);
	}
	
	public static void testFileExist(){
		String path = "fileExist.txt"; 
		File file = new File(path);
		while (true){
			if(file.exists()){
				System.out.println("success");
				break;
			}
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void testLock(){
		String file = "test.txt";
        FileOutputStream fileOut = null;
        FileLock filelock = null;
        try {
            fileOut = new FileOutputStream(file);
            FileChannel channel = fileOut.getChannel();
            filelock = channel.tryLock();
            while(true){
            	if(filelock != null){
            		break;
            	}else{
            		Thread.sleep(200);
            		filelock = channel.tryLock();
            	}
            }
            
            
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  finally {
		    if (filelock != null && filelock.isValid())
				try {
					filelock.release();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            Streams.closeOutputStream(fileOut);
        }
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		binarySearch();
		testFileExist();
	}

}
