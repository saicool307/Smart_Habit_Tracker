package app.util;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Very small custom Swing component to draw a simple line chart.
 * - Does not rely on any external libraries.
 * - Expects a list of integer points (e.g., 0/1 for completed days or numeric progress).
 *
 * Note: this intentionally does not set colors/styles so it stays neutral.
 */
public class SimpleLineChart extends JPanel {
    private List<Integer> points;
    private String title = "Progress";

    public SimpleLineChart(List<Integer> points) {
        this.points = points;
        setPreferredSize(new Dimension(400, 200));
    }

    public void setTitle(String t) { this.title = t; }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (points == null || points.isEmpty()) {
            g.drawString("No data to display", 10, 20);
            return;
        }

        int w = getWidth();
        int h = getHeight();
        int padding = 30;
        int max = points.stream().max(Integer::compareTo).orElse(1);
        int n = points.size();
        int stepX = (n <= 1) ? 0 : (w - 2*padding) / (n - 1);

        // axes
        g.drawLine(padding, h - padding, w - padding, h - padding); // x axis
        g.drawLine(padding, padding, padding, h - padding); // y axis

        // draw points and lines
        int prevX = padding;
        int prevY = h - padding - (points.get(0) * (h - 2*padding) / Math.max(1, max));
        g.fillOval(prevX - 3, prevY - 3, 6, 6);

        for (int i = 1; i < n; i++) {
            int x = padding + i * stepX;
            int y = h - padding - (points.get(i) * (h - 2*padding) / Math.max(1, max));
            g.fillOval(x - 3, y - 3, 6, 6);
            g.drawLine(prevX, prevY, x, y);
            prevX = x;
            prevY = y;
        }

        // title
        g.drawString(title, w / 2 - 20, 15);
    }
}
