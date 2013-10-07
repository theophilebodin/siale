package nc.mairie.siale.technique;

import java.util.TimerTask;

import com.crystaldecisions.sdk.framework.IEnterpriseSession;

class TimerTaskBO extends TimerTask {

	private IEnterpriseSession enterpriseSession;
	
	
	public TimerTaskBO(IEnterpriseSession enterpriseSession) {
		super();
		this.enterpriseSession = enterpriseSession;
	}

	
	@Override
	public void run() {
		try {
			enterpriseSession.logoff();
		} catch (Exception e) {
			//Bon... bhen tant pis...
		} finally {
			enterpriseSession = null;
		}
	}
	
}