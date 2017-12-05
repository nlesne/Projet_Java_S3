package fractales;

public class Main {

	public static void main(String[] args) {
		FractalesModele modele = new FractalesModele();
		FractalesControleur controleur = new FractalesControleur(modele);
		FractalesVue vue = new FractalesVue("Mandelbrot", modele, controleur);
		
		modele.addObserver(vue);
	}

	public Main() {
		// TODO Auto-generated constructor stub
	}

}
