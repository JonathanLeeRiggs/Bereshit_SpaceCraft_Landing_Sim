import java.text.DecimalFormat;
public class Bereshit {
	public static final double WEIGHT_EMP = 165; // kg
	public static final double WEIGHT_FULE = 420; // kg
	public static final double WEIGHT_FULL = WEIGHT_EMP + WEIGHT_FULE; // kg
	public static final double MAIN_ENG_F = 430; // N
	public static final double SECOND_ENG_F = 25; // N
	public static final double MAIN_BURN = 0.15; //liter per sec, 12 liter per m'
	public static final double SECOND_BURN = 0.009; //liter per sec 0.6 liter per m'
	public static final double ALL_BURN = MAIN_BURN + 8*SECOND_BURN;

	public static double accMax(double weight) {
		double t = 0;
		t += (8*SECOND_ENG_F) + MAIN_ENG_F;
		double ans = t/weight;
		return ans;
	}
	//the starting data
	Engines engines = new Engines(this);
	double gravity = 1.62;
	boolean landed = false;
	double vs = 24.8; // Vertical speed
	double hs = 932; // Horizontal speed
	double dist = 181*1000; // Distance to land 
	double ang = 58.3; // zero is vertical (as in landing) Orientation
	double alt = 13478;
	double time = 0; // Delta time
	double dt = 1; // sec
	double acc=0; // Acceleration rate (m/s^2)
	double distance_moved=0;
	double fuel = 121; // Fuel
	double weight = WEIGHT_EMP + fuel;
	double NN = 0.7; // rate[0,1] breaks

	boolean update() {
		if(alt < 2000) {
			engines.startLanding();
		}
		if(!landed) {
			engines.Stabalize(); 
			speedControle();
			dataComputing();
			printData();
			return true; //bereshit is still in the air
		}
		return false;

	} 
	private void printData() {
		DecimalFormat df = new DecimalFormat("###.###");
		if(time % 10 == 0 || time > 560||alt<100) 
			System.out.println("Time: "+df.format(time)+"  Height: "+df.format(alt)+"  VS: "+df.format(vs)+ "  fuel:"+df.format(fuel)+"  Weight: "+df.format(weight) +"  Acceleration: " +df.format(acc) +"  Rotation: "+df.format(ang)+"  Distance: "+df.format((distance_moved/1000))+" km");
		if(landed) 
			System.out.println("Landed successfully");		
	}
	//this method does the main computations of variation of data
	private void dataComputing() {
		double ang_rad = Math.toRadians(ang);
		double h_acc = Math.sin(ang_rad)*acc;
		double v_acc = Math.cos(ang_rad)*acc;
		double vacc = Moon.getAcc(hs);	
		this.time+=dt;
		double as = (hs/dist)/Math.toRadians(Math.PI*2); //angular speed
		ang += as;
		if(NN>1) {NN=1;}
		double dWeight = dt*ALL_BURN*NN;// delta weight
		if(fuel>0) {
			fuel -= dWeight;
			weight = WEIGHT_EMP + fuel;
			acc = NN* accMax(weight);
		}
		else { // ran out of fuel
			acc=0;
		}
		v_acc -= vacc; // moon affects the vertical acceleration 
		if(hs>0) {hs -= h_acc*dt;} // updating hs 
		dist -= hs*dt; // updating distance
		vs -= v_acc*dt;
		if(vs < 0) { vs = 1; }
		alt -= dt*vs;  // updating alltitude
		distance_moved += hs*dt;
	}
	//this method checks the situation in which the aircraft is in in order to give it the right instructions.
	//this method checks the altitude of the spacecraft and it's speed. 
	private void speedControle() {
		if(alt <= 1) {
			landed = true;
		}
		if(!landed) {
			// over 2 km above the ground
			if(alt>2000) {	
				orbiting(); // maintain a vertical speed of [18-27] m/s
			}
			// lower than 2 km
			else {
				finalLanding();
			}
		}
	}
	//this method is trying to maintain the vertical speed between 18 to 27 mps by handle the brakes (main engine)
	private void orbiting() {
		if(vs >27) {NN+=0.003;}
		if(vs <18) {NN-=0.003;}
	}
	private void finalLanding() {
		if(ang>3) 
			engines.Stabalize();
		if(alt>1000) {
			NN=0.58;
		}
		else if(alt>500) {
			NN=0.6;
		}
		else if(alt>100) {
			NN=0.69;
			if(vs<20)NN=0.65;
		}
		else if(alt>40){
			NN=0.75;
			if(vs<10 && vs>5) {NN=0.71;}
		}
		else {
			if(vs>5)NN=0.9;
			if(vs<5 && vs>3) {NN=0.75;}
			else if (vs<3) {NN=0.85;}
		}
		if(hs<2) {hs=0;}
		
	}
	
	public static void main(String[] args) {
		Bereshit myBereshit= new Bereshit();
		boolean didnotLand=true;
		while(didnotLand) {
			didnotLand=myBereshit.update();
		}
	}
}

