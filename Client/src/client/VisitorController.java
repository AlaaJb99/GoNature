package client;

import logic.Visitor;

public class VisitorController {
	public static Visitor v1 = new Visitor();
	public static boolean haveOrder;
	public static boolean wating;
	public static boolean available;
	public static boolean chara;
	public static boolean beforDay;

	public VisitorController() {
	}

	public void visitorMaker(String msg) {
		String[] result = msg.split("\\s");
		v1.setId(result[0]);
		if (result[1].equals("OrderNoWaitingfalse"))
			haveOrder = true;
		else if (result[1].equals("OrderNoWaitingtrue")) {
			beforDay = true;
			haveOrder = true;
		} else if (result[1].equals("NoOrderwaitingNo"))
			wating = true;
		else if (result[1].equals("NoOrderNoWaiting")) {
			haveOrder = false;
			wating = false;
		} else if (result[1].equals("Error"))
			chara = true;
		else {
			available = true;
			haveOrder = false;
		}
	}
}