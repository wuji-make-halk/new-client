package com.weimingfj.liner.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.weimingfj.api.qiniu.comm.Config;
import com.weimingfj.api.qiniu.comm.QiniuException;
import com.weimingfj.common.form.ResponseDataForm;
import com.weimingfj.common.utils.AliOssUploadUitls;

/**
 * 阿里巴巴OSS上传服务
 * @author htl
 * @时间  2016年11月23日下午3:22:17
 */
@Service("aliUoloadService")
public class AliUoloadService{
    
	private static final String ACCESSKEYID = "LTAI4Fm4KDoS9DAZNW7soYJ3";
	private static final String ACCESSKEYSECRET = "3fPdAha2Wdrdn0E7OvdOkPyDpUIHax";
	private static final String BUCKETNAME = "kdjp";
	
    /**
     * 上传文件
     * 2016年11月23日下午2:53:59
     */
    public ResponseDataForm uploadFile(String imgname, File img) throws QiniuException {
        ResponseDataForm rdf = new ResponseDataForm();
        String uuid = UUID.randomUUID().toString();
        String fileType = "";
        if (imgname.lastIndexOf(".") > -1) {
            fileType = imgname.substring(imgname.lastIndexOf(".") + 1);
        }
        
        String expectKey="open"+"_"+uuid + "." + fileType;//七牛上图片文件名
        
        AliOssUploadUitls auu=new AliOssUploadUitls(ACCESSKEYID, ACCESSKEYSECRET, BUCKETNAME, Config.IMAGE_URL);
        boolean status = auu.upload(File2byte(img), expectKey);
        
        if(status){
              rdf.setResult(ResponseDataForm.SUCCESS);
              rdf.setResultInfo("图片上传成功");
              rdf.setResultObj(Config.IMAGE_URL+"/"+expectKey);
              return rdf;
        }else{
            rdf.setResult(ResponseDataForm.FAILURE);
            rdf.setResultInfo("图片上传失败");
            return rdf;
        }
    }

    /**
     * base64上传文件
     * 2016年11月23日下午3:00:16
     */
    public ResponseDataForm uploadBase64(String imgname, String img) throws QiniuException {
        ResponseDataForm rdf = new ResponseDataForm();
        if (img.contains(",")) {
            img = img.substring(img.indexOf(",") + 1);
        }
        
        String uuid = UUID.randomUUID().toString();
        String fileType = "";
        if (imgname.lastIndexOf(".") > -1) {
            fileType = imgname.substring(imgname.lastIndexOf(".") + 1);
        }
        
        String expectKey="open"+"_"+uuid + "." + fileType;//七牛上图片文件名
        AliOssUploadUitls auu=new AliOssUploadUitls(ACCESSKEYID, ACCESSKEYSECRET, BUCKETNAME, Config.IMAGE_URL);
        boolean status;
		try {
			status = auu.upload(convertBase64ToBytes(img), expectKey);
		} catch (IOException e) {
			rdf.setResult(ResponseDataForm.FAILURE);
            rdf.setResultInfo("图片上传失败");
            return rdf;
		}
        
        if(status){
              rdf.setResult(ResponseDataForm.SUCCESS);
              rdf.setResultInfo("图片上传成功");
              rdf.setResultObj(Config.IMAGE_URL+"/"+expectKey);
              return rdf;
        }else{
            rdf.setResult(ResponseDataForm.FAILURE);
            rdf.setResultInfo("图片上传失败");
            return rdf;
        }
    }
    
    public static byte[] File2byte(File file)  
    {  
        byte[] buffer = new byte[Config.BLOCK_SIZE];  
        try  
        {  
            FileInputStream fis = new FileInputStream(file); 
            @SuppressWarnings("unused")
			int n;  
            while ((n = fis.read(buffer)) != -1)  
            {  
            }  
            fis.close();  
        }  
        catch (Exception e)  
        {  
            e.printStackTrace();  
        }
        return buffer;  
    }  
    
    
    public static byte[] convertBase64ToBytes(String base64Str) throws IOException {
        if (base64Str.contains("base64,")) {
            base64Str = base64Str.split("base64,")[1];
        }
//        System.out.println(base64Str);
        byte[] bytes = null;
        sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
        bytes = decoder.decodeBuffer(base64Str);
        return bytes;
    }
}
