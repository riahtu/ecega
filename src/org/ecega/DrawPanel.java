package org.ecega;

import static java.awt.BasicStroke.CAP_BUTT;
import static java.awt.BasicStroke.JOIN_MITER;
import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JPanel;

import org.jscience.mathematics.number.Float64;

/**
 * The panel which draws the polygons.
 * 
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version $Id: Geometry.java 826 2011-02-25 10:41:09Z fwilhelm $
 */
class DrawPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private static final Stroke THICK = new BasicStroke(
			3.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER
		);
//	private static final Stroke NORMAL = new BasicStroke(
//			1.8f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER
//		);	
	private static final Stroke THIN = new BasicStroke(
			1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER
		);	
	private static final Stroke DASHED = new BasicStroke(
		1.0f, CAP_BUTT, JOIN_MITER, 1f, new float[]{6, 3}, 0f
	);
	
	private EnergyDataSet _sourceConsumptionDataSet;
	private Float64[] _targetPolynom;
	
	private final AtomicReference<Float64[]> _populationBestPolynom = 
		new AtomicReference<Float64[]>();
	
	private final AtomicReference<Float64[]> _alltimeBestPolynom =
		new AtomicReference<Float64[]>();
	
	public DrawPanel() {	
		addComponentListener(new ComponentAdapter() {
			@Override public void componentResized(ComponentEvent e) {
				DrawPanel.this.repaint();
			}
		});
	}
	
	@Override
	public void paintComponent(final Graphics graphics) {
		super.paintComponent(graphics);
		
		final Graphics2D g2d = (Graphics2D)graphics;
		g2d.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
		
		
		paintCoordinates(g2d);
		
		final AffineTransform transform = AffineTransform.getScaleInstance(1.0, 1.0);
		
		if (_sourceConsumptionDataSet != null) {
//			TODO draw source consumption function
//			paint(g2d, _sourcePolygon, Color.LIGHT_GRAY, THICK, transform);
		}
		
		if (_targetPolynom != null) {
			// TODO draw current estimation
			/*paint(g2d, _targetPolynom, Color.GREEN, DASHED, transform);
			
			AffineTransform at = _alltimeBestPolynom.get();
			if (at != null) {
				paint(g2d, _targetPolynom, Color.GREEN, THICK, at);
			} else {
				paint(g2d, _targetPolynom, Color.GREEN, THICK, transform);
			}
			
			at = _populationBestPolynom.get();
			if (at != null) {
				paint(g2d, _targetPolynom, Color.BLUE, THIN,  at);
			} else {
				paint(g2d, _targetPolynom, Color.BLUE, THIN, transform);
			}*/
		}
	}
	
	private void paintCoordinates(final Graphics2D graphics) {
		final Dimension size = getSize();
		final int ox = size.width/2;
		final int oy = size.height/2;
		
		graphics.drawLine(ox, 0, ox, size.height);
		graphics.drawLine(0, oy, size.width, oy);
	}
	
	
	private void paint(
		final Graphics2D graphics, 
		final Point2D[] polygon,
		final Color color,
		final Stroke stroke,
		final AffineTransform transform
	) {
		final Color oldColor = graphics.getColor();
		final Stroke oldStroke = graphics.getStroke();
		
		graphics.setColor(color);
		graphics.setStroke(stroke);
		
		final Dimension size = getSize();
		final int ox = size.width/2;
		final int oy = size.height/2;
		
		for (int i = 0; i < polygon.length; ++i) {
			final Point2D p1 = transform.transform(polygon[i], null);
			final Point2D p2 = transform.transform(polygon[(i + 1)%polygon.length], null);
			
			graphics.drawLine(
				(int)p1.getX() + ox, -(int)p1.getY() + oy, 
				(int)p2.getX() + ox, -(int)p2.getY() + oy
			);
		}
		
		graphics.setColor(oldColor);
		graphics.setStroke(oldStroke);
	}
	
	public void setSourceDataSet(final EnergyDataSet sourceDataSet) {
		_sourceConsumptionDataSet = sourceDataSet;
	}
	
	public void setTargetPolygon(final Float64[] polygon) {
		_targetPolynom = polygon;
	}
	
	public void setPopulationBestTransform(final Float64[] transform) {
		_populationBestPolynom.set(transform);
	}
	
	public void setAlltimeBestTransform(final Float64[] transform) {
		_alltimeBestPolynom.set(transform);
	}
	
}