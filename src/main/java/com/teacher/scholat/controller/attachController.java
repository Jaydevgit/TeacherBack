package com.teacher.scholat.controller;

import com.alibaba.fastjson.JSONObject;
import com.teacher.scholat.MyApplication;
import com.teacher.scholat.util.CommonUtil;
import com.teacher.scholat.util.FTPUtil;
import com.teacher.scholat.util.IDUtils;
import com.teacher.scholat.util.WangEditor;
import com.teacher.scholat.util.constants.ErrorEnum;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

/*
 * 上传头像
 * */
@RestController
@RequestMapping("/attach")
public class attachController {
    @Value("${FTP.ADDRESS}")
    private String host;
    // 端口
    @Value("${FTP.PORT}")
    private int port;
    // ftp用户名
    @Value("${FTP.USERNAME}")
    private String userName;
    // ftp用户密码
    @Value("${FTP.PASSWORD}")
    private String passWord;
    // 文件在服务器端保存的主目录
    @Value("${FTP.AVATAR.BASEPATH}")
    private String baseAvatarPath;
    // 文件在服务器端保存的主目录
    @Value("${FTP.CERTIFICATE_FRONT.BASEPATH}")
    private String baseCertificateFrontPath;
    // 文件在服务器端保存的主目录
    @Value("${FTP.CERTIFICATE_BACK.BASEPATH}")
    private String baseCertificateBackPath;
    // 文件在服务器端保存的主目录
    @Value("${FTP.CERTIFICATE_WORKING.BASEPATH}")
    private String baseCertificateWorkingPath;
    // 文件在服务器端保存的主目录
    @Value("${FTP.CERTIFICATE_LOGO.BASEPATH}")
    private String baseCertificateLogoPath;
    // 文件在服务器端保存的主目录
    @Value("${FTP.UNIT_LOGO.BASEPATH}")
    private String unitLogoPath;
    // 文件在服务器端保存的主目录
    @Value("${FTP.BACKGROUND.BASEPATH}")
    private String backgroundPath;
    // 文件在服务器端保存的主目录
    @Value("${FTP.IMAGE.BASEPATH}")
    private String baseImagePath;
    // 访问图片时的基础url
    @Value("${IMAGE.BASE.URL}")
    private String baseUrl;
    // nginx服务器地址
    @Value("${NGINX.ADDRESS}")
    private String nginxHost;
    @Value("${NGINX.PORT}")
    private String nginxPort;
    @Value("${NGINX.IMAGE.PATH}")
    private String nginxImagePath;

    // 上传头像
    @RequestMapping("upload")
    public String upload(@RequestParam MultipartFile file) throws IllegalStateException, IOException {
        System.out.println("==================== 开始使用ftp上传文件");
        String newfileName = getFileName(file);
        String filePath = "";
        InputStream input = file.getInputStream();
        /*
                生成文件在服务器端存储的子目录
                比如: String filePath = new DateTime().toString("/yyyy/MM/dd");
                ftp://222.201.80.72/images/avatar/2019/06/12/文件名
         */
/*      String saveFilePath = "D:\\sansenLian\\project\\beta\\teacherHome\\teacherHome-back\\src\\main\\resources\\static\\avatar";
        String newFileName = UUID.randomUUID() + ".png";
        File newFile = new File(saveFilePath + '\\' + newFileName);
        // 将内存中的数据写入磁盘中
        file.transferTo(newFile);
        System.out.println(".....保存到磁盘中成功");*/

        // 调用FtpUtil工具类进行上传
        System.out.println("-----------------------ftp应用启动------------------------");
        FTPClient ftpClient = FTPUtil.connectFtpServer(host, port, userName, passWord, "gbk");
        System.out.println("FTP 连接是否成功：" + ftpClient.isConnected());
        System.out.println("FTP 连接是否有效：" + ftpClient.isAvailable());
        boolean result = FTPUtil.uploadFile(host, port, userName, passWord, baseAvatarPath, filePath, newfileName, input);
        System.out.println("-----------------------ftp应用关闭------------------------");
        System.out.println("========================== 使用ftp上传文件结束");
        if (result) {
            return newfileName;
        } else {
            return "false";
        }

    }
    // 删除上传头像
    @GetMapping("delete/{name}")
    public JSONObject delete(@PathVariable("name") String name) throws IllegalStateException, IOException {
        System.out.println("==================== 开始删除上传文件");
        String deleteFiles="/images/avatar/"+name;
        //删除测试数据
//        String host="47.106.132.95";
//        int port=21;
//        String userName="sansen_ftp";
//        String passWord="#scholat232";
        System.out.println("deleteFiles="+deleteFiles);
        // 调用FtpUtil工具类进行上传
        System.out.println("-----------------------ftp应用启动------------------------");
        FTPClient ftpClient = FTPUtil.connectFtpServer(host, port, userName, passWord, "gbk");
        System.out.println("FTP 连接是否成功：" + ftpClient.isConnected());
        System.out.println("FTP 连接是否有效：" + ftpClient.isAvailable());
        boolean deleteFlag=FTPUtil.deleteServerFiles(ftpClient, deleteFiles);
        System.out.println("-----------------------ftp应用关闭------------------------");
        System.out.println("========================== 使用ftp删除文件结束");
        if (deleteFlag) {
            System.out.println("========================== 使用ftp删除文件成功");
            return CommonUtil.successJson();
        } else {
            System.out.println("========================== 使用ftp删除文件失败,就是无该文件头像不报错");
            return CommonUtil.successJson();
        }

    }
    //删除测试代码
//    public static void main(String[] args) throws IOException {
//        attachController attachController = new attachController();
//        boolean delete = attachController.delete("1588668561359091.png");
//        System.out.println("delete="+delete);
//    }

    // 上传证明
    @RequestMapping("upload/certificate")
    public String uploadCertificate(@RequestParam MultipartFile file) throws IllegalStateException, IOException {
        System.out.println("==================== 成功进入申请界面上传证明文件功能");
        String oldFileName = file.getOriginalFilename(); //获取上传文件的原名
        System.out.println("旧文件名为: " + oldFileName);
        String newfileName = getFileName(file);
        String filePath = "";
        InputStream input = file.getInputStream();

        // 调用FtpUtil工具类进行上传
        System.out.println("-----------------------ftp应用启动------------------------");
        FTPClient ftpClient = FTPUtil.connectFtpServer(host, port, userName, passWord, "gbk");
        System.out.println("FTP 连接是否成功：" + ftpClient.isConnected());
        System.out.println("FTP 连接是否有效：" + ftpClient.isAvailable());
        // 判断上传的是：身份证，在职证明照片、Logo、背景
        boolean result = false;
        if(oldFileName.equals("front.png")){
            System.out.println("上传到: "+host+port+baseCertificateFrontPath+filePath+newfileName);
            result = FTPUtil.uploadFile(host, port, userName, passWord, baseCertificateFrontPath, filePath, newfileName, input);
        }else if(oldFileName.equals("back.png")){
            System.out.println("上传到: "+host+port+baseCertificateBackPath+filePath+newfileName);
            result = FTPUtil.uploadFile(host, port, userName, passWord, baseCertificateBackPath, filePath, newfileName, input);

        }else if(oldFileName.equals("working.png")){
            System.out.println("上传到: "+host+port+baseCertificateWorkingPath+filePath+newfileName);
            result = FTPUtil.uploadFile(host, port, userName, passWord, baseCertificateWorkingPath, filePath, newfileName, input);
        }else if(oldFileName.equals("logo.png")){
            System.out.println("上传到: "+host+port+baseCertificateLogoPath+filePath+newfileName);
            result = FTPUtil.uploadFile(host, port, userName, passWord, baseCertificateLogoPath, filePath, newfileName, input);
        }else if(oldFileName.equals("unit.png")){
            System.out.println("上传到: "+host+port+unitLogoPath+filePath+newfileName);
            result = FTPUtil.uploadFile(host, port, userName, passWord, unitLogoPath, filePath, newfileName, input);
        }else  if (oldFileName.equals("background.png")){
            System.out.println("上传到: "+host+port+backgroundPath+filePath+newfileName);
            result = FTPUtil.uploadFile(host, port, userName, passWord, backgroundPath, filePath, newfileName, input);
        }



        System.out.println("-----------------------ftp应用关闭------------------------");
        System.out.println("========================== 使用ftp上传文件结束");
        if (result) {
            return newfileName;
        } else {
            return "false";
        }

    }

    private Logger log = LoggerFactory.getLogger(getClass());

    // 其他图片上传
    @RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
    @ResponseBody
    public WangEditor uploadFile(
            @RequestParam("myFile") MultipartFile multipartFile,
            HttpServletRequest request) {
        System.out.println("================================= 进入上传图片功能 ===========================================");
        try {
           /* // 获取项目路径
            String realPath = request.getSession().getServletContext()
                    .getRealPath("");
            InputStream inputStream = multipartFile.getInputStream();
            String contextPath = request.getContextPath();
            // 服务器根目录的路径
            String path = realPath.replace(contextPath.substring(1), "");
            // 根目录下新建文件夹upload，存放上传图片
            String uploadPath = path + "upload";
            // 获取文件名称
            String filename = getFileName();
            // 将文件上传的服务器根目录下的upload文件夹
            File file = new File(uploadPath, filename);
            FileUtils.copyInputStreamToFile(inputStream, file);*/
            System.out.println("==================== 开始使用ftp上传文件");
            String newfileName = getFileName(multipartFile);
            String filePath = "";
            InputStream input = multipartFile.getInputStream();
            System.out.println("-----------------------ftp应用启动------------------------");
            FTPClient ftpClient = FTPUtil.connectFtpServer(host, port, userName, passWord, "gbk");
            System.out.println("FTP 连接是否成功：" + ftpClient.isConnected());
            System.out.println("FTP 连接是否有效：" + ftpClient.isAvailable());
            boolean result = FTPUtil.uploadFile(host, port, userName, passWord, baseImagePath, filePath, newfileName, input);
            System.out.println("-----------------------ftp应用关闭------------------------");
            System.out.println("========================== 使用ftp上传文件结束");
            // 返回图片访问路径
            String url = "http://" +nginxHost +":"+nginxPort+ nginxImagePath + "/"+newfileName;
            System.out.println("返回前端可以显示的图片url地址为: "+url);
            String[] str = {url};
            WangEditor we = new WangEditor(str);
            return we;
        } catch (IOException e) {
            log.error("上传文件失败", e);
        }
        return null;

    }

    // 获取上传的文件名字并且转为时间戳文件名
    public String getFileName(MultipartFile file) {
        System.out.println("================================= 上传图片重命名 ===========================================");
        String oldFileName = file.getOriginalFilename(); //获取上传文件的原名
        System.out.println("旧文件名为: " + oldFileName);
        // 使用工具生成新的文件名
        String newfileName = IDUtils.genImageName();
        newfileName = newfileName + oldFileName.substring(oldFileName.lastIndexOf("."));
        System.out.println("新生成的上传文件名为: " + newfileName);
        System.out.println("================================= 上传图片重命名结束 ===========================================");
        return newfileName;
    }
}
