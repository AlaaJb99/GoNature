package server;

import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

import db.DBConnection;

public class MyTimerTask extends TimerTask {
	@Override
	public void run() {
		try {
			DBConnection.sendMessageBeforOneDay();
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					String[] cancel = new String[10];
					for (String vid : DBConnection.beforDay) {
						try {
							String order = DBConnection.OrderIn(vid);
							System.err.println(order);
							String[] o = order.split("\\s");
							cancel[1] = o[8];
							cancel[2] = o[1];
							cancel[3] = o[2];
							cancel[4] = o[3];
							cancel[5] = o[4];
							DBConnection.cancelOrder(cancel);
							DBConnection.beforDay.remove(vid);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}, 1000 * 2);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}