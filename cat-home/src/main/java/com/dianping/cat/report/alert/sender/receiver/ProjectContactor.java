package com.dianping.cat.report.alert.sender.receiver;

import java.util.ArrayList;
import java.util.List;

import org.unidal.lookup.annotation.Inject;

import com.dianping.cat.core.dal.Project;
import com.dianping.cat.home.alert.config.entity.Receiver;
import com.dianping.cat.report.alert.sender.config.AlertConfigManager;
import com.dianping.cat.service.ProjectService;

import org.unidal.lookup.util.StringUtils;

public abstract class ProjectContactor extends DefaultContactor implements Contactor {

	@Inject
	protected ProjectService m_projectService;

	@Inject
	protected AlertConfigManager m_configManager;

	@Override
	public List<String> queryEmailContactors(String id, String idSuffix) {
		List<String> mailReceivers = new ArrayList<String>();
        String idName = getId();
        if (idSuffix != null && !idSuffix.isEmpty()) {
            idName += "_" + idSuffix; 
        }
		Receiver receiver = m_configManager.queryReceiverById(idName);

		if (receiver != null && !receiver.isEnable()) {
			return mailReceivers;
		} else {
			mailReceivers.addAll(buildDefaultMailReceivers(receiver));

			if (StringUtils.isNotEmpty(id)) {
				Project project = m_projectService.findByDomain(id);

				if (project != null) {
					mailReceivers.addAll(split(project.getEmail()));
				}
			}
			return mailReceivers;
		}
	}

	@Override
	public List<String> querySmsContactors(String id, String idSuffix) {
		List<String> smsReceivers = new ArrayList<String>();
        String idName = getId();
        if (idSuffix != null && !idSuffix.isEmpty()) {
            idName += "_" + idSuffix; 
        }
		Receiver receiver = m_configManager.queryReceiverById(idName);

		if (receiver != null && !receiver.isEnable()) {
			return smsReceivers;
		} else {
			smsReceivers.addAll(buildDefaultSMSReceivers(receiver));

			if (StringUtils.isNotEmpty(id)) {
				Project project = m_projectService.findByDomain(id);

				if (project != null) {
					smsReceivers.addAll(split(project.getPhone()));
				}
			}
			return smsReceivers;
		}
	}

	@Override
	public List<String> queryWeiXinContactors(String id, String idSuffix) {
		List<String> weixinReceivers = new ArrayList<String>();
        String idName = getId();
        if (idSuffix != null && !idSuffix.isEmpty()) {
            idName += "_" + idSuffix; 
        }
		Receiver receiver = m_configManager.queryReceiverById(idName);

		if (receiver != null && !receiver.isEnable()) {
			return weixinReceivers;
		} else {
			weixinReceivers.addAll(buildDefaultWeixinReceivers(receiver));

			if (StringUtils.isNotEmpty(id)) {
				Project project = m_projectService.findByDomain(id);

				if (project != null) {
					weixinReceivers.addAll(split(project.getEmail()));
				}
			}
			return weixinReceivers;
		}
	}

}
