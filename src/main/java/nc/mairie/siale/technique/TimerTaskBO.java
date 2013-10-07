package nc.mairie.siale.technique;

import java.util.Timer;
import java.util.TimerTask;

import com.crystaldecisions.sdk.framework.IEnterpriseSession;

class TimerTaskBO extends TimerTask {

	private IEnterpriseSession enterpriseSession;
	private Timer timer=new Timer();
	
	
	public TimerTaskBO(IEnterpriseSession enterpriseSession) {
		super();
		this.enterpriseSession = enterpriseSession;
	}

	public void schedule(int delay) {
		timer.schedule(this, delay);
	}
	
	
	@Override
	public void run() {
		try {
			enterpriseSession.logoff();
		} catch (Exception e) {
			//Bon... bhen tant pis...
		} finally {
			enterpriseSession = null;
			timer.cancel();
		}
	}
	
}