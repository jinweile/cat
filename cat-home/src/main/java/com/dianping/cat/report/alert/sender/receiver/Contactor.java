package com.dianping.cat.report.alert.sender.receiver;

import java.util.List;

public interface Contactor {

	public String getId();

	public List<String> queryEmailContactors(String id, String idSuffix);

	public List<String> queryWeiXinContactors(String id, String idSuffix);

	public List<String> querySmsContactors(String id, String idSuffix);
}
