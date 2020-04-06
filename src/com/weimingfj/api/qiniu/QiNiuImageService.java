package com.weimingfj.api.qiniu;

import java.io.File;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.weimingfj.api.qiniu.comm.Config;
import com.weimingfj.api.qiniu.comm.QiniuException;
import com.weimingfj.api.qiniu.http.Response;
import com.weimingfj.api.qiniu.storage.UploadManager;
import com.weimingfj.api.qiniu.utils.Base64;
import com.weimingfj.api.qiniu.utils.ImageToken;
import com.weimingfj.api.qiniu.utils.StringMap;
import com.weimingfj.common.form.RequestDataForm;
import com.weimingfj.common.form.ResponseDataForm;
import com.weimingfj.common.service.IService;
import com.weimingfj.common.web.httpobjects.HttpRequestObject;

@Service("qiNiuImageService")
public class QiNiuImageService implements IService {
    
    private UploadManager uploadManager =new UploadManager();
    

	@Override
	@Transactional
	public ResponseDataForm service(RequestDataForm requestDataForm) throws Exception {
		ResponseDataForm rdf = new ResponseDataForm();
		
		String imgtype = requestDataForm.getString("imgtype");
		String fileName = requestDataForm.getString("imgname");
		if(StringUtils.isEmpty(imgtype) || StringUtils.isEmpty(fileName)) {
		    rdf.setResult(ResponseDataForm.FAULAIE);
            rdf.setResultInfo("缺少必要的图片上传参数！");
            return rdf;
		}
		
		byte[] fileByte = null;
		if ("file".equals(imgtype)) {
	        HttpRequestObject httpRequestObject = requestDataForm.get("img");
	        fileByte = httpRequestObject.getValue();
	        if (StringUtils.isEmpty(fileName)) {
	            fileName = httpRequestObject.getFilename();
	        }
		} else if ("base64".equals(imgtype)) {
		    String imgStr = requestDataForm.getString("image");
		    if (imgStr.contains(",")) {
		        imgStr = imgStr.substring(imgStr.indexOf(",") + 1);
		    }
		    fileByte = Base64.decode(imgStr, Base64.DEFAULT);
		} else {
		    rdf.setResult(ResponseDataForm.FAULAIE);
            rdf.setResultInfo("无法处理类型为【" + imgtype + "】的图片！");
            return rdf;
		}
		
		if (fileByte == null || fileByte.length == 0) {
			rdf.setResult(ResponseDataForm.FAULAIE);
			rdf.setResultInfo("上传的图片不能为空！");
			return rdf;
		}
		
		String uuid = UUID.randomUUID().toString();
		String fileType = "";
        if (fileName.lastIndexOf(".") > -1) {
            fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        
        String expectKey="open"+"_"+uuid + "." + fileType;//七牛上图片文件名
        StringMap imgParam = new StringMap().put("x:foo", "foo_val");
        String token = ImageToken.testAuth.uploadToken(ImageToken.bucket, expectKey);
        Response r=uploadManager.put(fileByte, expectKey, token, imgParam, null, true);
        String status=String.valueOf(r.statusCode);
        
        if("200".equals(status)){
              rdf.setResult(ResponseDataForm.SESSFUL);
              rdf.setResultInfo("图片上传成功");
              rdf.setResultObj(Config.IMAGE_URL+expectKey);
              return rdf;
        }else{
            rdf.setResult(ResponseDataForm.FAULAIE);
            rdf.setResultInfo("图片上传失败");
            return rdf;
        }
        /*String path = ImageUtil.saveImage(fileByte, "tpxt", expectKey);
        rdf.setResult(ResponseDataForm.SESSFUL);
        rdf.setResultObj(path);
        return rdf;*/
	}
	
	/**
     * 七牛图片上传(BASE64)
     * @author lihw
     * @created 2016年7月25日 下午3:27:40
     *
     * @param imgname
     * @param img
     * @return
     * @throws QiniuException
     */
    public ResponseDataForm uploadBase64(String imgname, String img) throws QiniuException {
        ResponseDataForm rdf = new ResponseDataForm();
        if (img.contains(",")) {
            img = img.substring(img.indexOf(",") + 1);
        }
        byte[] fileByte = Base64.decode(img, Base64.DEFAULT);
        
        String uuid = UUID.randomUUID().toString();
        String fileType = "";
        if (imgname.lastIndexOf(".") > -1) {
            fileType = imgname.substring(imgname.lastIndexOf(".") + 1);
        }
        
        String expectKey="open"+"_"+uuid + "." + fileType;//七牛上图片文件名
        StringMap imgParam = new StringMap().put("x:foo", "foo_val");
        String token = ImageToken.testAuth.uploadToken(ImageToken.bucket, expectKey);
        Response r=uploadManager.put(fileByte, expectKey, token, imgParam, null, true);
        String status=String.valueOf(r.statusCode);
        
        if("200".equals(status)){
              rdf.setResult(ResponseDataForm.SESSFUL);
              rdf.setResultInfo("图片上传成功");
              rdf.setResultObj(Config.IMAGE_URL+expectKey);
              return rdf;
        }else{
            rdf.setResult(ResponseDataForm.FAULAIE);
            rdf.setResultInfo("图片上传失败");
            return rdf;
        }
    }
    
    /**
     * 七牛图片上传(FILE)
     * @author lihw
     * @created 2016年7月25日 下午3:27:10
     *
     * @param imgname
     * @param img
     * @return
     * @throws QiniuException
     */
    public ResponseDataForm uploadFile(String imgname, File img) throws QiniuException {
        ResponseDataForm rdf = new ResponseDataForm();
        String uuid = UUID.randomUUID().toString();
        String fileType = "";
        if (imgname.lastIndexOf(".") > -1) {
            fileType = imgname.substring(imgname.lastIndexOf(".") + 1);
        }
        
        String expectKey="open"+"_"+uuid + "." + fileType;//七牛上图片文件名
        StringMap imgParam = new StringMap().put("x:foo", "foo_val");
        String token = ImageToken.testAuth.uploadToken(ImageToken.bucket, expectKey);
        Response r=uploadManager.put(img, expectKey, token, imgParam, null, true);
        String status=String.valueOf(r.statusCode);
        
        if("200".equals(status)){
              rdf.setResult(ResponseDataForm.SESSFUL);
              rdf.setResultInfo("图片上传成功");
              rdf.setResultObj(Config.IMAGE_URL+expectKey);
              return rdf;
        }else{
            rdf.setResult(ResponseDataForm.FAULAIE);
            rdf.setResultInfo("图片上传失败");
            return rdf;
        }
    }

}
