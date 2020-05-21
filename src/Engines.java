
public class Engines {
	double initAngle= 58.3;
	private boolean isLanding;
	double currentRotation;
	Engine FrontRight1;
	Engine FrontRight2;
	Engine FrontLeft1;
	Engine FrontLeft2;
	Engine BackRight1;
	Engine BackRight2;
	Engine BackLeft1;
	Engine BackLeft2;
	Bereshit myBereshit;
	
	public Engines(Bereshit b) {
		myBereshit = b;
		initEîòïî÷ã();
	}
	public void initEîòïî÷ã() {
		FrontRight1 = new Engine(this, 1);
		FrontRight2 = new Engine(this,0.75);
		FrontLeft1 = new Engine(this,1);
		FrontLeft2 = new Engine(this,0.75);
		BackRight1 = new Engine(this,-1);
		BackRight2 = new Engine(this,-0.75);
		BackLeft1 = new Engine(this,-1);
		BackLeft2 = new Engine(this,-0.75);
		isLanding=false;
	}
	// Entering to landing 
	public void startLanding() {
		isLanding=true;
	}
	//this method is stabilizing the aircraft using the current rotation of the aircraft by operating each engine if needed.
	public void Stabalize() {
		currentRotation = myBereshit.ang;
		if(!isLanding) {	
			if(currentRotation > initAngle+5) {	 
				BackRight1.Stabilize();
				BackLeft1.Stabilize();
			}
			else if(currentRotation > initAngle+3) {	 
				BackRight1.Stabilize();
				BackLeft2.Stabilize();
			}
			else if(currentRotation > initAngle+1) {	 
				BackRight2.Stabilize();
			}	
			else if(currentRotation < initAngle+5) {	 
				FrontRight1.Stabilize();
				FrontLeft1.Stabilize();
			}
			else if(currentRotation < initAngle+3) {	 
				FrontRight1.Stabilize();
				FrontLeft2.Stabilize();
			}
			else if(currentRotation < initAngle+1) {	 
				FrontRight2.Stabilize();
			}	
		}
		else {	
			BackRight1.Stabilize();
			FrontRight1.Stabilize();
			BackLeft2.Stabilize();
		}

	}


}
