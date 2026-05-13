package com.qims.quartz.job;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qims.common.config.JwtProperties;
import com.qims.domain.entity.InspectionItem;
import com.qims.domain.entity.InspectionRecord;
import com.qims.domain.mapper.InspectionItemMapper;
import com.qims.domain.mapper.InspectionRecordMapper;
import jakarta.mail.internet.MimeMessage;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 检测项定时采集 Job
 * 当 InspectionItem 的 isActive=true 且 dataSource=0 或 1 时，
 * 根据 cronExpression 创建 Quartz Job 定时执行此任务
 */
@Component
public class InspectionJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(InspectionJob.class);

    @Autowired
    private InspectionItemMapper inspectionItemMapper;

    @Autowired
    private InspectionRecordMapper inspectionRecordMapper;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private JwtProperties jwtProperties;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // 从 JobDataMap 中获取检测项 ID
        Long itemId = context.getJobDetail().getJobDataMap().getLong("itemId");
        logger.info("开始执行检测项定时任务，itemId={}", itemId);

        try {
            // 1. 获取检测项配置
            InspectionItem item = inspectionItemMapper.selectById(itemId);
            if (item == null || item.getIsActive() == null || item.getIsActive() != 1) {
                logger.warn("检测项不存在或已禁用，itemId={}", itemId);
                return;
            }

            // 2. 根据 dataSource 采集/计算值
            BigDecimal value = collectValue(item);

            // 3. 保存检测记录
            InspectionRecord record = new InspectionRecord();
            record.setItemId(itemId);
            record.setValue(value);
            record.setInspectTime(LocalDateTime.now());
            record.setDataSource(3); // 3-定时采集
            record.setRemark("定时任务自动采集");

            // 判断是否达标（与目标值比较）
            if (item.getTargetValue() != null && value != null) {
                try {
                    BigDecimal target = new BigDecimal(item.getTargetValue());
                    record.setIsQualified(value.compareTo(target) <= 0 ? 1 : 0);
                } catch (NumberFormatException e) {
                    record.setIsQualified(1); // 无法比较时默认达标
                }
            } else {
                record.setIsQualified(1);
            }

            inspectionRecordMapper.insert(record);
            logger.info("检测记录已保存，recordId={}, value={}, isQualified={}",
                    record.getId(), value, record.getIsQualified());

            // 4. 检查推送条件，匹配则发送邮件
            checkAndSendEmail(item, record, value);

        } catch (Exception e) {
            logger.error("检测项定时任务执行失败，itemId=" + itemId, e);
            throw new JobExecutionException(e);
        }
    }

    /**
     * 根据 dataSource 采集或计算值
     * dataSource: 0-手动, 1-API采集, 2-公式计算
     */
    private BigDecimal collectValue(InspectionItem item) {
        Integer dataSource = item.getDataSource();
        if (dataSource == null) {
            return null;
        }

        try {
            if (dataSource == 1 && item.getApiUrl() != null && !item.getApiUrl().isEmpty()) {
                // API 采集
                String result = restTemplate.getForObject(item.getApiUrl(), String.class);
                if (result != null) {
                    return new BigDecimal(result.trim());
                }
            } else if (dataSource == 2 && item.getFormula() != null && !item.getFormula().isEmpty()) {
                // 公式计算（使用 Aviator）
                // 注意：需要在 pom.xml 中添加 Aviator 依赖
                // Object formulaResult = AviatorEvaluator.execute(item.getFormula());
                // return new BigDecimal(formulaResult.toString());
                logger.warn("公式计算功能需要集成 Aviator，itemId={}", item.getId());
            }
        } catch (Exception e) {
            logger.error("采集值失败，itemId=" + item.getId(), e);
        }

        return null;
    }

    /**
     * 检查推送条件，如果匹配则发送邮件
     */
    private void checkAndSendEmail(InspectionItem item, InspectionRecord record, BigDecimal value) {
        if (item.getPushEmails() == null || item.getPushEmails().isEmpty()) {
            return;
        }

        boolean shouldPush = false;

        // 简单推送条件：如果不达标则推送
        if (record.getIsQualified() != null && record.getIsQualified() == 0) {
            shouldPush = true;
        }

        // TODO: 解析 item.getPushCondition() 中的 Aviator 表达式进行复杂条件判断

        if (shouldPush) {
            String[] emails = item.getPushEmails().split(",");
            for (String email : emails) {
                try {
                    sendEmail(email.trim(), item, record, value);
                } catch (Exception e) {
                    logger.error("发送邮件失败，email=" + email, e);
                }
            }
        }
    }

    /**
     * 发送邮件
     */
    private void sendEmail(String to, InspectionItem item, InspectionRecord record, BigDecimal value) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject("【QIMS告警】检测项不达标：" + item.getName());

        String content = String.format(
                "<html><body>" +
                "<h3>检测项告警</h3>" +
                "<p><b>检测项：</b>%s</p>" +
                "<p><b>检测值：</b>%s</p>" +
                "<p><b>目标值：</b>%s</p>" +
                "<p><b>是否达标：</b><span style='color:red;font-weight:bold;'>不达标</span></p>" +
                "<p><b>检测时间：</b>%s</p>" +
                "<p>请及时处理！</p>" +
                "</body></html>",
                item.getName(),
                value != null ? value.toString() : "N/A",
                item.getTargetValue() != null ? item.getTargetValue() : "N/A",
                record.getInspectTime()
        );

        helper.setText(content, true);
        mailSender.send(message);
        logger.info("邮件已发送，to={}, itemId={}", to, item.getId());
    }
}
