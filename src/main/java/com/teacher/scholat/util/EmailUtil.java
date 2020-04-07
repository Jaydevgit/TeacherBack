package com.teacher.scholat.util;

import com.teacher.scholat.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

public class EmailUtil {
    @Autowired
    private MailService mailService;
    @Autowired
    private TemplateEngine templateEngine;

    /**
     * 发送简单纯文本邮件
     */
    public void sendSimpleMail(String email) {
        mailService.sendSimpleMail(email, "发送邮件测试", "大家好，这是我用springboot进行发送邮件测试");
    }

    /**
     * 发送HTML邮件
     */
    public void sendHtmlMail() {
        String content = "<html><body><h3><font color=\"red\">" +
                "大家好，这是springboot发送的HTML邮件" + "</font></h3></body></html>";
        mailService.sendHtmlMail("receiver@email.com", "发送邮件测试", content);
    }

    /**
     * 发送带附件的邮件
     */
    public void sendAttachmentMail() {
        String content = "<html><body><h3><font color=\"red\">" + "大家好，这是springboot发送的HTML邮件，有附件哦" + "</font></h3></body></html>";
        String filePath = "your file path";
        mailService.sendAttachmentMail("receiver@email.com", "发送邮件测试", content, filePath);
    }

    /**
     * 发送带图片的邮件
     */
    public void sendInlineResourceMail() {
        String rscPath = "your picture path";
        String rscId = "skill001";
        String content = "<html><body><h3><font color=\"red\">" + "大家好，这是springboot发送的HTML邮件，有图片哦" + "</font></h3>"
                + "<img src=\'cid:" + rscId + "\'></body></html>";
        mailService.sendInlineResourceMail("receiver@email.com", "发送邮件测试", content, rscPath, rscId);
    }

    /**
     * 指定模板发送邮件
     */
    public void templateMail(String email,String token) {
        System.out.println("--------------- 动作开始:发送模板邮件 ---------------------");
        //向Thymeleaf模板传值，并解析成字符串
        Context context = new Context();
        context.setVariable("token", token);
        String emailContent = templateEngine.process("unitApplyModify", context);
        mailService.sendHtmlMail(email, "学院师资队伍栏目申请修改", emailContent);
    }

}
