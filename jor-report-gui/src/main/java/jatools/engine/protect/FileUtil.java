package jatools.engine.protect;

import java.io.File;
import java.io.IOException;

public class FileUtil {
	static File tmpDir = new File("temp");
	
	

	public static File createTempFile(String pre, String ext) {
		if(!tmpDir.exists() )
			tmpDir.mkdirs() ;
		
		File tmp;
		try {
			tmp = File.createTempFile( pre,ext,tmpDir);
			tmp.deleteOnExit() ;
			return tmp;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}

}
