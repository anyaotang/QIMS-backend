package com.qims.quartz.service;

import com.qims.domain.entity.InspectionItem;
import com.qims.domain.mapper.InspectionItemMapper;
import com.qims.quartz.job.InspectionJob;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Quartz 动态调度服务
 * 用于管理检测项定时任务的创建、更新、删除、暂停、恢复
 */
@Service
public class QuartzScheduleService {

    private static final Logger logger = LoggerFactory.getLogger(QuartzScheduleService.class);
    private static final String JOB_GROUP = "InspectionJobGroup";
    private static final String TRIGGER_GROUP = "InspectionTriggerGroup";

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private InspectionItemMapper inspectionItemMapper;

    /**
     * 创建或更新定时任务
     */
    public void scheduleJob(Long itemId, String cronExpression) {
        try {
            JobKey jobKey = JobKey.jobKey("inspection_" + itemId, JOB_GROUP);
            JobDetail jobDetail;

            if (scheduler.checkExists(jobKey)) {
                // 更新现有任务 - 删除旧任务再创建新任务
                scheduler.deleteJob(jobKey);
                logger.info("更新检测项定时任务，itemId={}", itemId);
            } else {
                logger.info("创建检测项定时任务，itemId={}", itemId);
            }

            // 创建 JobDetail
            jobDetail = JobBuilder.newJob(InspectionJob.class)
                    .withIdentity(jobKey)
                    .withDescription("检测项定时任务, itemId=" + itemId)
                    .storeDurably()
                    .build();

            // 传递 itemId 到 JobDataMap
            jobDetail.getJobDataMap().put("itemId", itemId);

            // 创建 Cron 触发器
            CronTrigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("inspection_trigger_" + itemId, TRIGGER_GROUP)
                    .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)
                            .withMisfireHandlingInstructionDoNothing())
                    .build();

            scheduler.scheduleJob(jobDetail, trigger);
            logger.info("检测项定时任务已创建/更新，itemId={}, cronExpression={}", itemId, cronExpression);

        } catch (Exception e) {
            logger.error("创建检测项定时任务失败，itemId=" + itemId, e);
            throw new RuntimeException("创建定时任务失败", e);
        }
    }

    /**
     * 删除定时任务
     */
    public void deleteJob(Long itemId) {
        try {
            JobKey jobKey = JobKey.jobKey("inspection_" + itemId, JOB_GROUP);
            if (scheduler.checkExists(jobKey)) {
                scheduler.deleteJob(jobKey);
                logger.info("检测项定时任务已删除，itemId={}", itemId);
            } else {
                logger.warn("检测项定时任务不存在，itemId={}", itemId);
            }
        } catch (Exception e) {
            logger.error("删除检测项定时任务失败，itemId=" + itemId, e);
            throw new RuntimeException("删除定时任务失败", e);
        }
    }

    /**
     * 暂停定时任务
     */
    public void pauseJob(Long itemId) {
        try {
            JobKey jobKey = JobKey.jobKey("inspection_" + itemId, JOB_GROUP);
            if (scheduler.checkExists(jobKey)) {
                scheduler.pauseJob(jobKey);
                logger.info("检测项定时任务已暂停，itemId={}", itemId);
            } else {
                logger.warn("检测项定时任务不存在，itemId={}", itemId);
            }
        } catch (Exception e) {
            logger.error("暂停检测项定时任务失败，itemId=" + itemId, e);
            throw new RuntimeException("暂停定时任务失败", e);
        }
    }

    /**
     * 恢复定时任务
     */
    public void resumeJob(Long itemId) {
        try {
            JobKey jobKey = JobKey.jobKey("inspection_" + itemId, JOB_GROUP);
            if (scheduler.checkExists(jobKey)) {
                scheduler.resumeJob(jobKey);
                logger.info("检测项定时任务已恢复，itemId={}", itemId);
            } else {
                logger.warn("检测项定时任务不存在，itemId={}", itemId);
            }
        } catch (Exception e) {
            logger.error("恢复检测项定时任务失败，itemId=" + itemId, e);
            throw new RuntimeException("恢复定时任务失败", e);
        }
    }

    /**
     * 获取任务状态
     */
    public String getJobStatus(Long itemId) {
        try {
            JobKey jobKey = JobKey.jobKey("inspection_" + itemId, JOB_GROUP);
            if (!scheduler.checkExists(jobKey)) {
                return "NOT_EXISTS";
            }

            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            TriggerKey triggerKey = TriggerKey.triggerKey("inspection_trigger_" + itemId, TRIGGER_GROUP);

            if (!scheduler.checkExists(triggerKey)) {
                return "NO_TRIGGER";
            }

            Trigger.TriggerState state = scheduler.getTriggerState(triggerKey);
            return state.name();

        } catch (Exception e) {
            logger.error("获取任务状态失败，itemId=" + itemId, e);
            return "ERROR";
        }
    }

    /**
     * 初始化时加载所有活跃的检测项任务
     * 在应用启动时调用此方法恢复定时任务
     */
    public void initJobs() {
        try {
            // 查询所有 isActive=1 且 cronExpression 不为空的检测项
            var activeItems = inspectionItemMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<InspectionItem>()
                    .eq(InspectionItem::getIsActive, 1)
                    .isNotNull(InspectionItem::getCronExpression)
                    .ne(InspectionItem::getCronExpression, "")
            );

            for (InspectionItem item : activeItems) {
                if (item.getCronExpression() != null && !item.getCronExpression().isEmpty()) {
                    scheduleJob(item.getId(), item.getCronExpression());
                }
            }

            logger.info("已初始化 {} 个检测项定时任务", activeItems.size());

        } catch (Exception e) {
            logger.error("初始化检测项定时任务失败", e);
        }
    }

    /**
     * 删除所有检测项定时任务（用于测试或清理）
     */
    public void deleteAllJobs() {
        try {
            Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.jobGroupEquals(JOB_GROUP));
            for (JobKey jobKey : jobKeys) {
                scheduler.deleteJob(jobKey);
            }
            logger.info("已删除所有检测项定时任务，共 {} 个", jobKeys.size());
        } catch (Exception e) {
            logger.error("删除所有定时任务失败", e);
        }
    }
}