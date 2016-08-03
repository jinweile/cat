package com.dianping.cat.report.alert.sender.receiver;

import java.util.ArrayList;
import java.util.List;

import org.unidal.lookup.annotation.Inject;

import com.dianping.cat.config.web.js.AggregationConfigManager;
import com.dianping.cat.configuration.web.js.entity.AggregationRule;
import com.dianping.cat.home.alert.config.entity.Receiver;
import com.dianping.cat.report.alert.AlertType;
import com.dianping.cat.report.alert.sender.config.AlertConfigManager;

public class FrontEndExceptionContactor extends DefaultContactor implements Contactor {

	@Inject
	private AggregationConfigManager m_aggConfigManager;

	@Inject
	protected AlertConfigManager m_alertConfigManager;

	public static final String ID = AlertType.FrontEndException.getName();

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public List<String> queryEmailContactors(String id, String idSuffix) {
		List<String> mailReceivers = new ArrayList<String>();

		Receiver receiver = m_alertConfigManager.queryReceiverById(getId());

		if (receiver != null && !receiver.isEnable()) {
			return mailReceivers;
		} else {
			mailReceivers.addAll(buildDefaultMailReceivers(receiver));

			AggregationRule rule = m_aggConfigManager.queryAggration(id);
			if (rule != null) {
				mailReceivers.addAll(split(rule.getMails()));
			}
			return mailReceivers;
		}
	}

	@Override
	public List<String> querySmsContactors(String id, String idSuffix) {
		List<String> smsReceivers = new ArrayList<String>();
		Receiver receiver = m_alertConfigManager.queryReceiverById(getId());

		if (receiver != null && !receiver.isEnable()) {
			return smsReceivers;
		} else {
			smsReceivers.addAll(buildDefaultSMSReceivers(receiver));

			return smsReceivers;
		}
	}

	@Override
	public List<String> queryWeiXinContactors(String id, String idSuffix) {
		List<String> weixinReceivers = new ArrayList<String>();
		Receiver receiver = m_alertConfigManager.queryReceiverById(getId());

		if (receiver != null && !receiver.isEnable()) {
			return weixinReceivers;
		} else {
			weixinReceivers.addAll(buildDefaultWeixinReceivers(receiver));

			return weixinReceivers;
		}
	}
}
