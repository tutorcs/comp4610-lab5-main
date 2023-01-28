https://tutorcs.com
WeChat: cstutorcs
QQ: 749389476
Email: tutorcs@163.com
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;

public class RayTracer implements ActionListener, Runnable {

	/**
	 * RayTracer - A very simple raytracer to provide a starting point for a lab on
	 * ray tracing. The initial code uses ray casting to render a number of scenes
	 * then animates them.
	 * 
	 * Eric McCreath 2009, 2019
	 * 
	 */

	final static Dimension dim = new Dimension(800, 800);

	JFrame jframe;
	JComponent canvas;
	int numframes = 50;
	BufferedImage frames[];
	int currentframe = 0;

	Timer timer;

	int mode;

	public RayTracer() {
		SwingUtilities.invokeLater(this);
	}

	public static void main(String[] args) throws Exception {
		RayTracer rt = new RayTracer();
		if (args.length != 1) {
			throw new Exception("Must supply exactly one parameter, mode.");
		}
		rt.mode = Integer.parseInt(args[0]);
	}

	public void animate() {
		currentframe++;
		if (currentframe >= frames.length)
			currentframe = 0;
		Graphics g = canvas.getGraphics();
		canvas.paint(g);
		Toolkit.getDefaultToolkit().sync();
	}

	// raytrace - create the image for a particular scene from a particular view
	// point.
	private BufferedImage raytrace(Scene scene, View view) {
		System.out.println("Start Ray Tracing....");
		BufferedImage res = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_RGB);
		P3D startdir = view.direction.add(view.across.scale(-0.5).add(view.down.scale(-0.5)));
		for (int y = 0; y < dim.height; y++) {
			for (int x = 0; x < dim.width; x++) {
				Ray r = new Ray(view.camera, startdir.add(view.across.scale(((double) x) / dim.getWidth()))
						.add(view.down.scale(((double) y) / dim.getHeight())).normalize());
				Color c = scene.raytrace(r);
				res.setRGB(x, y, c.getRGB());
			}
		}
		return res;
	}

	private static Scene makeScene(double step) {
		Scene res = new Scene();

		// body
		res.add(new Sphere(new P3D(0.0, 1.4, 0.0), 1.9, Color.blue));
		res.add(new Sphere(new P3D(0.0, 1.0, 0.0), 2.0, Color.blue));
		res.add(new Sphere(new P3D(0.0, -1.0, 0.0), 2.0, Color.red));
		res.add(new Sphere(new P3D(0.0, -1.3, 0.0), 2.0, Color.red));

		// legs
		for (int i = 0; i < 10; i++) {
			res.add(new Sphere(new P3D(-0.5, -2.6 - (i * 0.3), 0.0), 1.0 - (i * 0.05),
					(i % 2 == 0 ? Color.red : Color.green)));
			res.add(new Sphere(new P3D(0.5, -2.6 - (i * 0.3), 0.0), 1.0 - (i * 0.05),
					(i % 2 == 0 ? Color.red : Color.green)));
		}

		// head
		res.add(new Sphere(new P3D(0.0, 4.3, 0.0), 1.0, Color.white));
		res.add(new Sphere(new P3D(0.35, 4.35, -0.9), 0.1, Color.black));
		res.add(new Sphere(new P3D(-0.35, 4.35, -0.9), 0.1, Color.black));

		// balls

		res.add(new Sphere(ballpos(step), 0.3, Color.orange));
		res.add(new Sphere(ballpos(step + 1.0 / 5.0), 0.3, Color.green));
		res.add(new Sphere(ballpos(step + 2.0 / 5.0), 0.3, Color.red));
		res.add(new Sphere(ballpos(step + 3.0 / 5.0), 0.3, Color.yellow));
		res.add(new Sphere(ballpos(step + 4.0 / 5.0), 0.3, Color.white));

		Sphere m;
		res.add(m = new Sphere(new P3D(-14.35, 7.35, 2.0), 11.0, Color.black));
		m.material.reflection = 1.0;

		res.add(m = new Sphere(new P3D(14.35, 7.35, 2.0), 11.0, Color.black));
		m.material.reflection = 1.0;


		res.add( new Plane(new P3D(0, -5.85, 0), new P3D(0.0, 1.0, 0.0)));

		return res;
	}

	private static P3D ballpos(double step) {

		double bhmax = 5.0;
		double bhmin = 0.3;
		double bwmax = 2.0;
		double bwmin = 1.0;

		double phase1 = 0.45;

		while (step > 1.0)
			step -= 1.0;
		if (step < phase1) {
			double phase = step / phase1;
			double x = bwmin - (bwmax + bwmin) * phase;
			double par = (phase - 0.5) * 2.0;
			double y = bhmin + bhmax * (1.0 - par * par);
			return new P3D(x, y, -2.5);
		} else if (step < 0.5) {
			double phase = (step - phase1) / (0.5 - phase1);
			double x = -bwmax + (bwmax - bwmin) * phase;
			double y = bhmin;
			return new P3D(x, y, -2.5);
		} else if (step < 0.5 + phase1) {
			double phase = (step - 0.5) / phase1;
			double x = -bwmin + (bwmax + bwmin) * phase;
			double par = (phase - 0.5) * 2.0;
			double y = bhmin + bhmax * (1.0 - par * par);
			return new P3D(x, y, -2.5);
		} else {
			double phase = (step - 0.5 - phase1) / (0.5 - phase1);
			double x = bwmax - (bwmax - bwmin) * phase;
			double y = bhmin;
			return new P3D(x, y, -2.5);

		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == timer) {
			animate();
		}
	}

	@Override
	public void run() {
		jframe = new JFrame("RayTracer - Mode " + mode);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frames = new BufferedImage[numframes];
		for (int i = 0; i < frames.length; i++) {
			frames[i] = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_RGB);
		}

		canvas = new JComponent() {
			public void paint(Graphics g) {
				if (frames[currentframe] != null)
					g.drawImage(frames[currentframe], 0, 0, null);
			}
		};
		canvas.setPreferredSize(dim);
		jframe.getContentPane().add(canvas);
		jframe.pack();
		jframe.setVisible(true);

		timer = new Timer(100, this);

		View view = new View(new P3D(4.0, 4.0, -20.0), new P3D(-2.0, -2.0, 10.0), new P3D(10.0, 0.0, 2.0),
				new P3D(0.0, -10.0, 2.0));

		// generate frames - this may take some time so best done in the background using a worker thread.
		SwingWorker worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				double stage = 0.0;
				for (int i = 0; i < frames.length; i++) {
					Scene scene = makeScene(stage);
					frames[i] = raytrace(scene, view);
					stage += 1.0 / numframes;
					currentframe = i;
					canvas.repaint();
				}
				return null;
			}

			// start the animation once the frames are generated
			@Override
			public void done() {
				timer.start();
			}

		};
		worker.execute();

	}

}
