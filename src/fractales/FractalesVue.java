package fractales;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class FractalesVue extends JFrame implements Observer, ActionListener {
	private FractalesControleur m_controleur;
	private FractalesModele m_modele;
	private int m_largeur_fenetre;
	private int m_hauteur_fenetre;
	private JPanel m_UI;
	private JLabel m_conteneur_image;
	private JLabel m_label_iterations;
	private JLabel m_label_zoom;

	public FractalesVue(String titre, FractalesModele modele, FractalesControleur controleur, int largeur, int hauteur) {
		super(titre);
		m_modele = modele;
		m_controleur = controleur;
		m_largeur_fenetre = largeur;
		m_hauteur_fenetre = hauteur;
		initVue();
	}
	
	public FractalesVue(String titre, FractalesModele modele, FractalesControleur controleur)
	{
		super(titre);
		m_modele = modele;
		m_controleur = controleur;
		
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		m_largeur_fenetre = gd.getDisplayMode().getWidth();
		m_hauteur_fenetre = gd.getDisplayMode().getHeight();
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		initVue();
	}

	private void initVue() {
		initFenetre();
		initUI();
		initImage();
		setBackground(Color.BLACK);
		setVisible(true);
	}

	private void initImage() {
		m_modele.set_largeur_fractale((int)(m_largeur_fenetre - m_UI.getWidth()));
		m_modele.set_hauteur_fractale((int)(m_hauteur_fenetre - m_UI.getHeight()));
		JPanel zone_fractale = new JPanel();
		zone_fractale.setBackground(Color.BLACK);
		m_controleur.calculer_image_fractale();
		m_conteneur_image = new JLabel();
		m_conteneur_image.setIcon(new ImageIcon(m_modele.get_image()));
		zone_fractale.add(m_conteneur_image);
		add(zone_fractale);
	}

	private void initUI() {
		m_UI = new JPanel();
		m_UI.setLayout(new BoxLayout(m_UI,BoxLayout.Y_AXIS));
		m_UI.setBackground(Color.GRAY);
		m_UI.setPreferredSize(new Dimension(m_largeur_fenetre/6, m_hauteur_fenetre/6));
		m_UI.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.DARK_GRAY));
		
		//JButton calcul = new JButton("Calculer fractale");
		
		JButton incIterations = new JButton("+");
		JButton decIterations = new JButton("-");
		if (m_modele.get_iterations_max() <= FractalesModele.ITERATIONS_MIN)
			decIterations.setEnabled(false);
		
		JButton incZoom = new JButton("+");
		JButton decZoom = new JButton("-");
		if (m_modele.getZoom() <= FractalesModele.ZOOM_MIN)
			decZoom.setEnabled(false);
		
		JButton deplacementHaut = new JButton("\u2191");
		JButton deplacementGauche = new JButton("\u2190");
		JButton deplacementBas = new JButton("\u2193");
		JButton deplacementDroite = new JButton("\u2192");
		
		//calcul.setActionCommand("Calcul");
		
		incIterations.setActionCommand("incIterations");
		decIterations.setActionCommand("decIterations");
		
		incZoom.setActionCommand("incZoom");
		decZoom.setActionCommand("decZoom");
		
		deplacementHaut.setActionCommand("deplacementHaut");
		deplacementGauche.setActionCommand("deplacementGauche");
		deplacementBas.setActionCommand("deplacementBas");
		deplacementDroite.setActionCommand("deplacementDroite");
		
		incIterations.addActionListener(this);
		decIterations.addActionListener(this);
		
		incZoom.addActionListener(this);
		decZoom.addActionListener(this);
		
		//calcul.addActionListener(this);
		
		deplacementHaut.addActionListener(this);
		deplacementGauche.addActionListener(this);
		deplacementBas.addActionListener(this);
		deplacementDroite.addActionListener(this);
	
		//JPanel lancer_calcul = new JPanel();
		//lancer_calcul.setBackground(Color.LIGHT_GRAY);
		//lancer_calcul.add(calcul);
		
		JPanel iterations = new JPanel();
		iterations.setBackground(Color.LIGHT_GRAY);
		iterations.add(new JLabel("Iterations"));
		iterations.add(incIterations);
		m_label_iterations = new JLabel(Integer.toString(m_modele.get_iterations_max()));
		iterations.add(m_label_iterations);
		iterations.add(decIterations);
		
		JPanel zoom = new JPanel();
		zoom.setBackground(Color.LIGHT_GRAY);
		zoom.add(new JLabel("Zoom"));
		zoom.add(incZoom);
		m_label_zoom = new JLabel(Integer.toString(m_modele.getZoom()) + "%");
		zoom.add(m_label_zoom);
		zoom.add(decZoom);
		
		JPanel deplacement = new JPanel();
		deplacement.setBackground(Color.LIGHT_GRAY);
		deplacement.add(deplacementHaut);
		deplacement.add(deplacementGauche);
		deplacement.add(deplacementBas);
		deplacement.add(deplacementDroite);
		
		//m_UI.add(lancer_calcul);
		m_UI.add(iterations);
		m_UI.add(zoom);
		m_UI.add(deplacement);
		add(m_UI,BorderLayout.WEST);
	}

	private void initFenetre() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, m_largeur_fenetre, m_hauteur_fenetre);
		addWindowListener(new java.awt.event.WindowAdapter() {
	        public void windowClosing(WindowEvent winEvt) {
	            dispose();
	            System.exit(0);
	        }
	    });
	}

	@Override
	public void update(Observable o, Object arg) {
		if (m_modele.get_image() != null)
		{
		m_conteneur_image.setIcon(new ImageIcon(m_modele.get_image()));
		m_conteneur_image.repaint();
		}
		m_label_iterations.setText(Integer.toString(m_modele.get_iterations_max()));
		m_label_zoom.setText(Integer.toString(m_modele.getZoom()) + "%");
			
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand())
		{
		case "incIterations":
			m_controleur.inc_iterations_max(25);
			((JButton)e.getSource()).getParent().getComponent(3).setEnabled(true);
			break;
		case "decIterations":
			m_controleur.inc_iterations_max(-25);
			if (m_modele.get_iterations_max() <= FractalesModele.ITERATIONS_MIN)
				((JButton)e.getSource()).setEnabled(false);
			break;
		case "Calcul":
			//m_controleur.calculer_image_fractale();
			break;
		case "incZoom":
			m_controleur.inc_zoom(1);
			((JButton)e.getSource()).getParent().getComponent(3).setEnabled(true);
			//m_controleur.calculer_image_fractale();
			break;
		case "decZoom":
			m_controleur.inc_zoom(-1);
			if (m_modele.getZoom() <= FractalesModele.ZOOM_MIN)
			((JButton)e.getSource()).getParent().getComponent(3).setEnabled(false);
			//m_controleur.calculer_image_fractale();
			break;
		case "deplacementHaut":
			m_controleur.deplacement(Direction.HAUT);
			((JButton)e.getSource()).getParent().getComponent(2).setEnabled(true);
			if (m_modele.get_Ypos() <= FractalesModele.XYPOS_MIN)
				((JButton)e.getSource()).setEnabled(false);
			break;
		case "deplacementGauche":
			m_controleur.deplacement(Direction.GAUCHE);
			((JButton)e.getSource()).getParent().getComponent(3).setEnabled(true);
			if (m_modele.get_Xpos() <= FractalesModele.XYPOS_MIN)
				((JButton)e.getSource()).setEnabled(false);
			break;
		case "deplacementBas":
			m_controleur.deplacement(Direction.BAS);
			((JButton)e.getSource()).getParent().getComponent(0).setEnabled(true);
			if (m_modele.get_Ypos() >= FractalesModele.XYPOS_MAX)
				((JButton)e.getSource()).setEnabled(false);
			break;
		case "deplacementDroite":
			m_controleur.deplacement(Direction.DROITE);
			((JButton)e.getSource()).getParent().getComponent(1).setEnabled(true);
			if (m_modele.get_Xpos() >= FractalesModele.XYPOS_MAX)
				((JButton)e.getSource()).setEnabled(false);
			break;

		}
		m_controleur.calculer_image_fractale();
	}

}
