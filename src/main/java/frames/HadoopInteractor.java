package frames;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class HadoopInteractor {
	public Configuration conf;
    public FileSystem fileSystem;
    public static boolean status = false;
    public String FILE_LOCAL_DATA_DIR = "C:/Users/pc/Desktop/MapReduce_GUI/src/main/java/data/input.txt";
    public String FILE_LOCAL_OUTPUT_DIR = "C:/Users/pc/Desktop/MapReduce_GUI/src/main/java/data/output.txt";
    
    public HadoopInteractor() throws IOException {
        conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://localhost:9000/");
        fileSystem = FileSystem.get(conf);
        
        if (createFile("testconnect")){
        	System.out.println("Ket noi thanh cong toi hadoop !");
        	deleteFile("testconnect");
        	status = true;
        }
        if (status == true) {
        	Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    closeConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));
        }
        
    }
    
    public boolean createFile(String fileName) {
        try {
        	Path path = new Path("/" + fileName);
            if (fileSystem.exists(path)) {
            	return false;
            }else {
				fileSystem.create(path).close();
				return true;
			}
            
        } catch (IOException e) {
            System.out.println("Ket noi toi Hadoop that bai !");
        }
        return false;
    }
    
    public boolean deleteFile(String filePath){
        try {
            Path pathToDelete = new Path("/" + filePath);
            if (fileSystem.exists(pathToDelete)) {
            	return fileSystem.delete(pathToDelete, true);
            } else {
                return false;
            }
        } catch (IOException e) {
            System.out.println("Ket noi toi Hadoop that bai !");
        }
        return false;
    }
    
    public boolean putFile(String src, String dst){
    	Path srcPath = new Path(src);
    	Path dstPath = new Path("/" + dst);
    	try {
			if (fileSystem.exists(dstPath)){
				fileSystem.copyFromLocalFile(srcPath, dstPath);
			}else {
				createFile(dst);
				fileSystem.copyFromLocalFile(srcPath, dstPath);
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	return false;
    }
    
    public void close() {
        try {
            if (fileSystem != null) {
                fileSystem.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean createAndPutFile(String content, String fileName, String dst) {
        String localFilePath = FILE_LOCAL_DATA_DIR;
        try {
            
            BufferedWriter writer = new BufferedWriter(new FileWriter(localFilePath));
            writer.write(content);
            writer.close();
            
            File localFile = new File(localFilePath);
            if (localFile.exists()) {
            	Path temPath = new Path("/" + dst);
                if (fileSystem.exists(temPath)){
                	deleteFile(dst);
                	return putFile(localFilePath, dst + "/" + fileName);
                }else {
                	return putFile(localFilePath, dst + "/" + fileName);
                }
            } else {
                System.out.println("Tệp không tồn tại sau khi ghi!");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

	public void closeConnection() throws IOException {
	    if (fileSystem != null) {
	        fileSystem.close();
	        System.out.println("Da dong ket noi voi Hadoop !");
	    }
	}
}
