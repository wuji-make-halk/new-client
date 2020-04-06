package com.weimingfj.utils;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.weimingfj.common.utils.Environment;

public class ImageUtil {
	protected static Logger logger = Logger.getLogger(ImageUtil.class);
	/**
	 * 二进制存为图片
	 * @param image 图片的二进制
	 * @param path 物理路径
	 */
	public static void saveFile(byte[] image,String filePath){
		try {
			FileOutputStream fos = new FileOutputStream(filePath);
			 fos.write(image);    
             fos.close();  
		
		} catch (IOException e) {
			e.printStackTrace();
			logger.debug("存储文件失败");
		}  
	}
	
	public static String saveImage(byte[] image,String dir,String fileName){
		String path=getImagesPath();
		String filePath=getImagesPath();
		if(StringUtils.isNotEmpty(dir)){
			filePath+=File.separatorChar+dir;
			path+=File.separatorChar+dir;
		}
		path+=File.separatorChar+fileName;
		File d=new File(filePath);
		if(!d.exists()){
			d.mkdirs();
		}
		saveFile(image, path);
		return dir+File.separatorChar+fileName;
	}
	
	/**
	 * 获取项目根目录
	 * @return
	 */
	public static String getRootPath(){
		StringBuffer buffer=new StringBuffer();
		buffer.append("WEB-INF").append("/").append("classes");
		return new ImageUtil().getClass().getResource("/").getPath().replace(buffer.toString(), "").replace("%20", " ");
	}
	
	private static String getImagesPath(){
		String path=getRootPath()+java.io.File.separatorChar+Environment.IMAGE_SAVE_PATH;
		File dir=new File(path);
		if(!dir.exists()){
			dir.mkdirs();
		}
		return path;
	}
	
	/**
	 * 复制图片
	 * @param sourcePath
	 * @param destDir
	 * @param destFileName
	 * @return
	 */
	public static String copyImage(String sourcePath,String destDir,String destFileName){
		String source=getImagesPath()+File.separatorChar+sourcePath;
		String dest=getImagesPath();
		
		File sFile=new File(source);
		if(!sFile.exists()){
			return null;
		}
		
		if(StringUtils.isNotEmpty(destDir)){
			dest+=File.separatorChar+destDir;
		}
		File dFile=new File(dest);
		if(!dFile.exists()){
			dFile.mkdirs();
		}
		dest+=File.separatorChar+destFileName;
		dFile=new File(dest);
		FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;
        try {
            fi = new FileInputStream(sFile);
            fo = new FileOutputStream(dFile);
            in = fi.getChannel();//得到对应的文件通道
            out = fo.getChannel();//得到对应的文件通道
            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                fi.close();
                in.close();
                fo.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        sFile.delete();
        return destDir+"/"+destFileName;
	}
}
