package problem4;

import java.text.DecimalFormat;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang3.tuple.Pair;

public class Balance {

	//CopyOnWriteArrayList um Concurrency zu gewährleisten
	//Diese bekommt ein Pair<Integer, Double>, wobei der Integer die Id und der
	//Double der Umsatz sind
	private CopyOnWriteArrayList<Pair<Integer, Double>> bilanz;

	public Balance () {
		//Wir initialisieren die bilanz-Liste
		bilanz = new CopyOnWriteArrayList<Pair<Integer,Double>>();
		//Es wird eine Schleife aufgerufen um die Liste mit initialen Werten zu befüllen
		for (int i = 1; i < 7; ++i)
		{
			//Es wird immer ein Pair-Objekt eingefügt (pair.of() bildet das Pair-Objekt
			//aus den Werten die man der Methode übergibt
			bilanz.add(Pair.of(i, 0.0));
		}
	}

	private void sort() {
		//Ein boolean, der angibt ob man die Liste verändert hat
		boolean changed = false;
		//Hilfsvariable für die Länge der Liste
		int laenge = bilanz.size();
		//Kopfgesteuerte Schleife zum Sortieren
		do {
			//Noch haben wir nix geändert also changed=false
			changed = false;
			//Eine Schleife über die Länge der Liste wird aufgerufen
			for (int i = 0; i < laenge - 1; i++) {
				//Wenn der Wert an der Stelle i kleiner ist, als der Wert an der
				//Stelle i+1...
				if (bilanz.get(i).getValue() < bilanz.get(i+1).getValue())
				{
					//... Erstelle ein neues Pair-Objekt aus dem Wert der 
					//bilanz-Liste an der Stelle i
					Pair<Integer, Double> pair = bilanz.get(i);
					//Setze das Pair um eine Stelle nach hinten
					bilanz.set(i, bilanz.get(i + 1));
					//Und füge das zwischengespeicherte Pair an der vorderen Stelle ein 
					bilanz.set(i + 1, pair);
					//Jetzt haben wir was verändert also changed=true
					changed = true;
				}
			}
		} while (changed);
	}

	//Synchronized damit hier nicht falsch schreibend zugegriffen wird, jeder Thread
	//darf nur einzeln hier rumschreiben
	public synchronized void addToBilanz (int id, double value) {
		//Schleife über alle Pairs in der bilanz-Liste
		for (Pair<Integer, Double> pair : bilanz) 
		{
			//Wenn die linke Seite (der Integer) = die id der Kasse ist,
			//die schreiben will...
			if (pair.getLeft() == id)
			{
				//... lösche das Pair
				bilanz.remove(pair);
				//Formatiere den neuen Wert
				DecimalFormat format = new DecimalFormat("#0.00");
				//value ist der zu addierende Wert (siehe parameter) und
				//pair.getRight() ist holt uns den Double.Wert
				String toParse = format.format(value+pair.getRight());
				//zu Schönheitszwecken Komma durch Punkt ersetzen
				toParse = toParse.replaceAll(",", ".");
				//wert als Double parsen
				double wert = Double.parseDouble(toParse);
				//Pair Objekt aus der id und dem neu gerechneten Wert
				//wieder in die Liste einfügen
				bilanz.add(Pair.of(pair.getLeft(), wert));
				//Dann wird sortiert, läuft auh synchronized, weil wir von hier aus aufrufen
				sort();
				//Hier steht nur ein wenig Ausgabe...
				System.out.println("----------");
				System.out.println("Bilanz:");
				for (int i = 0; i < bilanz.size(); ++i) {
					Pair<Integer, Double> p = bilanz.get(i);
					System.out.println((i+1) + ". - Kasse " + p.getKey() + " : " + p.getValue());
				}
				System.out.println("----------");
				//... und dann verlassen wir die Methode
				return;
			}
		}
	}
}