package problem4;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang3.tuple.Pair;

public class Balance {
	
	private CopyOnWriteArrayList<Pair<Integer, Double>> bilanz;

	public Balance () {
		bilanz = new CopyOnWriteArrayList<Pair<Integer,Double>>();
		for (int i = 1; i < 7; ++i) {
			bilanz.add(Pair.of(i, 0.0));
		}
	}
	
	private void sort() {
		boolean changed = false;
		int n = bilanz.size();
		do {
			changed = false;
			for (int i = 0; i < n - 1; ++i) {
				if (bilanz.get(i).getValue() < bilanz.get(i+1).getValue()) {
					Pair<Integer, Double> pair = bilanz.get(i);
					bilanz.set(i, bilanz.get(i + 1));
					bilanz.set(i + 1, pair);
					changed = true;
				}
			}
		} while (changed);
	}
	
	public synchronized void addToBilanz (int id, double value) {
		for (Pair<Integer, Double> pair : bilanz) {
			if (pair.getLeft() == id) {
				bilanz.remove(pair);
				DecimalFormat format = new DecimalFormat("#.##");
				String wert = format.format(value);
				wert = wert.replaceAll(",", ".");
				Double toAdd = Double.parseDouble(wert);
				bilanz.add(Pair.of(pair.getKey(), pair.getValue() + toAdd));
				sort();
				System.out.println("----------");
				System.out.println("Bilanz:");
				for (int i = 0; i < bilanz.size(); ++i) {
					Pair<Integer, Double> p = bilanz.get(i);
					System.out.println((i+1) + ". - Kasse " + p.getKey() + " : " + p.getValue());
				}
				System.out.println("----------");
				return;
			}
		}
	}
	
}
