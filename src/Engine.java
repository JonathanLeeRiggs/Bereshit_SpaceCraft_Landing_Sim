
public class Engine {
	
	double powerToRotation; 
	Engines myEngines;
	
	public Engine(Engines engines, double powerto) {
		this.powerToRotation = powerto;
		myEngines = engines;
	}
	//this method helps to stabilize an engine by using its proportional power.
	public void Stabilize() {
		if(myEngines.myBereshit.ang > 0) {
			myEngines.myBereshit.ang += powerToRotation;
		}
	}
}
