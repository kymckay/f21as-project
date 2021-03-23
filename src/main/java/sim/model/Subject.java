package sim.model;

import sim.view.Observer;

// Reference: PatternsExampleCode
public interface Subject {

	public void registerObserver(Observer o);

	public void removeObserver(Observer o);

	public void notifyObservers();
}
