
public class Arrow {
	double headx, heady;
	double tailx, taily;
	double sidex1, sidey1, sidex2, sidey2;
	
	private double length;
	private double sideLength;
	
	public Arrow(double x1, double y1, double x2, double y2) {
		tailx = x1;
		taily = y1;
		headx = x2;
		heady = y2;
		length = Math.sqrt( (x2-x1)*(x2-x1) + (y2-y1)*(y2-y1) );
		sideLength = 0.3*length;
		setSides();
	}
	private void setSides() {
		double x1 = headx - tailx;
		double y1 = heady - taily;
		double r1 = Math.sqrt(x1*x1 + y1*y1);
		double theta1 = Math.atan2(y1, x1);
		
		double r2 = sideLength;
		double theta2 = theta1 + Math.PI/4;
		double x2 = r2*Math.cos(theta2);
		double y2 = r2*Math.sin(theta2);
		
		double r3 = sideLength;
		double theta3 = theta1 - Math.PI/4;
		double x3 = r3*Math.cos(theta3);
		double y3 = r3*Math.sin(theta3);
		
		sidex1 = headx+x2;
		sidey1 = heady+y2;
		sidex2 = headx+x3;
		sidey2 = heady+y3;
		
		double[] r = new double[]{tailx-headx, taily-heady};
		double rmag = Math.sqrt(r[0]*r[0] + r[1]*r[1]);
		double[] rhat = new double[]{r[0]/rmag, r[1]/rmag};
		sidex1 += rhat[0]*sideLength*Math.sqrt(2);
		sidey1 += rhat[1]*sideLength*Math.sqrt(2);
		sidex2 += rhat[0]*sideLength*Math.sqrt(2);
		sidey2 += rhat[1]*sideLength*Math.sqrt(2);
	}
}
