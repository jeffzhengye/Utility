package sample;

import java.io.File;

import org.dutir.util.CharOper;
import org.dutir.util.Strings;

import com.google.api.translate.Language;
import com.google.api.translate.Translate;

public class GoogleTrans {

	public static void translateDir(File input, File output, String encoding) {
		// Set the HTTP referrer to your website address.
		Translate.setHttpReferrer("http://blog.so8848.com");
		File files[] = input.listFiles();
		if (!output.exists())
			output.mkdirs();
		for (int i = 0, n = files.length; i < n; i++) {
			File file = files[i];
			if (file.isDirectory()) {
				translateDir(file, new File(output, file.getName()), encoding);
			} else {
				String content = null;
				File outfile = new File(output, file.getName());
				if(outfile.exists())continue;
				try {
					content = org.dutir.util.Files.readFromFile(file, encoding);
					StringBuffer translatedText = new StringBuffer(2048);
					int len = content.length();
					while (len >= 5000) {
						int pos = 5000;
						for(;pos>4500; pos--){
							if(Strings.isPunctuation(content.charAt(pos))){
								translatedText.append(Translate.execute(content.substring(0, pos),
										Language.ENGLISH, Language.CHINESE_SIMPLIFIED));
								content = content.substring(pos);
								len = content.length();
								break;
							}
						}
						if(pos <= 4500){
							System.out.print(content.substring(pos, 5000));
							System.exit(0);
						}
					}
					if(len < 5000)
					translatedText.append(Translate.execute(content,
							Language.ENGLISH, Language.CHINESE_SIMPLIFIED));
					org.dutir.util.Files.writeStringToFile(translatedText.toString(),
							outfile, encoding);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(content.length());
					System.exit(0);
				}
			}
		}
	}

	public static void main(String[] args) throws Exception {
		translateDir(
				new File(
						"/win/workspace/OpenSource/LingPipe/sentiment/txt_sentoken"),
				new File(
						"/win/workspace/OpenSource/LingPipe/sentiment/txt_sentoken_trans"),
				"utf8");
	}

}
