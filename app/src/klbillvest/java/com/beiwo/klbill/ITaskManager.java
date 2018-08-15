package com.beiwo.klbill;

/**
 *
 *
 * @author:
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/8/6
 */

public interface ITaskManager {

    public void addTask(WorkTask task);

    public void removeTask(String taskId, boolean cancelIfRunning);

    public void removeAllTask(boolean cancelIfRunning);

    public int getTaskCount(String taskId);

}
